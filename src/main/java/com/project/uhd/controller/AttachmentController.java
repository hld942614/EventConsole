package com.project.uhd.controller;

import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uhd.dto.ApiResponse;
import com.project.uhd.dto.AttachmentDTO;
import com.project.uhd.dto.AttachmentDownloadDTO;
import com.project.uhd.entity.Attachment;
import com.project.uhd.service.AttachmentService;

@RestController
@RequestMapping("/api/v1/attachments")
public class AttachmentController {

	private static final Logger log = LoggerFactory.getLogger(AttachmentController.class);

	private final AttachmentService attachmentService;

	public AttachmentController(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	/**
	 * 查詢 Event 所有附件
	 */
	@GetMapping("/event/{eventId}")
	public ResponseEntity<ApiResponse<List<AttachmentDTO>>> getAttachments(@PathVariable String eventId) {

		try {
			List<AttachmentDTO> attachments = attachmentService.getAttachments(eventId);

			return ResponseEntity.ok(new ApiResponse<>(true, "附件查詢成功", attachments));

		} catch (Exception e) {

			log.error("Get attachments failed. eventId={}", eventId, e);

			return ResponseEntity.ok(new ApiResponse<>(false, "附件查詢失敗", null));
		}
	}

	/**
	 * 下載附件
	 */
	@GetMapping("/{attachmentId}")
	public ResponseEntity<ApiResponse<AttachmentDownloadDTO>> downloadAttachment(@PathVariable Long attachmentId) {

		try {

			Attachment attachment = attachmentService.getAttachment(attachmentId);

			try (InputStream is = attachmentService.loadAttachment(attachment.getFilePath())) {

				byte[] bytes = is.readAllBytes();

				AttachmentDownloadDTO dto = new AttachmentDownloadDTO();
				dto.setFileName(attachment.getOriginalFileName());
				dto.setContentType(attachment.getContentType());
				dto.setBase64(Base64.getEncoder().encodeToString(bytes));

				return ResponseEntity.ok(new ApiResponse<>(true, "附件取得成功", dto));
			}

		} catch (Exception e) {

			log.error("Download attachment failed. attachmentId={}", attachmentId, e);

			return ResponseEntity.ok(new ApiResponse<>(false, "附件讀取失敗", null));
		}
	}

	/**
	 * 刪除附件
	 */
	@DeleteMapping("/{attachmentId}")
	public ResponseEntity<ApiResponse<Void>> deleteAttachment(@PathVariable Long attachmentId) {

		try {

			attachmentService.deleteAttachment(attachmentId);

			return ResponseEntity.ok(new ApiResponse<>(true, "附件刪除成功", null));

		} catch (Exception e) {

			log.error("Delete attachment failed. attachmentId={}", attachmentId, e);

			return ResponseEntity.ok(new ApiResponse<>(false, "附件刪除失敗", null));
		}
	}

}
