package com.project.uhdbackend.entity;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.project.uhdbackend.enums.EventStatus;
import com.project.uhdbackend.utils.CommentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MUHD_EVENT")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "EVENT_ID", nullable = false, unique = true, length = 50)
	private String eventId;

	// ---- Mail 層級 ----
	@Column(name = "SUBJECT")
	private String subject;

	@Column(name = "SENDER")
	private String sender;

	@Column(name = "RECEIVER")
	private String receiver;

	@Column(name = "RECEIVED_AT")
	private OffsetDateTime receivedAt;

	// ---- 告警內容層級 ----
	@Column(name = "ALERT_CODE", nullable = false)
	private String alertCode;

	@Column(name = "MODULE_CODE", nullable = false)
	private String moduleCode;

	@Column(name = "ENVIRONMENT", nullable = false)
	private String environment;

	@Column(name = "SOURCE_HOST")
	private String sourceHost;

	@Column(name = "SOURCE_IP")
	private String sourceIp;

	@Column(name = "SEVERITY")
	private String severity;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "MESSAGE_CONTENT")
	private String messageContent;

	@Column(name = "OCCURRED_AT", nullable = false)
	private OffsetDateTime occurredAt;

	@Lob
	@Column(name = "DETAILS")
	private String details;

	@ManyToMany(mappedBy = "events")
	@JsonIgnore
	private Set<Case> cases = new HashSet<>();

	// ---- 除錯 / 稽核 ----
	@Column(name = "RAW_JSON_PAYLOAD")
	private String rawJsonPayload;

	@Column(name = "RAW_EML_CONTENT")
	private String rawEmlContent;

	@Column(name = "VALIDATION_ERROR_MESSAGE")
	private String validationErrorMessage;

	// ---- 處理資訊 ----
	@Column(name = "ASSIGNED_TO")
	private String assignedTo;

	@Column(name = "ASSIGNED_DEPT")
	private String assignedDept;

	@Column(name = "ACKNOWLEDGED_AT")
	private OffsetDateTime acknowledgedAt;

	@Column(name = "RESOLVED_AT")
	private OffsetDateTime resolvedAt;

	@Column(name = "CLOSED_AT")
	private OffsetDateTime closedAt;

	@Column(name = "READ_BY", length = 100)
	private String readBy;

	@Column(name = "RESOLVED_BY", length = 100)
	private String resolvedBy;

	@Column(name = "CLOSED_BY", length = 100)
	private String closedBy;

	@Enumerated(EnumType.STRING)
	@Column(name = "EVENT_STATUS", nullable = false)
	private EventStatus eventStatus = EventStatus.UNREAD;

	@Column(name = "CREATED_AT", nullable = false)
	private OffsetDateTime createdAt;

	@Column(name = "UPDATED_AT", nullable = false)
	private OffsetDateTime updatedAt;

	@Column(name = "HAS_ATTACHMENT", nullable = false, length = 1)
	private String hasAttachment = "N";

	@ManyToMany
	@JoinTable(name = "MUHD_EVENT_COMMENT", joinColumns = @JoinColumn(name = "EVENT_PK"), inverseJoinColumns = @JoinColumn(name = "COMMENT_ID"))
	@JsonIgnore
	private Set<Comment> comments = new HashSet<>();

	@Enumerated(EnumType.STRING)
	@Column(name = "PROCESSING_DETAIL_STATUS", length = 30)
	private CommentStatus processingDetailStatus;

	@PrePersist
	public void onCreate() {
		OffsetDateTime now = OffsetDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;
	}

	@PreUpdate
	public void onUpdate() {
		this.updatedAt = OffsetDateTime.now();
	}

	// ---- getters / setters ----

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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public OffsetDateTime getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(OffsetDateTime receivedAt) {
		this.receivedAt = receivedAt;
	}

	public String getAlertCode() {
		return alertCode;
	}

	public void setAlertCode(String alertCode) {
		this.alertCode = alertCode;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getSourceHost() {
		return sourceHost;
	}

	public void setSourceHost(String sourceHost) {
		this.sourceHost = sourceHost;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
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

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public OffsetDateTime getOccurredAt() {
		return occurredAt;
	}

	public void setOccurredAt(OffsetDateTime occurredAt) {
		this.occurredAt = occurredAt;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getRawJsonPayload() {
		return rawJsonPayload;
	}

	public void setRawJsonPayload(String rawJsonPayload) {
		this.rawJsonPayload = rawJsonPayload;
	}

	public String getRawEmlContent() {
		return rawEmlContent;
	}

	public void setRawEmlContent(String rawEmlContent) {
		this.rawEmlContent = rawEmlContent;
	}

	public String getValidationErrorMessage() {
		return validationErrorMessage;
	}

	public void setValidationErrorMessage(String validationErrorMessage) {
		this.validationErrorMessage = validationErrorMessage;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getAssignedDept() {
		return assignedDept;
	}

	public void setAssignedDept(String assignedDept) {
		this.assignedDept = assignedDept;
	}

	public OffsetDateTime getAcknowledgedAt() {
		return acknowledgedAt;
	}

	public void setAcknowledgedAt(OffsetDateTime acknowledgedAt) {
		this.acknowledgedAt = acknowledgedAt;
	}

	public OffsetDateTime getResolvedAt() {
		return resolvedAt;
	}

	public void setResolvedAt(OffsetDateTime resolvedAt) {
		this.resolvedAt = resolvedAt;
	}

	public OffsetDateTime getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(OffsetDateTime closedAt) {
		this.closedAt = closedAt;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}

	public String getHasAttachment() {
		return hasAttachment;
	}

	public void setHasAttachment(String hasAttachment) {
		this.hasAttachment = hasAttachment;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public void addComment(Comment c) {
		this.comments.add(c);
		c.getEvents().add(this);
	}

	public void removeComment(Comment c) {
		this.comments.remove(c);
		c.getEvents().remove(this);
	}

	public Set<Case> getCases() {
		return cases;
	}

	public void setCases(Set<Case> cases) {
		this.cases = cases;
	}

	public String getReadBy() {
		return readBy;
	}

	public void setReadBy(String readBy) {
		this.readBy = readBy;
	}

	public String getResolvedBy() {
		return resolvedBy;
	}

	public void setResolvedBy(String resolvedBy) {
		this.resolvedBy = resolvedBy;
	}

	public String getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(String closedBy) {
		this.closedBy = closedBy;
	}

	public CommentStatus getProcessingDetailStatus() {
		return processingDetailStatus;
	}

	public void setProcessingDetailStatus(CommentStatus processingDetailStatus) {
		this.processingDetailStatus = processingDetailStatus;
	}
}