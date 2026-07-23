package com.project.uhdbackend.dto;

import java.util.List;

import com.project.uhdbackend.enums.MessageStatus;

public class MessageSearchRequest {
	private List<MessageStatus> statusArray ;
	private String subject;
	private String mainCategory;
	private String sender;
	private String content;
	private String day;

	public MessageSearchRequest(List<MessageStatus> statusArray, String subject, String mainCategory, String sender,
			String content, String day) {
		this.statusArray = statusArray;
		this.subject = subject;
		this.mainCategory = mainCategory;
		this.sender = sender;
		this.content = content;
		this.day = day;
	}

	public List<MessageStatus> getStatusArray() {
		return statusArray;
	}

	public void setStatusArray(List<MessageStatus> statusArray) {
		this.statusArray = statusArray;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMainCategory() {
		return mainCategory;
	}

	public void setMainCategory(String mainCategory) {
		this.mainCategory = mainCategory;
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
