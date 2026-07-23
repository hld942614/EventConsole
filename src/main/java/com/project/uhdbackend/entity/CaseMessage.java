//package com.project.uhdbackend.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.IdClass;
//import javax.persistence.Table;
//
//import org.json.JSONObject;
//
//import lombok.Data;
//
//@Entity
//@Data
//@IdClass(CaseMessageId.class)
//@Table(name = "muhd_case_message")
//public class CaseMessage {
//
//	@Id
//	@Column(name = "CASE_ID")
//	private Long caseId;
//
//	@Id
//	@Column(name = "MESSAGE_ID")
//	private Long messageId;
//
//	public CaseMessage() {
//	}
//
//	public CaseMessage(Long caseId, Long messageId) {
//		this.caseId = caseId;
//		this.messageId = messageId;
//	}
//
//	public Long getCaseId() {
//		return caseId;
//	}
//
//	public void setCaseId(Long caseId) {
//		this.caseId = caseId;
//	}
//
//	public Long getMessageId() {
//		return messageId;
//	}
//
//	public void setMessageId(Long messageId) {
//		this.messageId = messageId;
//	}
//
//	@Override
//	public String toString() {
//		JSONObject jo = new JSONObject();
//		jo.put("caseId", caseId);
//		jo.put("messageId", messageId);
//		return jo.toString();
//	}
//}
