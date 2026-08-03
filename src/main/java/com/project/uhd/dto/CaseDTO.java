package com.project.uhd.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import com.project.uhd.entity.Case;
import com.project.uhd.util.CommentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

public class CaseDTO {
	private Long id;
	private String name;
	private String description;
	private String logic;
	private String conditions;
	private String creator;
	private String status;
	private Boolean ruleEnabled;

	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime createTime;

	private String resolvedBy;
	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime resolvedAt;
	private String closedBy;
	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime closedAt;

//	private Set<MessageDTO> messages;
	private Set<CommentDTO> comments;
	private Set<EventDTO> events;
	private CommentStatus processingDetailStatus;

	public CaseDTO(Case input) {
		this.id = input.getId();
		this.name = input.getName();
		this.description = input.getDescription();
		this.logic = input.getLogic();
		this.conditions = input.getConditions();
		this.creator = input.getCreator();
		this.status = input.getStatus() != null ? input.getStatus().name() : null;
//		if (input.getMessages() != null) {
//			this.messages = input.getMessages().stream().map(MessageDTO::new).collect(Collectors.toSet());
//		}

		if (input.getComments() != null) {
			this.comments = input.getComments().stream().map(CommentDTO::new).collect(Collectors.toSet());
		}

		if (input.getEvents() != null) {
			this.events = input.getEvents().stream().map(EventDTO::new).collect(Collectors.toSet());
		}
		this.createTime = input.getCreatedAt();
		this.ruleEnabled = input.getRuleEnabled();

		this.resolvedBy = input.getResolvedBy();
		this.resolvedAt = input.getResolvedAt();
		this.closedBy = input.getClosedBy();
		this.closedAt = input.getClosedAt();
		this.processingDetailStatus = input.getProcessingDetailStatus();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

//	public Set<MessageDTO> getMessages() {
//		return messages;
//	}
//
//	public void setMessages(Set<MessageDTO> messages) {
//		this.messages = messages;
//	}

	public Set<CommentDTO> getComments() {
		return comments;
	}

	public void setComments(Set<CommentDTO> comments) {
		this.comments = comments;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public Boolean getRuleEnabled() {
		return ruleEnabled;
	}

	public void setRuleEnabled(Boolean ruleEnabled) {
		this.ruleEnabled = ruleEnabled;
	}

	public Set<EventDTO> getEvents() {
		return events;
	}

	public void setEvents(Set<EventDTO> events) {
		this.events = events;
	}

	public String getResolvedBy() {
		return resolvedBy;
	}

	public void setResolvedBy(String resolvedBy) {
		this.resolvedBy = resolvedBy;
	}

	public LocalDateTime getResolvedAt() {
		return resolvedAt;
	}

	public void setResolvedAt(LocalDateTime resolvedAt) {
		this.resolvedAt = resolvedAt;
	}

	public String getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(String closedBy) {
		this.closedBy = closedBy;
	}

	public LocalDateTime getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(LocalDateTime closedAt) {
		this.closedAt = closedAt;
	}

	public CommentStatus getProcessingDetailStatus() {
		return processingDetailStatus;
	}

	public void setProcessingDetailStatus(CommentStatus processingDetailStatus) {
		this.processingDetailStatus = processingDetailStatus;
	}
}
