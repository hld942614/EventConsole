package com.project.uhd.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.uhd.authentication.CustomUserDetails;
import com.project.uhd.dto.ApiResponse;
import com.project.uhd.dto.CaseCreateRequest;
import com.project.uhd.dto.CaseDTO;
import com.project.uhd.dto.CaseUpdateRequest;
import com.project.uhd.dto.StatusLogDTO;
import com.project.uhd.entity.Case;
import com.project.uhd.service.CaseService;

@RestController
@RequestMapping("/api/v1/case")
public class CaseController {

	private final CaseService caseService;

	public CaseController(CaseService caseService) {
		this.caseService = caseService;
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse<CaseDTO>> addCase(@RequestBody CaseCreateRequest request,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		CaseDTO savedCase = caseService.addCase(request, currentUser);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Success.", savedCase));
	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<CaseDTO>>> getAllCases() {
		List<Case> cases = caseService.getAllCases();
		List<CaseDTO> dtos = cases.stream().map(CaseDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok(new ApiResponse<>(true, "All cases fetched.", dtos));
	}

	@GetMapping("/{caseId}")
	public ResponseEntity<ApiResponse<CaseDTO>> getCaseById(@PathVariable Long caseId) {
		Optional<CaseDTO> result = caseService.getCaseDtoById(caseId);
		if (result.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Case not found", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "Case fetched!", result.get()));
	}

	@PostMapping("/update")
	public ResponseEntity<ApiResponse<CaseDTO>> updateCase(@RequestBody CaseUpdateRequest request) {
		if (request.getId() == null) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "id is required", null));
		}

		Optional<Case> caseOpt = caseService.findById(request.getId());
		if (caseOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Case not found", null));
		}

		CaseDTO updated = caseService.updateCase(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Case updated successfully", updated));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteCase(@PathVariable Long id) {
		Optional<Case> caseOpt = caseService.findById(id);
		if (caseOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Case not found", null));
		}
		try {
			caseService.deleteCase(id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage(), null));
		}

		return ResponseEntity.ok(new ApiResponse<>(true, "Case deleted successfully", null));
	}

	@PatchMapping("/{caseId}/resolve")
	public ResponseEntity<ApiResponse<CaseDTO>> resolveCase(@PathVariable Long caseId,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		try {
			CaseDTO dto = caseService.resolveCase(caseId, currentUser);
			return ResponseEntity.ok(new ApiResponse<>(true, "Case resolved", dto));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage(), null));
		}
	}

	@PatchMapping("/{caseId}/close")
	public ResponseEntity<ApiResponse<CaseDTO>> closeCase(@PathVariable Long caseId,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		try {
			CaseDTO dto = caseService.closeCase(caseId, currentUser);
			return ResponseEntity.ok(new ApiResponse<>(true, "Case closed", dto));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage(), null));
		}
	}
	
	@GetMapping("/{caseId}/status-log")
	public ResponseEntity<ApiResponse<List<StatusLogDTO>>> getCaseStatusHistory(@PathVariable Long caseId,
			@RequestParam(value = "order", required = false, defaultValue = "asc") String order) {
		List<StatusLogDTO> history = caseService.getStatusHistory(caseId, order);
		return ResponseEntity.ok(new ApiResponse<>(true, "Status history fetched", history));
	}
}
