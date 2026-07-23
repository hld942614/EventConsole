//package com.project.uhdbackend.dto;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import com.project.uhdbackend.entity.Ticket;
//
//public class TicketDetailDTO {
//    private Long ticketId;
//    private String title;
//    private String description;
//    private String author;
//    private String status;
//    private LocalDateTime timestamp;
//    private List<MessageDTO> messages;
//    private List<CommentDTO> comments;
//    
//    public TicketDetailDTO(Ticket ticket) {
//        this.ticketId = ticket.getTicketId();
//        this.title = ticket.getTicketTitle();
//        this.author = ticket.getTicketAuthor();
//        this.description = ticket.getTicketDescription();
//        this.timestamp = ticket.getTicketTimestamp();
//        this.status = ticket.getTicketStatus();
//
//        this.comments = ticket.getComments().stream()
//                .map(CommentDTO::new)
//                .collect(Collectors.toList());
//
//        this.messages = ticket.getMessages().stream()
//                .map(MessageDTO::new)
//                .collect(Collectors.toList());
//    }
//    
//	public Long getTicketId() {
//		return ticketId;
//	}
//	public void setTicketId(Long ticketId) {
//		this.ticketId = ticketId;
//	}
//	public String getTitle() {
//		return title;
//	}
//	public void setTitle(String title) {
//		this.title = title;
//	}
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
//	public String getAuthor() {
//		return author;
//	}
//	public void setAuthor(String author) {
//		this.author = author;
//	}
//	public String getStatus() {
//		return status;
//	}
//	public void setStatus(String status) {
//		this.status = status;
//	}
//	public LocalDateTime getTimestamp() {
//		return timestamp;
//	}
//	public void setTimestamp(LocalDateTime timestamp) {
//		this.timestamp = timestamp;
//	}
//	public List<MessageDTO> getMessages() {
//		return messages;
//	}
//	public void setMessages(List<MessageDTO> messages) {
//		this.messages = messages;
//	}
//	public List<CommentDTO> getComments() {
//		return comments;
//	}
//	public void setComments(List<CommentDTO> comments) {
//		this.comments = comments;
//	}
//    
//}
