package com.project.uhd.dto;

import java.time.OffsetDateTime;

import com.project.uhd.entity.CommentDraft;

public class CommentDraftDTO {

	private String content;
	private OffsetDateTime updatedAt;

	public CommentDraftDTO(CommentDraft draft) {
		this.content = draft.getDraftContent();
		this.updatedAt = draft.getUpdatedAt();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(OffsetDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
