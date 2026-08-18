package com.project.uhd.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhd.dto.StatusLogDTO;
import com.project.uhd.entity.StatusLog;
import com.project.uhd.enums.ChangeSource;
import com.project.uhd.enums.StatusLogTargetType;
import com.project.uhd.repository.CommentRepository;
import com.project.uhd.repository.StatusLogRepository;

@Service
public class StatusLogService {

	private final StatusLogRepository statusLogRepository;
	private final CommentRepository commentRepository;

	public StatusLogService(StatusLogRepository statusLogRepository, CommentRepository commentRepository) {
		this.statusLogRepository = statusLogRepository;
		this.commentRepository = commentRepository;
	}

	@Transactional
	public void log(StatusLogTargetType targetType, Long entityId, String status, String changedBy,
			String changedById, ChangeSource source, Long relatedCommentId) {
		StatusLog log = new StatusLog();
		log.setEntityType(targetType);
		log.setEntityId(entityId);
		log.setStatus(status);
		log.setChangedBy(changedBy);
		log.setChangedById(changedById);
		log.setSource(source);
		log.setRelatedCommentId(relatedCommentId);
		statusLogRepository.save(log);
	}

	@Transactional(readOnly = true)
	public List<StatusLogDTO> getHistory(StatusLogTargetType targetType, Long entityId, String order) {
		Sort sort = "desc".equalsIgnoreCase(order) ? Sort.by("changedAt").descending()
				: Sort.by("changedAt").ascending();
		return statusLogRepository.findByEntityTypeAndEntityId(targetType, entityId, sort).stream()
				.map(StatusLogDTO::new).map(this::attachComment).toList();
	}
	
	private StatusLogDTO attachComment(StatusLogDTO dto) {
		if(dto.getRelatedCommentId()==null) {
			return dto;
		}
		dto.setCommentContent(commentRepository.findById(dto.getRelatedCommentId()).get().getCommentContent());
		return dto;
	}
}
