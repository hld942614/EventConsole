package com.project.uhdbackend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhdbackend.dto.CaseCommentCreateRequest;
import com.project.uhdbackend.dto.CaseDTO;
import com.project.uhdbackend.dto.CommentDTO;
import com.project.uhdbackend.dto.EventCommentCreateRequest;
import com.project.uhdbackend.dto.EventDTO;
import com.project.uhdbackend.entity.Case;
import com.project.uhdbackend.entity.Comment;
import com.project.uhdbackend.entity.Event;
import com.project.uhdbackend.enums.CaseStatus;
import com.project.uhdbackend.enums.EventStatus;
import com.project.uhdbackend.realtime.event.EventType;
import com.project.uhdbackend.realtime.service.RealtimeEventService;
import com.project.uhdbackend.repository.CaseRepository;
import com.project.uhdbackend.repository.CommentRepository;
import com.project.uhdbackend.repository.EventRepository;
import com.project.uhdbackend.utils.CommentStatus;

@Service
public class CommentService {

	/** 切換子狀態時，若前端沒帶自訂內容，落地用的預設留言文字。 */
	private static final Map<CommentStatus, String> DEFAULT_COMMENT_TEXT = Map.of(CommentStatus.TRANSFERRED_TO_PIC,
			"[已轉交 PIC] 已通知負責人，等待對方回應", CommentStatus.INVESTIGATING, "[調查中] 正在排查與分析原因", CommentStatus.WAITING_VENDOR,
			"[等待廠商] 正在等待外部支援或原廠回覆", CommentStatus.FIXING, "[修復中] 正在動手調整或套用解法", CommentStatus.VERIFYING,
			"[觀察驗證] 已處理完畢，正在確認系統穩定度");

	private final CommentRepository commentRepository;
	private final CaseRepository caseRepository;
	private final RealtimeEventService realtimeEventService;
	private final EventRepository eventRepository;
	private final EventStatusService eventStatusService;
	private final CaseService caseService;

	public CommentService(CommentRepository commentRepository, CaseRepository caseRepository,
			RealtimeEventService realtimeEventService, EventRepository eventRepository,
			EventStatusService eventStatusService, CaseService caseService) {
		this.commentRepository = commentRepository;
		this.caseRepository = caseRepository;
		this.realtimeEventService = realtimeEventService;
		this.eventRepository = eventRepository;
		this.eventStatusService = eventStatusService;
		this.caseService = caseService;
	}

	@Transactional
	public CommentDTO createCaseComment(CaseCommentCreateRequest request) {
		Case target = caseRepository.findById(request.getCaseId())
				.orElseThrow(() -> new NoSuchElementException("Case not found: " + request.getCaseId()));

		CommentStatus commentStatus = request.getStatus();

		String content = resolveContent(request.getContent(), commentStatus);

		Comment comment = new Comment();
		comment.setCommentContent(content);
		comment.setCommentAuthor(request.getAuthor().trim());
		comment.setCommentTimestamp(LocalDateTime.now());
		comment.setStatus(commentStatus);

		target.addComment(comment);

		Comment saved = commentRepository.save(comment);

		// 先讓既有的「留言即晉升」邏輯跑完（OPEN -> PROCESSING），
		// 這樣即使是第一筆留言同時帶子狀態，晉升也會先發生，
		// 下面套用子狀態時 target 的狀態才會是 PROCESSING。
		caseService.ensureProcessingOnComment(target);

		if (commentStatus != null) {
			applyCaseProcessingDetail(target, commentStatus);
		}
		
		if (commentStatus == CommentStatus.RESOLVED) {
			caseService.resolveCase(target.getId(), request.getAuthor());
		}
		
		if (commentStatus == CommentStatus.CLOSED) {
			caseService.closeCase(target.getId(), request.getAuthor());
		}
		
		CommentDTO dto = new CommentDTO(saved);
		realtimeEventService.publish(EventType.COMMENT_CREATED, "CASE", target.getId(), dto);
		return dto;
	}

	@Transactional
	public CommentDTO createEventComment(EventCommentCreateRequest request) {
		Event target = eventRepository.findByEventId(request.getEventId())
				.orElseThrow(() -> new NoSuchElementException("Event not found: " + request.getEventId()));

		// 事件一旦被分類進 Case，後續互動一律改走 Case 層級留言，
		// 不再對單一 Event 留言（避免 Event 與 Case 兩邊狀態各自演進、互相混淆）。
		if (target.getEventStatus() == EventStatus.CLASSIFIED) {
			throw new IllegalStateException("此事件已分類至 Case，請至對應 Case 頁面留言: eventId=" + request.getEventId());
		}

		CommentStatus commentStatus = request.getStatus();

		String content = resolveContent(request.getContent(), commentStatus);

		Comment comment = new Comment();
		comment.setCommentContent(content);
		comment.setCommentAuthor(request.getAuthor().trim());
		comment.setCommentTimestamp(LocalDateTime.now());
		comment.setStatus(commentStatus);

		target.addComment(comment);
		Comment saved = commentRepository.save(comment);

		// 先讓 UNREAD/ACKNOWLEDGED -> PROCESSING 的晉升跑完，
		// 再檢查並套用子狀態，避免「第一筆留言就帶子狀態」時被誤判為非法狀態轉換。
		eventStatusService.ensureProcessingOnComment(target, request.getAuthor());

		if (commentStatus != null) {
			applyEventProcessingDetail(target, commentStatus);
		}

		if (commentStatus == CommentStatus.RESOLVED) {
			eventStatusService.resolve(target.getEventId(), request.getAuthor());
		}
		
		if (commentStatus == CommentStatus.CLOSED) {
			eventStatusService.close(target.getEventId(), request.getAuthor());
		}

		CommentDTO dto = new CommentDTO(saved);
		realtimeEventService.publish(EventType.COMMENT_CREATED, "EVENT", target.getEventId(), dto);
		return dto;
	}

	@Transactional(readOnly = true)
	public List<CommentDTO> getDistinctCommentsByEventIds(List<String> eventIds, String order) {
		if (eventIds == null || eventIds.isEmpty())
			return List.of();

		Sort sort = "desc".equalsIgnoreCase(order) ? Sort.by("commentTimestamp").descending()
				: Sort.by("commentTimestamp").ascending();
		return commentRepository.findAllDistinctByEventIds(eventIds, sort).stream().map(CommentDTO::new).toList();
	}

	@Transactional
	public void deleteCommentById(Long commentId) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new NoSuchElementException("Comment not found"));

		for (Case c : List.copyOf(comment.getCases())) {
			c.removeComment(comment);
		}
		for (Event e : List.copyOf(comment.getEvents())) {
			e.removeComment(comment);
		}

		commentRepository.delete(comment);
	}

	@Transactional(readOnly = true)
	public List<CommentDTO> getCaseComments(Long caseId, String order) {
		if (!caseRepository.existsById(caseId)) {
			throw new NoSuchElementException("Case not found: " + caseId);
		}
		Sort sort = "desc".equalsIgnoreCase(order) ? Sort.by("commentTimestamp").descending()
				: Sort.by("commentTimestamp").ascending();

		List<Comment> list = commentRepository.findAllByCaseId_ManyToMany(caseId, sort);

		return list.stream().map(CommentDTO::new).toList();
	}

	/** 供時間軸 UI 使用：只回傳帶有 processingDetailStatus 的留言，依時間排序。 */
//	@Transactional(readOnly = true)
//	public List<CommentDTO> getEventProcessingDetailHistory(String eventId) {
//		if (!eventRepository.existsByEventId(eventId)) {
//			throw new NoSuchElementException("Event not found: " + eventId);
//		}
//		return commentRepository.findProcessingDetailHistoryByEventId(eventId).stream().map(CommentDTO::new).toList();
//	}
//
//	@Transactional(readOnly = true)
//	public List<CommentDTO> getCaseProcessingDetailHistory(Long caseId) {
//		if (!caseRepository.existsById(caseId)) {
//			throw new NoSuchElementException("Case not found: " + caseId);
//		}
//		return commentRepository.findProcessingDetailHistoryByCaseId(caseId).stream().map(CommentDTO::new).toList();
//	}

	// ---- helpers ----

	private String resolveContent(String rawContent, CommentStatus commentStatus) {
		if (rawContent != null && !rawContent.isBlank()) {
			return rawContent.trim();
		}
		if (commentStatus != null) {
			return DEFAULT_COMMENT_TEXT.get(commentStatus);
		}
		throw new IllegalArgumentException("content 不可為空");
	}

	/**
	 * 呼叫此方法前，呼叫端必須先執行過 eventStatusService.ensureProcessingOnComment(...)， 確保
	 * UNREAD/ACKNOWLEDGED 都已經晉升過，且 CLASSIFIED 已在 createEventComment
	 * 一開始就被擋下。走到這裡若還不是 PROCESSING，代表事件已經是 RESOLVED/CLOSED
	 * 這種終態，這種情況下不允許再切換處理中細節子狀態，是合理的例外。
	 */
	private void applyEventProcessingDetail(Event event, CommentStatus detailStatus) {
		if (event.getEventStatus() != EventStatus.PROCESSING) {
			return;
		}
		event.setProcessingDetailStatus(detailStatus);
		EventDTO dto = new EventDTO(event);
		realtimeEventService.publish(EventType.EVENT_PROCESSING_DETAIL_UPDATED, "EVENT", event.getEventId(), dto);
	}

	private void applyCaseProcessingDetail(Case caze, CommentStatus commentStatus) {
		if (caze.getStatus() != CaseStatus.PROCESSING) {
			return;
		}
		caze.setProcessingDetailStatus(commentStatus);
		CaseDTO dto = new CaseDTO(caze);
		realtimeEventService.publish(EventType.CASE_PROCESSING_DETAIL_UPDATED, "CASE", caze.getId(), dto);
	}
}
