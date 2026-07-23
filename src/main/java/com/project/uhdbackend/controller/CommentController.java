package com.project.uhdbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uhdbackend.dto.ApiResponse;
import com.project.uhdbackend.dto.CaseCommentCreateRequest;
import com.project.uhdbackend.dto.CommentDTO;
import com.project.uhdbackend.dto.EventCommentCreateRequest;
import com.project.uhdbackend.dto.EventCommentSearchRequest;
import com.project.uhdbackend.service.CommentService;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

//	@PostMapping("/message")
//	public ResponseEntity<ApiResponse<CommentDTO>> createMsgComment(@RequestBody MessageCommentCreateRequest request) {
//		CommentDTO created = commentService.createMsgComment(request);
//		return ResponseEntity.ok(new ApiResponse<>(true, "留言新增成功", created));
//	}

	@PostMapping("/case")
	public ResponseEntity<ApiResponse<CommentDTO>> createCaseComment(@RequestBody CaseCommentCreateRequest request) {
		CommentDTO created = commentService.createCaseComment(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "留言新增成功", created));
	}

//	@DeleteMapping("/{commentId}")
//	public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long commentId) {
//	    try {
//	        commentService.deleteCommentById(commentId);
//	        return ResponseEntity.ok(new ApiResponse<>(true, "刪除成功", null));
//	    } catch (NoSuchElementException e) {
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//	                .body(new ApiResponse<>(false, "找不到留言 ID：" + commentId, null));
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                .body(new ApiResponse<>(false, "刪除失敗：" + e.getMessage(), null));
//	    }
//	}

//	@PostMapping("/search")
//	public ResponseEntity<ApiResponse<List<CommentDTO>>> getDistinctCommentsByMessageIds(
//			@RequestBody CommentSearchRequest req) {
//		List<CommentDTO> result = commentService.getDistinctCommentsByMessageIds(req.getMessageIds(), req.getOrder());
//
//		return ResponseEntity.ok(new ApiResponse<>(true, "查詢成功", result));
//	}

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
}
