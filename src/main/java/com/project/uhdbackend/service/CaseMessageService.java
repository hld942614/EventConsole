//package com.project.uhdbackend.service;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.project.uhdbackend.entity.CaseMessage;
//import com.project.uhdbackend.entity.CaseMessageId;
//import com.project.uhdbackend.entity.Message;
//import com.project.uhdbackend.enums.MessageStatus;
//import com.project.uhdbackend.realtime.event.EventType;
//import com.project.uhdbackend.realtime.service.RealtimeEventService;
//import com.project.uhdbackend.repository.CaseMessageRepository;
//import com.project.uhdbackend.repository.MessageRepository;
//
//@Service
//public class CaseMessageService {
//
//	private CaseMessageRepository caseMessageRepository;
//	private MessageRepository messageRepository;
//	private final RealtimeEventService realtimeEventService;
//
//	public CaseMessageService(CaseMessageRepository caseMessageRepository, MessageRepository messageRepository,
//			RealtimeEventService realtimeEventService) {
//		this.caseMessageRepository = caseMessageRepository;
//		this.messageRepository = messageRepository;
//		this.realtimeEventService = realtimeEventService;
//	}
//
//	@Value("${spring.datasource.url}")
//	private String DB_URL;
//	@Value("${spring.datasource.username}")
//	private String USER;
//	@Value("${spring.datasource.password}")
//	private String PASS;
//
//	@Transactional
//	public void addMessagesToCase(Long caseId, List<Long> messageIds) {
//		for (Long msgId : messageIds) {
//			CaseMessageId id = new CaseMessageId(caseId, msgId);
//			if (!caseMessageRepository.existsById(id)) {
//				caseMessageRepository.save(new CaseMessage(caseId, msgId));
//			}
//		}
//		realtimeEventService.publish(EventType.MESSAGE_CLASSIFIED, "CASE-MESSAGE", caseId, messageIds);
//	}
//
//	@Transactional
//	public void removeCaseMessages(Long caseId, List<Long> messageIds) {
//		if (caseId == null || messageIds == null || messageIds.isEmpty()) {
//			return;
//		}
//
//		List<Long> filteredIds = messageIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
//
//		if (filteredIds.isEmpty()) {
//			return;
//		}
//
//		List<CaseMessageId> ids = filteredIds.stream().map(mid -> new CaseMessageId(caseId, mid))
//				.collect(Collectors.toList());
//
//		caseMessageRepository.deleteAllByIdInBatch(ids);
//		realtimeEventService.publish(EventType.MESSAGE_RECLASSIFIED, "CASE-MESSAGE", caseId, messageIds);
//	}
//
//	public List<CaseMessage> getMessagesByCaseId(Long caseId) {
//		return caseMessageRepository.findByCaseId(caseId);
//	}
//
//	public List<CaseMessage> getCasesByMessageId(Long messageId) {
//		return caseMessageRepository.findByMessageId(messageId);
//	}
//
//	public List<CaseMessage> getAllCaseMessages() {
//		return caseMessageRepository.findAll();
//	}
//
//	public void removeCaseMessageByCaseId(Long caseId) {
//		String sql = "DELETE FROM MUHD_CASE_MESSAGE WHERE CASE_ID = ?";
//		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
//				PreparedStatement stmt = conn.prepareStatement(sql);) {
//			stmt.setLong(1, caseId);
//			stmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public List<Message> getMsgDetailByCaseId(Long caseId) {
//		String sql = "SELECT * FROM MUHD_MESSAGE m WHERE m.MESSAGE_ID IN ( SELECT mg.MESSAGE_ID FROM MUHD_CASE_MESSAGE mg WHERE mg.CASE_ID = ? )";
//		List<Message> list = new ArrayList<>();
//		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
//				PreparedStatement stmt = conn.prepareStatement(sql);) {
//			stmt.setLong(1, caseId);
//			try (ResultSet result = stmt.executeQuery();) {
//				while (result.next()) {
//					list.add(resultToMessage(result));
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
//
//	@Transactional
//	public void updateMessagesStatusByCaseId(Long caseId, MessageStatus targetStatus) {
//
//		List<CaseMessage> relations = caseMessageRepository.findByCaseId(caseId);
//		if (relations.isEmpty()) {
//			return;
//		}
//		List<Long> messageIds = relations.stream().map(cm -> cm.getMessageId()).distinct().collect(Collectors.toList());
//		List<Message> messages = messageRepository.findAllById(messageIds);
//		for (Message m : messages) {
//			m.setStatus(targetStatus);
//		}
//	}
//
//	private Message resultToMessage(ResultSet result) throws SQLException {
//		Message msg = new Message();
//		msg.setAlertTimestamp(result.getString("MESSAGE_ALERTTIMESTAMP"));
//		msg.setAlertCode(result.getString("MESSAGE_ALERTCODE"));
//		msg.setSubject(result.getString("MESSAGE_SUBJECT"));
//		msg.setSourceIp(result.getString("MESSAGE_SOURCEIP"));
//		msg.setData(result.getString("MESSAGE_DATA"));
//		msg.setSender(result.getString("MESSAGE_SENDER"));
//		msg.setReceiver(result.getString("MESSAGE_RECEIVER"));
//		msg.setMessageId(result.getLong("MESSAGE_ID"));
//		msg.setEmailTimestamp(result.getString("MESSAGE_EMAILTIMESTAMP"));
//		msg.setDbTimestamp(result.getString("MESSAGE_DBTIMESTAMP"));
//		msg.setStatus(MessageStatus.valueOf(result.getString("MESSAGE_STATUS")));
//		return msg;
//	}
//}
