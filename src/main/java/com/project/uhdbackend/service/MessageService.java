//package com.project.uhdbackend.service;
//
//import java.util.List;
//import java.util.Optional;
//
//import javax.transaction.Transactional;
//
//import org.springframework.stereotype.Service;
//
//import com.project.uhdbackend.dto.MessageDTO;
//import com.project.uhdbackend.entity.Category;
//import com.project.uhdbackend.entity.Message;
//import com.project.uhdbackend.enums.MessageStatus;
//import com.project.uhdbackend.realtime.event.EventType;
//import com.project.uhdbackend.realtime.service.RealtimeEventService;
//import com.project.uhdbackend.repository.MessageQueryRepository;
//import com.project.uhdbackend.repository.MessageRepository;
//
//@Service
//public class MessageService {
//
//	private final MessageRepository messageRepository;
//	private final MessageQueryRepository messageQueryRepository;
//	private final RealtimeEventService realtimeEventService;
//	private final CategoryService categoryService;
//
//	public MessageService(MessageRepository messageRepository, MessageQueryRepository messageQueryRepository,
//			RealtimeEventService realtimeEventService, CategoryService categoryService) {
//		this.messageRepository = messageRepository;
//		this.messageQueryRepository = messageQueryRepository;
//		this.realtimeEventService = realtimeEventService;
//		this.categoryService = categoryService;
//	}
//
//	public Message getById(Long messageId) {
//		return messageRepository.findById(messageId).get();
//	}
//
//	public MessageDTO saveMessage(Message message) {
//		return new MessageDTO(messageRepository.save(message));
//	}
//
//	public Optional<MessageDTO> getMessageDTO(Long id) {
//		return messageRepository.findById(id).map(MessageDTO::new);
//	}
//
//	public List<Message> getAllMessages() {
//		return messageRepository.findAll();
//	}
//
//	public void deleteAll() {
//		messageRepository.deleteAll();
//	}
//
//	public void deleteMessage(Long id) {
//		messageRepository.deleteById(id);
//	}
//
//	@Transactional
//	public void changeMsgStatus(Long id, MessageStatus status) {
//		Message msg = messageRepository.findById(id)
//				.orElseThrow(() -> new IllegalArgumentException("Message not found: " + id));
//		msg.setStatus(status);
//		MessageDTO dto = new MessageDTO(msg);
//		Category category = categoryService.getMainByAlertCode(msg.getAlertCode());
//		if(category!=null) {
//			dto.setMainCategoryTitle(category.getTitle()==null?null:category.getTitle());
//			dto.setMainCategoryCode(category.getCode()==null?null:category.getCode());
//		}
//		realtimeEventService.publish(EventType.MESSAGE_UPDATED, "MESSAGE", dto.getMessageId(), dto);
//	}
//
//	public List<MessageDTO> getMessagesWithMainCategoryTitle(String categoryCode) {
//		return messageQueryRepository.getMessagesWithMainCategoryTitle(categoryCode);
//	}
//
//	public List<MessageDTO> getUncategorizedMessagesWithMainCategoryTitle() {
//		return messageQueryRepository.getUncategorizedMessagesWithMainCategoryTitle();
//	}
//
//	public List<MessageDTO> getMessagesByFilters(List<MessageStatus> statusArray, String subject, String mainCategory, String sender,
//			String content, String day) {
//		return messageQueryRepository.getMessagesByFilters(statusArray, subject, mainCategory, sender, content, day);
//	}
//}
