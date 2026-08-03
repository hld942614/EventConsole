package com.project.uhd.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uhd.dto.ApiResponse;
import com.project.uhd.dto.CaseCloseRequest;
import com.project.uhd.dto.CaseCreateRequest;
import com.project.uhd.dto.CaseDTO;
import com.project.uhd.dto.CaseResolveRequest;
import com.project.uhd.dto.CaseUpdateRequest;
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
	public ResponseEntity<ApiResponse<CaseDTO>> addCase(@RequestBody CaseCreateRequest request) {
		CaseDTO savedCase = caseService.addCase(request);
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

//    @GetMapping("/{caseId}/messages")
//    public ResponseEntity<ApiResponse<Set<MessageDTO>>> getMessagesByCase(@PathVariable Long caseId) {
//        Set<Message> messages = caseService.getMessagesByCaseId(caseId);
//        Set<MessageDTO> dtos = messages.stream()
//                                       .map(MessageDTO::new)
//                                       .collect(Collectors.toSet());
//        return ResponseEntity.ok(new ApiResponse<>(true, "Messages fetched successfully.", dtos));
//    }

//    @PostMapping("/{caseId}/messages/{messageId}")
//    public ResponseEntity<ApiResponse<CaseDTO>> addMessageToCase(
//            @PathVariable Long caseId,
//            @PathVariable Long messageId) {
//        Case updatedCase = caseService.addMessageToCase(caseId, messageId);
//        return ResponseEntity.ok(new ApiResponse<>(true, "Message added to case.", new CaseDTO(updatedCase)));
//    }

//    @DeleteMapping("/{caseId}/messages/{messageId}")
//    public ResponseEntity<ApiResponse<CaseDTO>> removeMessageFromCase(
//            @PathVariable Long caseId,
//            @PathVariable Long messageId) {
//        Case updatedCase = caseService.removeMessageFromCase(caseId, messageId);
//        return ResponseEntity.ok(new ApiResponse<>(true, "Message removed from group.", new CaseDTO(updatedCase)));
//    }

//    @GetMapping("/messages/{messageId}/cases")
//    public ResponseEntity<ApiResponse<Set<CaseDTO>>> getCasesByMessage(@PathVariable Long messageId) {
//        Set<Case> cases = caseService.getCasesByMessageId(messageId);
//        Set<CaseDTO> dtos = cases.stream()
//                                   .map(CaseDTO::new)
//                                   .collect(Collectors.toSet());
//        return ResponseEntity.ok(new ApiResponse<>(true, "Cases fetched successfully.", dtos));
//    }

//    @PostMapping("/{caseId}/messages")
//    public ResponseEntity<ApiResponse<CaseDTO>> addMessagesToCase(
//            @PathVariable Long caseId,
//            @RequestBody List<Long> messageIds) {
//        Case updatedCase = caseService.addMessagesToCase(caseId, messageIds);
//        return ResponseEntity.ok(new ApiResponse<>(true, "Messages added to case.", new CaseDTO(updatedCase)));
//    }

//    @GetMapping("/{caseId}/comments")
//    public ResponseEntity<ApiResponse<List<CommentDTO>>> getCaseComments(
//            @PathVariable Long caseId,
//            @RequestParam(defaultValue = "asc") String order) {
//        
//        List<CommentDTO> data = commentService.getCaseComments(caseId,order);
//        return ResponseEntity.ok(new ApiResponse<>(true, "Comments fetched!", data));
//    }

//    @PutMapping("/messages/status")
//    public ResponseEntity<ApiResponse<String>> updateCaseMsgStatus(@RequestBody CaseStatusUpdateRequest request){
//    	Long caseId = request.getCaseId();
//    	final MessageStatus targetStatus;
//        try {
//            targetStatus = MessageStatus.valueOf(request.getStatus().trim().toUpperCase());
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest()
//                    .body(new ApiResponse<>(false, "Invalid status: " + request.getStatus(), null));
//        } 
//    	caseService.updateCaseStatus(caseId,targetStatus);
//    	return ResponseEntity.ok(new ApiResponse<>(true, "Case status change successfully", null));
//    }

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
			@RequestBody CaseResolveRequest request) {
		try {
			CaseDTO dto = caseService.resolveCase(caseId, request.getResolvedBy());
			return ResponseEntity.ok(new ApiResponse<>(true, "Case resolved", dto));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage(), null));
		}
	}

	@PatchMapping("/{caseId}/close")
	public ResponseEntity<ApiResponse<CaseDTO>> closeCase(@PathVariable Long caseId,
			@RequestBody CaseCloseRequest request) {
		try {
			CaseDTO dto = caseService.closeCase(caseId, request.getClosedBy());
			return ResponseEntity.ok(new ApiResponse<>(true, "Case closed", dto));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage(), null));
		}
	}
}
