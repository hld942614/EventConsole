package com.project.uhd.entity;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "MUHD_COMMENT_DRAFT")
public class CommentDraft {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DRAFT_ID")
	private Long id;

	@Column(name = "OWNER_ID", nullable = false, length = 100)
	private String ownerId;

	@Column(name = "CASE_ID")
	private Long caseId;

	@Column(name = "EVENT_PK")
	private Long eventPk;

	@Lob
	@Column(name = "DRAFT_CONTENT", nullable = false)
	private String draftContent;

	@Column(name = "UPDATED_AT", insertable = false, updatable = false)
	private OffsetDateTime updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	public Long getEventPk() {
		return eventPk;
	}

	public void setEventPk(Long eventPk) {
		this.eventPk = eventPk;
	}

	public String getDraftContent() {
		return draftContent;
	}

	public void setDraftContent(String draftContent) {
		this.draftContent = draftContent;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}
}	
