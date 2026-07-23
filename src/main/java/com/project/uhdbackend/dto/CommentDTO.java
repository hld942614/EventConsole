package com.project.uhdbackend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.project.uhdbackend.entity.Comment;

public class CommentDTO {
	private Long commentId;
	private String content;
	private String author;
	private LocalDateTime timestamp;
	private List<Long> messageIds;

	public CommentDTO(Comment comment) {
		this.commentId = comment.getCommentId();
		this.content = comment.getCommentContent();
		this.author = comment.getCommentAuthor();
		this.timestamp = comment.getCommentTimestamp();
//		this.messageIds = comment.getMessages().stream().map(m -> m.getMessageId()).collect(Collectors.toList());
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

	public List<Long> getMessageIds() {
		return messageIds;
	}

	public void setMessageIds(List<Long> messageIds) {
		this.messageIds = messageIds;
	}

}
