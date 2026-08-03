package com.project.uhd.dto;

import com.project.uhd.util.CommentStatus;

public class EventCommentCreateRequest {
	private String content;
	private String author;
	private String eventId;

	/** 若使用者是透過「切換處理中細節子狀態」觸發留言，帶入對應值；一般留言則為 null。 */
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

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public CommentStatus getStatus() {
		return status;
	}

	public void setStatus(CommentStatus status) {
		this.status = status;
	}
}