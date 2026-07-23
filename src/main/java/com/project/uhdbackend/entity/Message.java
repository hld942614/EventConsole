//package com.project.uhdbackend.entity;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//
//import org.json.JSONObject;
//
//import com.project.uhdbackend.enums.MessageStatus;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "muhd_message")
//@NoArgsConstructor
//@AllArgsConstructor
//public class Message {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_id_gen")
//	@SequenceGenerator(name = "message_id_gen", sequenceName = "message_id_seq", allocationSize = 1)
//	@Column(name = "message_id")
//	private Long messageId;
//
//	@Column(name = "message_alerttimestamp")
//	private String alertTimestamp;
//
//	@Column(name = "message_emailtimestamp")
//	private String emailTimestamp;
//
//	@Column(name = "message_dbtimestamp")
//	private String dbTimestamp;
//
//	@Column(name = "message_alertcode")
//	private String alertCode;
//
//	@Column(name = "message_subject")
//	private String subject;
//
//	@Column(name = "message_sourceip")
//	private String sourceIp;
//
//	@Column(name = "message_data")
//	private String data;
//
//	@Column(name = "message_sender")
//	private String sender;
//
//	@Column(name = "message_receiver")
//	private String receiver;
//
//	@Enumerated(EnumType.STRING)
//	@Column(name = "message_status")
//	private MessageStatus status = MessageStatus.UNPROCESSED;
//
//	@ManyToMany
//	@JoinTable(name = "MUHD_MESSAGE_COMMENT", joinColumns = @JoinColumn(name = "MESSAGE_ID"), inverseJoinColumns = @JoinColumn(name = "COMMENT_ID"))
//	@JsonIgnore
//	private Set<Comment> comments = new HashSet<>();
//
//	@ManyToMany(mappedBy = "messages")
//	@JsonIgnore
//	private Set<Case> cases = new HashSet<>();
//
////	@ManyToMany(mappedBy = "messages")
////	private Set<Ticket> tickets = new HashSet<>();
//
//	public Long getMessageId() {
//		return messageId;
//	}
//
//	public void setMessageId(Long messageId) {
//		this.messageId = messageId;
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
//	public String getAlertTimestamp() {
//		return alertTimestamp;
//	}
//
//	public void setAlertTimestamp(String alertTimestamp) {
//		this.alertTimestamp = alertTimestamp;
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
//	public MessageStatus getStatus() {
//		return status;
//	}
//
//	public void setStatus(MessageStatus status) {
//		this.status = status;
//	}
//
//	public Set<Comment> getComments() {
//		return comments;
//	}
//
//	public void setComments(Set<Comment> comments) {
//		this.comments = comments;
//	}
//
//	public Set<Case> getCases() {
//		return cases;
//	}
//
//	public void setCases(Set<Case> cases) {
//		this.cases = cases;
//	}
//	
//	public void addCase(Case casex) {
//		this.cases.add(casex);
//		casex.getMessages().add(this);
//	}
//
//	public void addComment(Comment c) {
//	    this.comments.add(c);          
//	    c.getMessages().add(this);     
//	}
//
//	public void removeComment(Comment c) {
//	    this.comments.remove(c);       
//	    c.getMessages().remove(this);  
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
//		return jo.toString();
//	}
//}
