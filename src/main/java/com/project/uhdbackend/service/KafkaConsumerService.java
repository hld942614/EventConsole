package com.project.uhdbackend.service;

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
			System.out.println(input);
			eventService.processNewEvent(input);
			return;

//			Message message = parseMessage(input);
//			if (message == null)
//				return;
//
//			self.handleMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private Message parseMessage(String input) {
//		if (input == null || input.trim().isEmpty())
//			return null;
//
//		try {
//			if (isValidJson(input)) {
//				return messageParser.transferApiToMsg(input);
//			} else if (input.contains("Return-Path")) {
//				return messageParser.transferMailToMsg(input);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//
//	@Transactional
//	public void handleMessage(Message msg) {
//		messageService.saveMessage(msg);
//		classifierService.classifyAndAddMessageToCases(msg);
//
//		// 取得分類
//		Category mainCategory = categoryService.getMainByAlertCode(msg.getAlertCode());
//		String mainCategoryCode = mainCategory == null ? "Others" : mainCategory.getCode();
//		String mainCategoryTitle = mainCategory == null ? "" : mainCategory.getTitle();
//
//		// 組 DTO 並發送 WebSocket
//		MessageDTO dto = new MessageDTO(msg);
//		dto.setMainCategoryTitle(mainCategoryTitle);
//		dto.setMainCategoryCode(mainCategoryCode);
//		realtimeEventService.publish(EventType.MESSAGE_CREATED, "MESSAGE", dto.getMessageId(), dto);
//	}

//	private boolean isValidJson(String input) {
//		try {
//			new JSONObject(input);
//			return true;
//		} catch (JSONException e) {
//			return false;
//		}
//	}
}
