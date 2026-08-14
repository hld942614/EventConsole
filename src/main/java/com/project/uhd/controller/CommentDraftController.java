package com.project.uhd.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uhd.authentication.CustomUserDetails;
import com.project.uhd.dto.ApiResponse;
import com.project.uhd.dto.CommentDraftDTO;
import com.project.uhd.dto.CommentDraftRequest;
import com.project.uhd.service.CommentDraftService;

@RestController
@RequestMapping("/api/v1/comments/draft")
public class CommentDraftController {

	private final CommentDraftService draftService;

	public CommentDraftController(CommentDraftService draftService) {
		this.draftService = draftService;
	}

	@PutMapping("/event/{eventPk}")
	public ResponseEntity<ApiResponse<Void>> saveEventDraft(@PathVariable Long eventPk,
			@RequestBody CommentDraftRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
		draftService.saveEventDraft(eventPk, request.getContent(), currentUser);
		return ResponseEntity.ok(new ApiResponse<>(true, "草稿已儲存", null));
	}

	@GetMapping("/event/{eventPk}")
	public ResponseEntity<ApiResponse<CommentDraftDTO>> getEventDraft(@PathVariable Long eventPk,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		return draftService.getEventDraft(eventPk, currentUser)
				.map(d -> ResponseEntity.ok(new ApiResponse<>(true, "草稿查詢成功", new CommentDraftDTO(d))))
				.orElse(ResponseEntity.ok(new ApiResponse<>(true, "無草稿", null)));
	}

	@DeleteMapping("/event/{eventPk}")
	public ResponseEntity<ApiResponse<Void>> deleteEventDraft(@PathVariable Long eventPk,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		draftService.deleteEventDraft(eventPk, currentUser);
		return ResponseEntity.ok(new ApiResponse<>(true, "草稿已刪除", null));
	}

	@PutMapping("/case/{caseId}")
	public ResponseEntity<ApiResponse<Void>> saveCaseDraft(@PathVariable Long caseId,
			@RequestBody CommentDraftRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
		draftService.saveCaseDraft(caseId, request.getContent(), currentUser);
		return ResponseEntity.ok(new ApiResponse<>(true, "草稿已儲存", null));
	}

	@GetMapping("/case/{caseId}")
	public ResponseEntity<ApiResponse<CommentDraftDTO>> getCaseDraft(@PathVariable Long caseId,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		return draftService.getCaseDraft(caseId, currentUser)
				.map(d -> ResponseEntity.ok(new ApiResponse<>(true, "草稿查詢成功", new CommentDraftDTO(d))))
				.orElse(ResponseEntity.ok(new ApiResponse<>(true, "無草稿", null)));
	}

	@DeleteMapping("/case/{caseId}")
	public ResponseEntity<ApiResponse<Void>> deleteCaseDraft(@PathVariable Long caseId,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		draftService.deleteCaseDraft(caseId, currentUser);
		return ResponseEntity.ok(new ApiResponse<>(true, "草稿已刪除", null));
	}
}
