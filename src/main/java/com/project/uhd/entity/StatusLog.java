package com.project.uhd.entity;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.project.uhd.enums.ChangeSource;
import com.project.uhd.enums.StatusLogTargetType;

@Entity
@Table(name = "MUHD_STATUS_LOG")
public class StatusLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "ENTITY_TYPE", nullable = false, length = 10)
	private StatusLogTargetType entityType;

	@Column(name = "ENTITY_ID", nullable = false)
	private Long entityId;

	@Column(name = "STATUS", nullable = false, length = 30)
	private String status;

	@Column(name = "CHANGED_BY", length = 100)
	private String changedBy;

	@Column(name = "CHANGED_BY_ID", length = 100)
	private String changedById;

	@Enumerated(EnumType.STRING)
	@Column(name = "SOURCE", nullable = false, length = 20)
	private ChangeSource source;

	@Column(name = "RELATED_COMMENT_ID")
	private Long relatedCommentId;

	@Column(name = "CHANGED_AT", insertable = false, updatable = false)
	private OffsetDateTime changedAt;

	public Long getId() {
		return id;
	}

	public StatusLogTargetType getEntityType() {
		return entityType;
	}

	public void setEntityType(StatusLogTargetType entityType) {
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

	public ChangeSource getSource() {
		return source;
	}

	public void setSource(ChangeSource source) {
		this.source = source;
	}

	public Long getRelatedCommentId() {
		return relatedCommentId;
	}

	public void setRelatedCommentId(Long relatedCommentId) {
		this.relatedCommentId = relatedCommentId;
	}

	public OffsetDateTime getChangedAt() {
		return changedAt;
	}
}
