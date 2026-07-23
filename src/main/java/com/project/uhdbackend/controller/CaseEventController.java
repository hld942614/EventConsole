package com.project.uhdbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uhdbackend.dto.ApiResponse;
import com.project.uhdbackend.dto.CaseEventBatchRequest;
import com.project.uhdbackend.dto.CaseEventDeleteRequest;
import com.project.uhdbackend.service.CaseEventService;

@RestController
@RequestMapping("/api/v1/case-event")
public class CaseEventController {

	private final CaseEventService caseEventService;

	public CaseEventController(CaseEventService caseEventService) {
		this.caseEventService = caseEventService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> addEventsToCase(@RequestBody CaseEventBatchRequest request) {
		caseEventService.addEventsToCase(request.getCaseId(), request.getEventIds(), request.getAssignedTo());
		return ResponseEntity.ok(new ApiResponse<>(true, "Batch insert completed.", null));
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<Void>> deleteCaseEvents(@RequestBody CaseEventDeleteRequest req) {
		caseEventService.removeCaseEvents(req.getCaseId(), req.getEventIds());
		return ResponseEntity.ok(new ApiResponse<>(true, "Deleted successfully.", null));
	}
}