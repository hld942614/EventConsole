package com.project.uhdbackend.dto;

import java.util.List;

import com.project.uhdbackend.enums.EventStatus;

public class EventSearchRequest {
	private List<EventStatus> statusArray;
	private String subject;
	private String moduleCode; // 對應 Message 的 mainCategory
	private String sender;
	private String content; // 對應 MESSAGE_CONTENT
	private String day; // 對應 Message 的 day，篩選基準改成 OCCURRED_AT

	public EventSearchRequest() {
	}

	public EventSearchRequest(List<EventStatus> statusArray, String subject, String moduleCode, String sender,
			String content, String day) {
		this.statusArray = statusArray;
		this.subject = subject;
		this.moduleCode = moduleCode;
		this.sender = sender;
		this.content = content;
		this.day = day;
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

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
}