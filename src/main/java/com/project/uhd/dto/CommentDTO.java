package com.project.uhd.dto;

import java.time.LocalDateTime;

import com.project.uhd.entity.Comment;
import com.project.uhd.util.CommentStatus;

public class CommentDTO {
	private Long commentId;
	private String content;
	private String author;
	private LocalDateTime timestamp;

	/** 非 null 時代表這則留言對應一次處理中細節子狀態的切換；前端可據此關閉編輯功能。 */
	private CommentStatus status;

	public CommentDTO(Comment comment) {
		this.commentId = comment.getCommentId();
		this.content = comment.getCommentContent();
		this.author = comment.getCommentAuthor();
		this.timestamp = comment.getCommentTimestamp();
		this.status = comment.getStatus();
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

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

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public CommentStatus getStatus() {
		return status;
	}

	public void setStatus(CommentStatus status) {
		this.status = status;
	}
}
