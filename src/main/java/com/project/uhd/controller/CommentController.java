package com.project.uhd.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uhd.dto.ApiResponse;
import com.project.uhd.dto.CaseCommentCreateRequest;
import com.project.uhd.dto.CommentDTO;
import com.project.uhd.dto.EventCommentCreateRequest;
import com.project.uhd.dto.EventCommentSearchRequest;
import com.project.uhd.service.CommentService;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping("/case")
	public ResponseEntity<ApiResponse<CommentDTO>> createCaseComment(@RequestBody CaseCommentCreateRequest request) {
		CommentDTO created = commentService.createCaseComment(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "留言新增成功", created));
	}

	@GetMapping("/case/{caseId}")
	public ResponseEntity<ApiResponse<List<CommentDTO>>> getCommentsByCaseId(@PathVariable Long caseId) {
		List<CommentDTO> result = commentService.getCaseComments(caseId, "ASC");
		return ResponseEntity.ok(new ApiResponse<>(true, "Comments fetched!", result));
	}

	@PostMapping("/event")
	public ResponseEntity<ApiResponse<CommentDTO>> createEventComment(@RequestBody EventCommentCreateRequest request) {
		CommentDTO created = commentService.createEventComment(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "留言新增成功", created));
	}

	@PostMapping("/event/search")
	public ResponseEntity<ApiResponse<List<CommentDTO>>> getDistinctCommentsByEventIds(
			@RequestBody EventCommentSearchRequest req) {
		List<CommentDTO> result = commentService.getDistinctCommentsByEventIds(req.getEventIds(), req.getOrder());
		return ResponseEntity.ok(new ApiResponse<>(true, "查詢成功", result));
	}

//	/** 處理中細節子狀態的時間軸歷程（只回傳有帶 processingDetailStatus 的留言，依時間排序）。 */
//	@GetMapping("/event/{eventId}/processing-detail-history")
//	public ResponseEntity<ApiResponse<List<CommentDTO>>> getEventProcessingDetailHistory(
//			@PathVariable String eventId) {
//		List<CommentDTO> result = commentService.getEventProcessingDetailHistory(eventId);
//		return ResponseEntity.ok(new ApiResponse<>(true, "歷程查詢成功", result));
//	}
//
//	@GetMapping("/case/{caseId}/processing-detail-history")
//	public ResponseEntity<ApiResponse<List<CommentDTO>>> getCaseProcessingDetailHistory(@PathVariable Long caseId) {
//		List<CommentDTO> result = commentService.getCaseProcessingDetailHistory(caseId);
//		return ResponseEntity.ok(new ApiResponse<>(true, "歷程查詢成功", result));
//	}
}
