package com.project.uhdbackend.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import com.project.uhdbackend.entity.Case;
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

//	private Set<MessageDTO> messages;
	private Set<CommentDTO> comments;
	private Set<EventDTO> events;

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
}
