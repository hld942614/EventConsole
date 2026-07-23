//package com.project.uhdbackend.controller;
//
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.project.uhdbackend.dto.ApiResponse;
//import com.project.uhdbackend.dto.MessageDTO;
//import com.project.uhdbackend.dto.MessageSearchRequest;
//import com.project.uhdbackend.dto.MessageStatusUpdateRequest;
//import com.project.uhdbackend.service.MessageService;
//
//@RestController
//@RequestMapping("/api/v1/msg")
//public class MessageController {
//
//	private MessageService messageService;
//	
//	public MessageController(MessageService messageService) {
//		this.messageService = messageService;
//	}
//
//	@GetMapping("/{id}")
//	public ResponseEntity<ApiResponse<MessageDTO>> getMessageById(@PathVariable Long id) {
//		return messageService.getMessageDTO(id)
//				.map(msg -> ResponseEntity.ok(new ApiResponse<>(true, "Message found", msg))).orElse(ResponseEntity
//						.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Message not found", null)));
//	}
//
//	@PostMapping("/search")
//	public ResponseEntity<ApiResponse<List<MessageDTO>>> searchMessages(@RequestBody MessageSearchRequest request) {
//		List<MessageDTO> result = messageService.getMessagesByFilters(request.getStatusArray(), request.getSubject(),
//				request.getMainCategory(), request.getSender(), request.getContent(),request.getDay());
//		return ResponseEntity.ok(new ApiResponse<>(true, "Query success", result));
//	}
//	
//	@PatchMapping("/status")
//	public ResponseEntity<ApiResponse<Void>> changeMsgStatus(
//	        @RequestBody MessageStatusUpdateRequest request) {
//
//	    if (request.getMessageId() == null) {
//	        return ResponseEntity.badRequest()
//	                .body(new ApiResponse<>(false, "Message ID list is empty", null));
//	    }
//
//	    if (request.getStatus() == null) {
//	        return ResponseEntity.badRequest()
//	                .body(new ApiResponse<>(false, "Status is required", null));
//	    }
//
//	    messageService.changeMsgStatus(request.getMessageId(), request.getStatus());
//	    return ResponseEntity.ok(new ApiResponse<>(true, "Message Status updated successfully", null));
//	}
//}
