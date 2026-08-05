package com.project.uhd.dto;

import com.project.uhd.util.CommentStatus;

public class CaseCommentCreateRequest {
	private String content;
	private Long caseId;
	private CommentStatus status;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	public CommentStatus getStatus() {
		return status;
	}

	public void setStatus(CommentStatus status) {
		this.status = status;
	}
}