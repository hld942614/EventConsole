package com.project.uhdbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProduceService {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void send(String message, String topic) {
		System.out.println("Send ----> " + message);
		kafkaTemplate.send(topic, message);
	}
}
