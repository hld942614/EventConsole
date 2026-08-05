package com.project.uhd.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.json.JSONObject;

import com.project.uhd.enums.CaseStatus;
import com.project.uhd.util.CommentStatus;
import com.project.uhd.util.YesNoToBooleanConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "muhd_case")
@NoArgsConstructor
@AllArgsConstructor
public class Case {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "case_id_gen")
	@SequenceGenerator(name = "case_id_gen", sequenceName = "case_id_seq", allocationSize = 1)
	@Column(name = "CASE_ID")
	private Long id;

	@Column(name = "CASE_NAME")
	private String name;

	@Column(name = "CASE_DESCRIPTION")
	private String description;

	@Column(name = "CASE_LOGIC")
	private String logic;

	@Column(name = "CASE_CONDITIONS")
	private String conditions;

	@Column(name = "CASE_CREATOR")
	private String creator;

	@Column(name = "CASE_RULE_ENABLED", nullable = true, length = 1)
	@Convert(converter = YesNoToBooleanConverter.class)
	private Boolean ruleEnabled = false;

	@Column(name = "CASE_CREATED_AT", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "CASE_UPDATED_AT")
	private LocalDateTime updatedAt;

	@Column(name = "CASE_RESOLVED_BY", length = 100)
	private String resolvedBy;

	@Column(name = "CASE_RESOLVED_AT")
	private LocalDateTime resolvedAt;

	@Column(name = "CASE_CLOSED_BY", length = 100)
	private String closedBy;

	@Column(name = "CASE_CLOSED_AT")
	private LocalDateTime closedAt;

	@Column(name = "CASE_CREATOR_ID", length = 100)
	private String creatorId;

	@Column(name = "CASE_RESOLVED_BY_ID", length = 100)
	private String resolvedById;

	@Column(name = "CASE_CLOSED_BY_ID", length = 100)
	private String closedById;

	@OneToMany(mappedBy = "caze", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Comment> comments = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "MUHD_CASE_EVENT", joinColumns = @JoinColumn(name = "CASE_ID"), inverseJoinColumns = @JoinColumn(name = "EVENT_PK"))
	private Set<Event> events = new HashSet<>();

	@Enumerated(EnumType.STRING)
	@Column(name = "CASE_STATUS", length = 20, nullable = false)
	private CaseStatus status = CaseStatus.OPEN;

	@Enumerated(EnumType.STRING)
	@Column(name = "PROCESSING_DETAIL_STATUS", length = 30)
	private CommentStatus processingDetailStatus;

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

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Boolean getRuleEnabled() {
		return ruleEnabled;
	}

	public void setRuleEnabled(Boolean ruleEnabled) {
		this.ruleEnabled = ruleEnabled;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getResolvedById() {
		return resolvedById;
	}

	public void setResolvedById(String resolvedById) {
		this.resolvedById = resolvedById;
	}

	public String getClosedById() {
		return closedById;
	}

	public void setClosedById(String closedById) {
		this.closedById = closedById;
	}

	public void addComment(Comment c) {
		this.comments.add(c);
		c.setCaze(this);
	}

	public void removeComment(Comment c) {
		this.comments.remove(c);
		c.setCaze(null);
	}

	public CaseStatus getStatus() {
		return status;
	}

	public void setStatus(CaseStatus status) {
		this.status = status;
	}

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	public void addEvent(Event event) {
		this.events.add(event);
		event.getCases().add(this);
	}

	public void removeEvent(Event event) {
		this.events.remove(event);
		event.getCases().remove(this);
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

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		jo.put("id", id);
		jo.put("name", name);
		jo.put("description", description);
		jo.put("logic", logic);
		jo.put("conditions", conditions);
		jo.put("creator", creator);
		jo.put("ruleEnabled", ruleEnabled);
		jo.put("createdAt", createdAt);
		return jo.toString();
	}
}
