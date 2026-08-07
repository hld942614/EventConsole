package com.project.uhd.service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhd.authentication.CustomUserDetails;
import com.project.uhd.dto.CaseCommentCreateRequest;
import com.project.uhd.dto.CaseDTO;
import com.project.uhd.dto.CommentDTO;
import com.project.uhd.dto.EventCommentCreateRequest;
import com.project.uhd.dto.EventDTO;
import com.project.uhd.entity.Case;
import com.project.uhd.entity.Comment;
import com.project.uhd.entity.Event;
import com.project.uhd.enums.CaseStatus;
import com.project.uhd.enums.EventStatus;
import com.project.uhd.exception.ForbiddenOperationException;
import com.project.uhd.realtime.event.EventType;
import com.project.uhd.realtime.service.RealtimeEventService;
import com.project.uhd.repository.CaseRepository;
import com.project.uhd.repository.CommentRepository;
import com.project.uhd.repository.EventRepository;
import com.project.uhd.util.CommentStatus;

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
	public CommentDTO createCaseComment(CaseCommentCreateRequest request, CustomUserDetails currentUser) {
		Case target = caseRepository.findById(request.getCaseId())
				.orElseThrow(() -> new NoSuchElementException("Case not found: " + request.getCaseId()));

		CommentStatus commentStatus = request.getStatus();

		String content = resolveContent(request.getContent(), commentStatus);

		Comment comment = new Comment();
		comment.setCommentContent(content);
		comment.setCommentAuthor(currentUser.getChineseName());
		comment.setCommentAuthorId(currentUser.getId());
		comment.setCommentTimestamp(LocalDateTime.now());
		comment.setStatus(commentStatus);

		target.addComment(comment);

		Comment saved = commentRepository.save(comment);

		// 依 commentStatus 決定目標狀態：只有 RESOLVED/CLOSED 會跳過 PROCESSING 直接轉，
	    // 其餘（含 null）交給 applyCommentStatus 內建的 ensureProcessingOnComment 邏輯處理。
	    CaseStatus targetStatus = resolveTargetCaseStatus(commentStatus);
	    caseService.applyCommentStatus(target, targetStatus, currentUser);

	    if (commentStatus != null) {
	        applyCaseProcessingDetail(target, commentStatus);
	    }

		CommentDTO dto = new CommentDTO(saved);
		realtimeEventService.publish(EventType.COMMENT_CREATED, "CASE", target.getId(), dto);
		return dto;
	}

	@Transactional
	public CommentDTO createEventComment(EventCommentCreateRequest request, CustomUserDetails currentUser) {
		Event target = eventRepository.findByEventId(request.getEventId())
				.orElseThrow(() -> new NoSuchElementException("Event not found: " + request.getEventId()));

		if (target.getEventStatus() == EventStatus.CLASSIFIED) {
			throw new IllegalStateException("此事件已分類至 Case，請至對應 Case 頁面留言: eventId=" + request.getEventId());
		}

		CommentStatus commentStatus = request.getStatus();
		String content = resolveContent(request.getContent(), commentStatus);

		Comment comment = new Comment();
		comment.setCommentContent(content);
		comment.setCommentAuthor(currentUser.getChineseName());
		comment.setCommentAuthorId(currentUser.getId());
		comment.setCommentTimestamp(LocalDateTime.now());
		comment.setStatus(commentStatus);

		target.addComment(comment);
		Comment saved = commentRepository.save(comment);

		EventStatus targetStatus = resolveTargetEventStatus(commentStatus);
	    eventStatusService.applyCommentStatus(target, targetStatus, currentUser);

	    if (commentStatus != null) {
	        applyEventProcessingDetail(target, commentStatus);
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
		return commentRepository.findAllByEvent_EventIdIn(eventIds, sort).stream().map(CommentDTO::new).toList();
	}

	@Transactional
	public void deleteCommentById(Long commentId) {
	    Comment comment = commentRepository.findById(commentId)
	            .orElseThrow(() -> new NoSuchElementException("Comment not found"));

	    if (comment.getCaze() != null) {
	        comment.getCaze().removeComment(comment);
	    }
	    if (comment.getEvent() != null) {
	        comment.getEvent().removeComment(comment);
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

		List<Comment> list = commentRepository.findAllByCaze_Id(caseId, sort);

		return list.stream().map(CommentDTO::new).toList();
	}

	@Transactional
	public CommentDTO updateComment(Long commentId, String newContent, CustomUserDetails currentUser) {
		if (newContent == null || newContent.isBlank()) {
			throw new IllegalArgumentException("content 不可為空");
		}

		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new NoSuchElementException("Comment not found: " + commentId));

		if (comment.getCommentAuthorId() == null || !comment.getCommentAuthorId().equals(currentUser.getId())) {
			throw new ForbiddenOperationException("使用者無權限");
		}

		comment.setCommentContent(newContent.trim());
		comment.setUpdatedAt(OffsetDateTime.now());

		Comment saved = commentRepository.save(comment);
		CommentDTO dto = new CommentDTO(saved);

		if (saved.getCaze() != null) {
		    realtimeEventService.publish(EventType.COMMENT_UPDATED, "CASE", saved.getCaze().getId(), dto);
		}
		if (saved.getEvent() != null) {
		    realtimeEventService.publish(EventType.COMMENT_UPDATED, "EVENT", saved.getEvent().getEventId(), dto);
		}

		return dto;
	}

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
	
	private EventStatus resolveTargetEventStatus(CommentStatus commentStatus) {
	    if (commentStatus == CommentStatus.RESOLVED) {
	        return EventStatus.RESOLVED;
	    }
	    if (commentStatus == CommentStatus.CLOSED) {
	        return EventStatus.CLOSED;
	    }
	    return null;
	}
	
	private CaseStatus resolveTargetCaseStatus(CommentStatus commentStatus) {
	    if (commentStatus == CommentStatus.RESOLVED) {
	        return CaseStatus.RESOLVED;
	    }
	    if (commentStatus == CommentStatus.CLOSED) {
	        return CaseStatus.CLOSED;
	    }
	    return null;
	}
}
