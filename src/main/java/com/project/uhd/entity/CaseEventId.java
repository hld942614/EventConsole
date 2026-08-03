package com.project.uhd.entity;

import java.io.Serializable;
import java.util.Objects;

public class CaseEventId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long caseId;
	private Long eventId; // 對應 MUHD_EVENT.ID（內部 PK）

	public CaseEventId() {
	}

	public CaseEventId(Long caseId, Long eventId) {
		this.caseId = caseId;
		this.eventId = eventId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof CaseEventId))
			return false;
		CaseEventId that = (CaseEventId) o;
		return Objects.equals(caseId, that.caseId) && Objects.equals(eventId, that.eventId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(caseId, eventId);
	}
}