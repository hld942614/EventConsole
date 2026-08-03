package com.project.uhd.dto;

public class CaseStatusUpdateRequest {
	private Long caseId;
	private String status;

	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
