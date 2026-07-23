//package com.project.uhdbackend.dto;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import com.project.uhdbackend.entity.Comment;
//import com.project.uhdbackend.entity.Message;
//import com.project.uhdbackend.entity.Ticket;
//
//public class TicketDTO {
//	private Long ticketId;
//	private String title;
//	private String author;
//	private String description;
//	private LocalDateTime timestamp;
//	private List<Long> messageIds;
//	private List<Long> commentIds;
//
//	public TicketDTO() {
//	}
//
//	public TicketDTO(Ticket ticket) {
//		this.ticketId = ticket.getTicketId();
//		this.title = ticket.getTicketTitle();
//		this.author = ticket.getTicketAuthor();
//		this.description = ticket.getTicketDescription();
//		this.timestamp = ticket.getTicketTimestamp();
//		this.messageIds = ticket.getMessages().stream().map(Message::getMessageId).collect(Collectors.toList());
//		this.commentIds = ticket.getComments().stream().map(Comment::getCommentId).collect(Collectors.toList());
//	}
//
//	public TicketDTO(Long ticketId, String title) {
//		this.ticketId = ticketId;
//		this.title = title;
//	}
//
//	public Long getTicketId() {
//		return ticketId;
//	}
//
//	public void setTicketId(Long ticketId) {
//		this.ticketId = ticketId;
//	}
//
//	public String getTitle() {
//		return title;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}
//
//	public String getAuthor() {
//		return author;
//	}
//
//	public void setAuthor(String author) {
//		this.author = author;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public LocalDateTime getTimestamp() {
//		return timestamp;
//	}
//
//	public void setTimestamp(LocalDateTime timestamp) {
//		this.timestamp = timestamp;
//	}
//
//	public List<Long> getMessageIds() {
//		return messageIds;
//	}
//
//	public void setMessageIds(List<Long> messageIds) {
//		this.messageIds = messageIds;
//	}
//
//	public List<Long> getCommentIds() {
//		return commentIds;
//	}
//
//	public void setCommentIds(List<Long> commentIds) {
//		this.commentIds = commentIds;
//	}
//
//}
