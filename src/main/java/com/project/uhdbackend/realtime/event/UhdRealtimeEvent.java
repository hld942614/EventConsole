package com.project.uhdbackend.realtime.event;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UhdRealtimeEvent {
	private EventType type;
	private String aggType;
	private Object aggId;
	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime timeStamp;
	private Object data;

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public String getAggType() {
		return aggType;
	}

	public void setAggType(String aggType) {
		this.aggType = aggType;
	}

	public Object getAggId() {
		return aggId;
	}

	public void setAggId(Object aggId) {
		this.aggId = aggId;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
