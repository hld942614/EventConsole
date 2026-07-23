//package com.project.uhdbackend.controller;
//
//import java.util.List;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.project.uhdbackend.dto.ApiResponse;
//import com.project.uhdbackend.dto.TicketCreateRequest;
//import com.project.uhdbackend.dto.TicketDTO;
//import com.project.uhdbackend.dto.TicketDetailDTO;
//import com.project.uhdbackend.dto.TicketUpdateRequest;
//import com.project.uhdbackend.service.TicketService;
//
//@RestController
//@RequestMapping("/api/v1/tickets")
//public class TicketController {
//
//	private final TicketService ticketService;
//
//	public TicketController(TicketService ticketService) {
//		this.ticketService = ticketService;
//	}
//
//	@PostMapping
//	public ResponseEntity<ApiResponse<TicketDTO>> createTicket(@RequestBody TicketCreateRequest request) {
//		TicketDTO dto = ticketService.createTicket(request);
//		return ResponseEntity.ok(new ApiResponse<>(true, "Ticket created", dto));
//	}
//
//	@GetMapping("/{ticketId}")
//	public ResponseEntity<ApiResponse<TicketDetailDTO>> getTicketDetail(@PathVariable Long ticketId) {
//		TicketDetailDTO dto = ticketService.getTicketDetail(ticketId);
//		return ResponseEntity.ok(new ApiResponse<>(true, "Ticket detail fetched", dto));
//	}
//
//	@PutMapping("/{ticketId}")
//	public ResponseEntity<ApiResponse<TicketDTO>> updateTicket(@PathVariable Long ticketId,
//			@RequestBody TicketUpdateRequest request) {
//		TicketDTO dto = ticketService.updateTicket(ticketId, request);
//		return ResponseEntity.ok(new ApiResponse<>(true, "Ticket updated", dto));
//	}
//	
//	@GetMapping
//	public ResponseEntity<ApiResponse<List<TicketDTO>>> getAllTickets() {
//	    List<TicketDTO> tickets = ticketService.getAllTickets();
//	    ApiResponse<List<TicketDTO>> response = new ApiResponse<>(true, "Tickets fetched successfully", tickets);
//	    return ResponseEntity.ok(response);
//	}
//	
//	@DeleteMapping("/{ticketId}")
//	public ResponseEntity<ApiResponse<Void>> deleteTicket(@PathVariable Long ticketId) {
//	    ticketService.deleteTicketById(ticketId);
//	    ApiResponse<Void> response = new ApiResponse<>(true, "Ticket deleted successfully", null);
//	    return ResponseEntity.ok(response);
//	}
//}
