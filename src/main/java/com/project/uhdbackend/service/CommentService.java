package com.project.uhdbackend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhdbackend.dto.CaseCommentCreateRequest;
import com.project.uhdbackend.dto.CommentDTO;
import com.project.uhdbackend.dto.EventCommentCreateRequest;
import com.project.uhdbackend.entity.Case;
import com.project.uhdbackend.entity.Comment;
import com.project.uhdbackend.entity.Event;
import com.project.uhdbackend.realtime.event.EventType;
import com.project.uhdbackend.realtime.service.RealtimeEventService;
import com.project.uhdbackend.repository.CaseRepository;
import com.project.uhdbackend.repository.CommentRepository;
import com.project.uhdbackend.repository.EventRepository;

@Service
public class CommentService {

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
		Case target = caseRepository.findById(request.getCaseId()).get();

		Comment comment = new Comment();
		comment.setCommentContent(request.getContent().trim());
		comment.setCommentAuthor(request.getAuthor().trim());
		comment.setCommentTimestamp(LocalDateTime.now());

		target.addComment(comment);

		Comment saved = commentRepository.save(comment);

		caseService.ensureProcessingOnComment(target);

		CommentDTO dto = new CommentDTO(saved);
		realtimeEventService.publish(EventType.COMMENT_CREATED, "CASE", target.getId(), dto);
		return new CommentDTO(saved);
	}

//	@Transactional
//	public CommentDTO createMsgComment(MessageCommentCreateRequest request) {
//		Message message = messageRepository.findById(request.getMessageId()).get();
//
//		Comment comment = new Comment();
//		comment.setCommentContent(request.getContent().trim());
//		comment.setCommentAuthor(request.getAuthor().trim());
//		comment.setCommentTimestamp(LocalDateTime.now());
//
//		message.addComment(comment);
//		message.setStatus(MessageStatus.PROCESSING);
//
//		Comment saved = commentRepository.save(comment);
//
//		CommentDTO dto = new CommentDTO(saved);
//		realtimeEventService.publish(EventType.COMMENT_CREATED, "MESSAGE", message.getMessageId(), dto);
//		return new CommentDTO(saved);
//	}

	@Transactional
	public CommentDTO createEventComment(EventCommentCreateRequest request) {
		Event target = eventRepository.findByEventId(request.getEventId())
				.orElseThrow(() -> new NoSuchElementException("Event not found: " + request.getEventId()));

		Comment comment = new Comment();
		comment.setCommentContent(request.getContent().trim());
		comment.setCommentAuthor(request.getAuthor().trim());
		comment.setCommentTimestamp(LocalDateTime.now());

		target.addComment(comment);
		Comment saved = commentRepository.save(comment);

		eventStatusService.ensureProcessingOnComment(target, request.getAuthor());

		CommentDTO dto = new CommentDTO(saved);
		realtimeEventService.publish(EventType.COMMENT_CREATED, "EVENT", target.getEventId(), dto);
		return dto;
	}

//	@Transactional
//	public void deleteCommentById(Long commentId) {
//	    Comment comment = commentRepository.findById(commentId)
//	            .orElseThrow(() -> new NoSuchElementException("Comment not found"));
//
//	    for (Message m : List.copyOf(comment.getMessages())) {
//	        m.removeComment(comment);   // ← 從擁有端移除，聯結表會刪除
//	    }
//
//	    commentRepository.delete(comment);
//	}

//	@Transactional(readOnly = true)
//	public List<CommentDTO> getDistinctCommentsByMessageIds(List<Long> messageIds, String order) {
//		if (messageIds == null || messageIds.isEmpty())
//			return List.of();
//
//		Sort sort = "desc".equalsIgnoreCase(order) ? Sort.by("commentTimestamp").descending()
//				: Sort.by("commentTimestamp").ascending();
//		return commentRepository.findAllDistinctByMessageIds(messageIds, sort).stream().map(CommentDTO::new).toList();
//	}

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
		// 1) 清 message_comment 聯結
//		for (Message m : List.copyOf(comment.getMessages())) {
//			m.removeComment(comment);
//		}

		// 2) 清 case_comment 聯結
		for (Case c : List.copyOf(comment.getCases())) {
			c.removeComment(comment);
		}
		// 3) 清 event_comment 聯結
		for (Event e : List.copyOf(comment.getEvents())) {
			e.removeComment(comment);
		}

		// 4) 刪 comment 本體
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
}
