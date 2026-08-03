// dto/CaseEventDeleteRequest.java
package com.project.uhd.dto;

import java.util.List;

public class CaseEventDeleteRequest {
	private Long caseId;
	private List<String> eventIds;

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
}