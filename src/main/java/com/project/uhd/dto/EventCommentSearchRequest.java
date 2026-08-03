package com.project.uhd.dto;

import java.util.List;

public class EventCommentSearchRequest {
	private List<String> eventIds;
	private String order = "asc";

	public List<String> getEventIds() {
		return eventIds;
	}

	public void setEventIds(List<String> eventIds) {
		this.eventIds = eventIds;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}