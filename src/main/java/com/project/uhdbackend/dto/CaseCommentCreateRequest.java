package com.project.uhdbackend.dto;

import com.project.uhdbackend.utils.CommentStatus;

public class CaseCommentCreateRequest {
	private String content;
	private String author;
	private Long caseId;

	private CommentStatus status;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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