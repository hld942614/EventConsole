package com.project.uhd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uhd.dto.ApiResponse;
import com.project.uhd.dto.ExportDataDTO;
import com.project.uhd.dto.UploadFileRequest;
import com.project.uhd.dto.UploadFileUpdateRequest;
import com.project.uhd.entity.UploadedFile;
import com.project.uhd.service.UploadedFileService;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {

	@Autowired
	private UploadedFileService service;

	@GetMapping("/alertcode/{alertCode}")
	public ResponseEntity<ApiResponse<List<UploadedFile>>> getFilesByAlertCode(@PathVariable String alertCode) {
		List<UploadedFile> files = service.getFilesByAlertCode(alertCode);
		return ResponseEntity.ok(new ApiResponse<>(true, "檔案取得成功", files));
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<Void>> uploadFile(@ModelAttribute UploadFileRequest form) {
		service.uploadFile(form);
		return ResponseEntity.ok(new ApiResponse<>(true, "上傳成功", null));
	}

	@GetMapping("/{fileId:\\d+}")
	public ResponseEntity<ApiResponse<ExportDataDTO>> getFileById(@PathVariable("fileId") Long fileId) {
		ExportDataDTO data = service.exportFile(fileId);
		return ResponseEntity.ok(new ApiResponse<>(true, "SOP 檔案匯出成功", data));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteFile(@PathVariable Long id) {
		service.deleteFile(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "File deleted successfully", null));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> updateFileDescription(@PathVariable Long id,
			@RequestBody UploadFileUpdateRequest request) {
		service.updateFileDescription(id, request.getDescription());
		return ResponseEntity.ok(new ApiResponse<>(true, "File updated successfully", null));
	}
}
