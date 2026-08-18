package com.project.uhd.dto;

import com.project.uhd.entity.StatusLog;
import com.project.uhd.util.TimestampFormatUtil;

public class StatusLogDTO {

	private Long id;
	private String entityType;
	private Long entityId;
	private String status;
	private String changedBy;
	private String changedById;
	private String source;
	private Long relatedCommentId;
	private String changedAt;
	private String commentContent;

	public StatusLogDTO(StatusLog log) {
		this.id = log.getId();
		this.entityType = log.getEntityType() != null ? log.getEntityType().name() : null;
		this.entityId = log.getEntityId();
		this.status = log.getStatus();
		this.changedBy = log.getChangedBy();
		this.changedById = log.getChangedById();
		this.source = log.getSource() != null ? log.getSource().name() : null;
		this.relatedCommentId = log.getRelatedCommentId();
		this.changedAt = TimestampFormatUtil.format(log.getChangedAt());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	public String getChangedById() {
		return changedById;
	}

	public void setChangedById(String changedById) {
		this.changedById = changedById;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getRelatedCommentId() {
		return relatedCommentId;
	}

	public void setRelatedCommentId(Long relatedCommentId) {
		this.relatedCommentId = relatedCommentId;
	}

	public String getChangedAt() {
		return changedAt;
	}

	public void setChangedAt(String changedAt) {
		this.changedAt = changedAt;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
}
