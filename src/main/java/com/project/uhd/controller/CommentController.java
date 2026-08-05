package com.project.uhd.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uhd.authentication.CustomUserDetails;
import com.project.uhd.dto.ApiResponse;
import com.project.uhd.dto.CaseCommentCreateRequest;
import com.project.uhd.dto.CommentDTO;
import com.project.uhd.dto.CommentUpdateRequest;
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
	public ResponseEntity<ApiResponse<CommentDTO>> createCaseComment(@RequestBody CaseCommentCreateRequest request,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		CommentDTO created = commentService.createCaseComment(request, currentUser);
		return ResponseEntity.ok(new ApiResponse<>(true, "留言新增成功", created));
	}

	@GetMapping("/case/{caseId}")
	public ResponseEntity<ApiResponse<List<CommentDTO>>> getCommentsByCaseId(@PathVariable Long caseId) {
		List<CommentDTO> result = commentService.getCaseComments(caseId, "ASC");
		return ResponseEntity.ok(new ApiResponse<>(true, "Comments fetched!", result));
	}

	@PostMapping("/event")
	public ResponseEntity<ApiResponse<CommentDTO>> createEventComment(@RequestBody EventCommentCreateRequest request,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		CommentDTO created = commentService.createEventComment(request, currentUser);
		return ResponseEntity.ok(new ApiResponse<>(true, "留言新增成功", created));
	}

	@PostMapping("/event/search")
	public ResponseEntity<ApiResponse<List<CommentDTO>>> getDistinctCommentsByEventIds(
			@RequestBody EventCommentSearchRequest req) {
		List<CommentDTO> result = commentService.getDistinctCommentsByEventIds(req.getEventIds(), req.getOrder());
		return ResponseEntity.ok(new ApiResponse<>(true, "查詢成功", result));
	}

	@PatchMapping("/{commentId}")
	public ResponseEntity<ApiResponse<CommentDTO>> updateComment(@PathVariable Long commentId,
			@RequestBody CommentUpdateRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
		CommentDTO updated = commentService.updateComment(commentId, request.getContent(), currentUser);
		return ResponseEntity.ok(new ApiResponse<>(true, "留言更新成功", updated));
	}
}
