//package com.project.uhdbackend.service;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.stream.Collectors;
//
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.project.uhdbackend.dto.TicketCreateRequest;
//import com.project.uhdbackend.dto.TicketDTO;
//import com.project.uhdbackend.dto.TicketDetailDTO;
//import com.project.uhdbackend.dto.TicketUpdateRequest;
//import com.project.uhdbackend.entity.Comment;
//import com.project.uhdbackend.entity.Message;
//import com.project.uhdbackend.entity.Ticket;
//import com.project.uhdbackend.enums.MessageStatus;
//import com.project.uhdbackend.repository.CommentRepository;
//import com.project.uhdbackend.repository.MessageRepository;
//import com.project.uhdbackend.repository.TicketRepository;
//
//@Service
//public class TicketService {
//
//	private final TicketRepository ticketRepository;
//	private final MessageRepository messageRepository;
//	private final CommentRepository commentRepository;
//
//	public TicketService(TicketRepository ticketRepository, MessageRepository messageRepository,
//			CommentRepository commentRepository) {
//		this.ticketRepository = ticketRepository;
//		this.messageRepository = messageRepository;
//		this.commentRepository = commentRepository;
//	}
//
//	@Transactional
//	public TicketDTO createTicket(TicketCreateRequest request) {
//		Ticket ticket = new Ticket();
//		ticket.setTicketTitle(request.getTitle());
//		ticket.setTicketAuthor(request.getAuthor());
//		ticket.setTicketDescription(request.getDescription());
//		ticket.setTicketTimestamp(LocalDateTime.now());
//
//		if (request.getMessageIds() != null && !request.getMessageIds().isEmpty()) {
//			List<Message> messages = messageRepository.findAllById(request.getMessageIds());
//			for (Message msg : messages) {
//				msg.setStatus(MessageStatus.PROCESSING);
//			}
//			ticket.setMessages(new HashSet<>(messages));
//		}
//
//		if (request.getCommentIds() != null && !request.getCommentIds().isEmpty()) {
//			List<Comment> comments = commentRepository.findAllById(request.getCommentIds());
//			ticket.setComments(new HashSet<>(comments));
//		}
//
//		Ticket saved = ticketRepository.save(ticket);
//		return new TicketDTO(saved);
//	}
//
//	public TicketDetailDTO getTicketDetail(Long ticketId) {
//		Ticket ticket = ticketRepository.findById(ticketId)
//				.orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
//
//		return new TicketDetailDTO(ticket);
//	}
//
//	@Transactional
//	public TicketDTO updateTicket(Long ticketId, TicketUpdateRequest request) {
//		Ticket ticket = ticketRepository.findById(ticketId)
//				.orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
//
//		if (request.getTitle() != null)
//			ticket.setTicketTitle(request.getTitle());
//		if (request.getDescription() != null)
//			ticket.setTicketDescription(request.getDescription());
//		if (request.getStatus() != null)
//			ticket.setTicketStatus(request.getStatus());
//
//		Ticket saved = ticketRepository.save(ticket);
//		return new TicketDTO(saved);
//	}
//
//	@Transactional
//	public void deleteTicketById(Long ticketId) {
//		Ticket ticket = ticketRepository.findById(ticketId)
//				.orElseThrow(() -> new NoSuchElementException("Ticket not found"));
//
//		// 移除與 message/comment 的關聯，避免外鍵 constraint 錯誤
//		for (Message message : ticket.getMessages()) {
//			message.getTickets().remove(ticket);
//			message.setStatus(MessageStatus.UNPROCESSED);
//		}
//
//		for (Comment comment : ticket.getComments()) {
//			comment.getTickets().remove(ticket);
//		}
//
//		ticket.getMessages().clear();
//		ticket.getComments().clear();
//
//		ticketRepository.delete(ticket);
//	}
//
//	@Transactional(readOnly = true)
//	public List<TicketDTO> getAllTickets() {
//		List<Ticket> tickets = ticketRepository.findAll(Sort.by(Sort.Direction.DESC, "ticketTimestamp"));
//		return tickets.stream()
//	            .map(t -> new TicketDTO(t.getTicketId(), t.getTicketTitle()))
//	            .collect(Collectors.toList());
//	}
//}
