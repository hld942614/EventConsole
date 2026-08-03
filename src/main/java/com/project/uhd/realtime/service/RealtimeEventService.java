package com.project.uhd.realtime.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.project.uhd.realtime.event.EventType;
import com.project.uhd.realtime.event.RealtimeEventPublisher;
import com.project.uhd.realtime.event.UhdRealtimeEvent;

@Service
public class RealtimeEventService {

	private final RealtimeEventPublisher publisher;

	public RealtimeEventService(RealtimeEventPublisher publisher) {
		this.publisher = publisher;
	}

	public void publish(EventType type, String aggType, Object aggId, Object data) {
		UhdRealtimeEvent evt = new UhdRealtimeEvent();
		evt.setType(type);
		evt.setAggType(aggType);
		evt.setAggId(aggId);
		evt.setTimeStamp(LocalDateTime.now());
		evt.setData(data);
		publisher.publish(evt);
	}
}
