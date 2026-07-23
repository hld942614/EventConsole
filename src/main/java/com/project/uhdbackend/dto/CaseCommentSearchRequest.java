package com.project.uhdbackend.dto;

public class CaseCommentSearchRequest {
	private Long caseId;
	private String order = "asc";

	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
