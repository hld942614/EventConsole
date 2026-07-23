//package com.project.uhdbackend.dto;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.json.JSONObject;
//
//import com.project.uhdbackend.entity.Case;
//import com.project.uhdbackend.entity.Message;
//import com.project.uhdbackend.enums.MessageStatus;
//
//public class MessageDTO {
//
//	private Long messageId;
//	private String alertTimestamp;
//	private String alertCode;
//	private String subject;
//	private String sourceIp;
//	private String data;
//	private String sender;
//	private String receiver;
//	private String emailTimestamp;
//	private String dbTimestamp;
//	private MessageStatus status;
//	private String mainCategoryTitle;
//	private String mainCategoryCode;
//	private boolean hasCase;
//	private List<Long> caseIds;
////	private TicketDTO ticket;
//
//	public MessageDTO() {
//	}
//
//	public MessageDTO(Message message) {
//		this.messageId = message.getMessageId();
//		this.alertTimestamp = message.getAlertTimestamp();
//		this.alertCode = message.getAlertCode();
//		this.subject = message.getSubject();
//		this.sourceIp = message.getSourceIp();
//		this.data = message.getData();
//		this.sender = message.getSender();
//		this.receiver = message.getReceiver();
//		this.emailTimestamp = message.getEmailTimestamp();
//		this.dbTimestamp = message.getDbTimestamp();
//		this.status = message.getStatus();
//		Set<Case> cases = message.getCases();
//		if (cases != null && !cases.isEmpty()) {
//			this.hasCase = true;
//			this.caseIds = cases.stream().map(Case::getId).collect(Collectors.toList());
//		} else {
//			this.hasCase = false;
//			this.caseIds = Collections.emptyList();
//		}
//	}
//
////	public MessageDTO(Long messageId, String alertTimestamp, String alertCode, String subject,
////			String sourceIp, String data, String sender, String receiver, String emailTimestamp, String dbTimestamp,
////			String status, String mainCategoryTitle) {
////		this.messageId = messageId;
////		this.alertTimestamp = alertTimestamp;
////		this.alertCode = alertCode;
////		this.subject = subject;
////		this.sourceIp = sourceIp;
////		this.data = data;
////		this.sender = sender;
////		this.receiver = receiver;
////		this.emailTimestamp = emailTimestamp;
////		this.dbTimestamp = dbTimestamp;
////		this.status = status;
////		this.mainCategoryTitle = mainCategoryTitle;
////	}
//
//	public Long getMessageId() {
//		return messageId;
//	}
//
//	public void setMessageId(Long messageId) {
//		this.messageId = messageId;
//	}
//
//	public String getAlertTimestamp() {
//		return alertTimestamp;
//	}
//
//	public void setAlertTimestamp(String alertTimestamp) {
//		this.alertTimestamp = alertTimestamp;
//	}
//
//	public String getAlertCode() {
//		return alertCode;
//	}
//
//	public void setAlertCode(String alertCode) {
//		this.alertCode = alertCode;
//	}
//
//	public String getSubject() {
//		return subject;
//	}
//
//	public void setSubject(String subject) {
//		this.subject = subject;
//	}
//
//	public String getSourceIp() {
//		return sourceIp;
//	}
//
//	public void setSourceIp(String sourceIp) {
//		this.sourceIp = sourceIp;
//	}
//
//	public String getData() {
//		return data;
//	}
//
//	public void setData(String data) {
//		this.data = data;
//	}
//
//	public String getSender() {
//		return sender;
//	}
//
//	public void setSender(String sender) {
//		this.sender = sender;
//	}
//
//	public String getReceiver() {
//		return receiver;
//	}
//
//	public void setReceiver(String receiver) {
//		this.receiver = receiver;
//	}
//
//	public String getEmailTimestamp() {
//		return emailTimestamp;
//	}
//
//	public void setEmailTimestamp(String emailTimestamp) {
//		this.emailTimestamp = emailTimestamp;
//	}
//
//	public String getDbTimestamp() {
//		return dbTimestamp;
//	}
//
//	public void setDbTimestamp(String dbTimestamp) {
//		this.dbTimestamp = dbTimestamp;
//	}
//
//	public String getMainCategoryTitle() {
//		return mainCategoryTitle;
//	}
//
//	public void setMainCategoryTitle(String mainCategoryTitle) {
//		this.mainCategoryTitle = mainCategoryTitle;
//	}
//
//	public MessageStatus getStatus() {
//		return status;
//	}
//
//	public void setStatus(MessageStatus status) {
//		this.status = status;
//	}
//
////	public TicketDTO getTicket() {
////		return ticket;
////	}
////
////	public void setTicket(TicketDTO ticket) {
////		this.ticket = ticket;
////	}
//
//	public boolean isHasCase() {
//		return hasCase;
//	}
//
//	public void setHasCase(boolean hasCase) {
//		this.hasCase = hasCase;
//	}
//
//	public List<Long> getCaseIds() {
//		return caseIds;
//	}
//
//	public void setCaseIds(List<Long> caseIds) {
//		this.caseIds = caseIds;
//	}
//
//	public String getMainCategoryCode() {
//		return mainCategoryCode;
//	}
//
//	public void setMainCategoryCode(String mainCategoryCode) {
//		this.mainCategoryCode = mainCategoryCode;
//	}
//
//	@Override
//	public String toString() {
//		JSONObject jo = new JSONObject();
//		jo.put("messageId", this.messageId);
//		jo.put("alertTimestamp", this.alertTimestamp);
//		jo.put("emailTimestamp", this.emailTimestamp);
//		jo.put("dbTimestamp", this.dbTimestamp);
//		jo.put("alertCode", this.alertCode);
//		jo.put("subject", this.subject);
//		jo.put("sourceIp", this.sourceIp);
//		jo.put("data", this.data);
//		jo.put("sender", this.sender);
//		jo.put("receiver", this.receiver);
//		jo.put("status", this.status);
//		jo.put("mainCategoryTitle", this.mainCategoryTitle);
//		jo.put("mainCategoryCode", this.mainCategoryCode);
//		return jo.toString();
//	}
//}
