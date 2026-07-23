//package com.project.uhdbackend.entity;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.Set;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "MUHD_TICKET")
//public class Ticket {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq_gen")
//	@SequenceGenerator(name = "ticket_seq_gen", sequenceName = "MUHD_TICKET_SEQ", allocationSize = 1)
//	@Column(name = "TICKET_ID")
//	private Long ticketId;
//
//	@Column(name = "TICKET_TITLE")
//	private String ticketTitle;
//
//	@Column(name = "TICKET_AUTHOR")
//	private String ticketAuthor;
//
//	@Column(name = "TICKET_DESCRIPTION")
//	private String ticketDescription;
//
//	@Column(name = "TICKET_TIMESTAMP")
//	private LocalDateTime ticketTimestamp;
//	
//	@Column(name = "TICKET_STATUS")
//	private String ticketStatus;
//
//	@ManyToMany
//	@JoinTable(name = "MUHD_TICKET_MESSAGE", joinColumns = @JoinColumn(name = "TICKET_ID"), inverseJoinColumns = @JoinColumn(name = "MESSAGE_ID"))
//	private Set<Message> messages = new HashSet<>();
//
//	@ManyToMany
//	@JoinTable(name = "MUHD_TICKET_COMMENT", joinColumns = @JoinColumn(name = "TICKET_ID"), inverseJoinColumns = @JoinColumn(name = "COMMENT_ID"))
//	private Set<Comment> comments = new HashSet<>();
//
//	public Long getTicketId() {
//		return ticketId;
//	}
//
//	public void setTicketId(Long ticketId) {
//		this.ticketId = ticketId;
//	}
//
//	public String getTicketTitle() {
//		return ticketTitle;
//	}
//
//	public void setTicketTitle(String ticketTitle) {
//		this.ticketTitle = ticketTitle;
//	}
//
//	public String getTicketAuthor() {
//		return ticketAuthor;
//	}
//
//	public void setTicketAuthor(String ticketAuthor) {
//		this.ticketAuthor = ticketAuthor;
//	}
//
//	public String getTicketDescription() {
//		return ticketDescription;
//	}
//
//	public void setTicketDescription(String ticketDescription) {
//		this.ticketDescription = ticketDescription;
//	}
//
//	public LocalDateTime getTicketTimestamp() {
//		return ticketTimestamp;
//	}
//
//	public void setTicketTimestamp(LocalDateTime ticketTimestamp) {
//		this.ticketTimestamp = ticketTimestamp;
//	}
//
//	public Set<Message> getMessages() {
//		return messages;
//	}
//
//	public void setMessages(Set<Message> messages) {
//		this.messages = messages;
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
//	public String getTicketStatus() {
//		return ticketStatus;
//	}
//
//	public void setTicketStatus(String ticketStatus) {
//		this.ticketStatus = ticketStatus;
//	}
//}
