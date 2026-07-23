//package com.project.uhdbackend.controller;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.project.uhdbackend.dto.ApiResponse;
//import com.project.uhdbackend.dto.CaseMessageBatchRequest;
//import com.project.uhdbackend.dto.CaseMessageDeleteRequest;
//import com.project.uhdbackend.service.CaseMessageService;
//
//@RestController
//@RequestMapping("/api/v1/case-message")
//public class CaseMessageController {
//
//	private CaseMessageService caseMessageService;
//
//	public CaseMessageController(CaseMessageService caseMessageService) {
//		this.caseMessageService = caseMessageService;
//	}
//
//	@DeleteMapping
//	public ResponseEntity<ApiResponse<Void>> deleteCaseMessage(@RequestBody CaseMessageDeleteRequest req) {
//		caseMessageService.removeCaseMessages(req.getCaseId(), req.getMessageIds());
//		return ResponseEntity.ok(new ApiResponse<>(true, "Deleted successfully.", null));
//	}
//
////	@GetMapping("/case/{caseId}")
////	public ResponseEntity<ApiResponse<List<CaseMessage>>> getMessagesByCaseId(@PathVariable Long caseId) {
////
////		List<CaseMessage> result = caseMessageService.getMessagesByCaseId(caseId);
////		return ResponseEntity.ok(new ApiResponse<>(true, "Fetched successfully.", result));
////	}
//
////	@GetMapping("/message/{messageId}")
////	public ResponseEntity<ApiResponse<List<CaseMessage>>> getCasesByMessageId(@PathVariable Long messageId) {
////
////		List<CaseMessage> result = caseMessageService.getCasesByMessageId(messageId);
////		return ResponseEntity.ok(new ApiResponse<>(true, "Fetched successfully.", result));
////	}
//
////	@GetMapping("/all")
////	public ResponseEntity<ApiResponse<List<CaseMessage>>> getAllMessageGroups() {
////		List<CaseMessage> result = caseMessageService.getAllCaseMessages();
////		return ResponseEntity.ok(new ApiResponse<>(true, "All case messages fetched.", result));
////	}
//
//	@PostMapping
//	public ResponseEntity<ApiResponse<Map<String, Object>>> addMessagesToCase(
//			@RequestBody CaseMessageBatchRequest request) {
//
//		Long caseId = request.getCaseId();
//		List<Long> messageIds = request.getMessageIds();
//		caseMessageService.addMessagesToCase(caseId, messageIds);
//
//		return ResponseEntity.ok(new ApiResponse<>(true, "Batch insert completed.", null));
//	}
//}
