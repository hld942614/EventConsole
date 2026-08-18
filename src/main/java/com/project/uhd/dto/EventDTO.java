package com.project.uhd.dto;

import java.util.List;

import com.project.uhd.entity.Event;
import com.project.uhd.entity.UploadedFile;
import com.project.uhd.enums.EventStatus;
import com.project.uhd.util.TimestampFormatUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDTO {

	private Long id;
	private String eventId;
	private EventStatus status;
	private String moduleCode;
	private Boolean hasAttachment;

	private String alertCode;
	private SourceInfo source;
	private String severity;
	private String title;
	private String message;
	private String occurredAt;
	@JsonRawValue
	private String details;
	private String assignedTo;
	private String acknowledgedAt;
	private boolean hasCase;
	private List<Long> caseIds;
	@JsonRawValue
	private String rawJsonPayload;

	private String validationErrorMessage; // 只有 INVALID 狀態才會有值
	
	private List<UploadedFile> sopFileList;

	public EventDTO() {
	}

	public EventDTO(Event event) {
		this.id = event.getId();
		this.eventId = event.getEventId();
		this.status = event.getStatus();
		this.moduleCode = event.getModuleCode();
		this.hasAttachment = "Y".equalsIgnoreCase(event.getHasAttachment());
		this.alertCode = event.getAlertCode();
		this.source = buildSource(event.getEnvironment(), event.getSourceHost(), event.getSourceIp());
		this.severity = event.getSeverity();
		this.title = event.getTitle();
		this.message = event.getMessageContent();
		this.occurredAt = TimestampFormatUtil.format(event.getOccurredAt());
		this.details = event.getDetails();
		this.assignedTo = event.getAssignedTo();
		this.rawJsonPayload = event.getRawJsonPayload();

		this.validationErrorMessage = event.getValidationErrorMessage();
	}

	public static SourceInfo buildSource(String environment, String host, String ip) {
		SourceInfo s = new SourceInfo(environment, host, ip);
		return s.isEmpty() ? null : s;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public EventStatus getStatus() {
		return status;
	}

	public void setStatus(EventStatus status) {
		this.status = status;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public Boolean getHasAttachment() {
		return hasAttachment;
	}

	public void setHasAttachment(Boolean hasAttachment) {
		this.hasAttachment = hasAttachment;
	}

	public String getAlertCode() {
		return alertCode;
	}

	public void setAlertCode(String alertCode) {
		this.alertCode = alertCode;
	}

	public SourceInfo getSource() {
		return source;
	}

	public void setSource(SourceInfo source) {
		this.source = source;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOccurredAt() {
		return occurredAt;
	}

	public void setOccurredAt(String occurredAt) {
		this.occurredAt = occurredAt;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getAcknowledgedAt() {
		return acknowledgedAt;
	}

	public void setAcknowledgedAt(String acknowledgedAt) {
		this.acknowledgedAt = acknowledgedAt;
	}

	public boolean isHasCase() {
		return hasCase;
	}

	public void setHasCase(boolean hasCase) {
		this.hasCase = hasCase;
	}

	public List<Long> getCaseIds() {
		return caseIds;
	}

	public void setCaseIds(List<Long> caseIds) {
		this.caseIds = caseIds;
	}

	public String getRawJsonPayload() {
		return rawJsonPayload;
	}

	public void setRawJsonPayload(String rawJsonPayload) {
		this.rawJsonPayload = rawJsonPayload;
	}

	public String getValidationErrorMessage() {
		return validationErrorMessage;
	}

	public void setValidationErrorMessage(String validationErrorMessage) {
		this.validationErrorMessage = validationErrorMessage;
	}
	
	public List<UploadedFile> getSopFileList() {
		return sopFileList;
	}

	public void setSopFileList(List<UploadedFile> sopFileList) {
		this.sopFileList = sopFileList;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class SourceInfo {
		private String environment;
		private String host;
		private String ip;

		public SourceInfo() {
		}

		public SourceInfo(String environment, String host, String ip) {
			this.environment = environment;
			this.host = host;
			this.ip = ip;
		}

		boolean isEmpty() {
			return environment == null && host == null && ip == null;
		}

		public String getEnvironment() {
			return environment;
		}

		public void setEnvironment(String environment) {
			this.environment = environment;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}
	}
}