package com.project.uhd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.json.JSONObject;

@Entity
@IdClass(CaseEventId.class)
@Table(name = "muhd_case_event")
public class CaseEvent {

	@Id
	@Column(name = "CASE_ID")
	private Long caseId;

	@Id
	@Column(name = "EVENT_PK")
	private Long eventId;

	public CaseEvent() {
	}

	public CaseEvent(Long caseId, Long eventId) {
		this.caseId = caseId;
		this.eventId = eventId;
	}

	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		jo.put("caseId", caseId);
		jo.put("eventId", eventId);
		return jo.toString();
	}
}