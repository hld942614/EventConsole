package com.project.uhd.dto;

import java.util.List;

import com.project.uhd.enums.EventStatus;
import com.project.uhd.util.CommentStatus;

public class EventSearchRequest {
	private List<EventStatus> statusArray;
	private String subject;
	private String moduleCode;
	private String sender;
	private String content;
	private String startDay; // 篩選區間起始日（含），基準為 OCCURRED_AT
	private String endDay; // 篩選區間結束日（含），基準為 OCCURRED_AT
	private CommentStatus processingDetailStatus;

	public EventSearchRequest() {
	}

	public EventSearchRequest(List<EventStatus> statusArray, String subject, String moduleCode, String sender,
			String content, String startDay, String endDay, CommentStatus processingDetailStatus) {
		this.statusArray = statusArray;
		this.subject = subject;
		this.moduleCode = moduleCode;
		this.sender = sender;
		this.content = content;
		this.startDay = startDay;
		this.endDay = endDay;
		this.processingDetailStatus = processingDetailStatus;
	}

	public List<EventStatus> getStatusArray() {
		return statusArray;
	}

	public void setStatusArray(List<EventStatus> statusArray) {
		this.statusArray = statusArray;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

	public CommentStatus getProcessingDetailStatus() {
		return processingDetailStatus;
	}

	public void setProcessingDetailStatus(CommentStatus processingDetailStatus) {
		this.processingDetailStatus = processingDetailStatus;
	}
}