package com.project.uhd.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

	private final EventService eventService;

	public KafkaConsumerService(EventService eventService) {
		this.eventService = eventService;
	}

	@KafkaListener(topics = "UHDEvent")
	public void kafkaEmailConsumer(@Payload String input) {
		try {
			if (input == null || input.isBlank() || input.equals("test")) {
				return;
			}
//			System.out.println(input);
			eventService.processNewEvent(input);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
