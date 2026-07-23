package com.project.uhdbackend.dto;

import java.util.List;

public class CommentSearchRequest {

	private List<Long> messageIds;
	private String order = "asc";

	public List<Long> getMessageIds() {
		return messageIds;
	}

	public void setMessageIds(List<Long> messageIds) {
		this.messageIds = messageIds;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
