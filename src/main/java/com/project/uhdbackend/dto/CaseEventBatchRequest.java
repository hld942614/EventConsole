package com.project.uhdbackend.dto;

import java.util.List;

public class CaseEventBatchRequest {
	private Long caseId;
	private List<String> eventIds;
	private String assignedTo;

	public CaseEventBatchRequest() {
	}

	public CaseEventBatchRequest(Long caseId, List<String> eventIds) {
		this.caseId = caseId;
		this.eventIds = eventIds;
	}

	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	public List<String> getEventIds() {
		return eventIds;
	}

	public void setEventIds(List<String> eventIds) {
		this.eventIds = eventIds;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
}