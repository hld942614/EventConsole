package com.project.uhdbackend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.multipart.MultipartFile;

import com.project.uhdbackend.dto.ApiResponse;
import com.project.uhdbackend.dto.ExportDataDTO;
import com.project.uhdbackend.dto.FileExportDTO;
import com.project.uhdbackend.dto.UploadFileRequest;
import com.project.uhdbackend.dto.UploadFileUpdateRequest;
import com.project.uhdbackend.entity.UploadedFile;
import com.project.uhdbackend.service.UploadedFileService;

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

		String alertCode = form.getAlertCode();
		if (alertCode == null || alertCode.isBlank()) {
			return ResponseEntity.ok(new ApiResponse<>(false, "alertCode 不可為空", null));
		}

		MultipartFile multipartFile = form.getFile();
		if (multipartFile == null || multipartFile.isEmpty()) {
			return ResponseEntity.ok(new ApiResponse<>(false, "file 不可為空", null));
		}

		String fileName = (form.getFileName() != null && !form.getFileName().isBlank()) ? form.getFileName()
				: multipartFile.getOriginalFilename();

		if (fileName == null || fileName.isBlank()) {
			return ResponseEntity.ok(new ApiResponse<>(false, "檔名不可為空", null));
		}

		if (!fileName.toLowerCase().endsWith(".pdf")) {
			return ResponseEntity.ok(new ApiResponse<>(false, "只能上傳 PDF 檔案：" + fileName, null));
		}

		String userHome = System.getProperty("user.home");
		Path documentsDir = Paths.get(userHome, "UHD_Files", alertCode);

		try {
			Files.createDirectories(documentsDir);

			Path filePath = documentsDir.resolve(fileName);
			Files.write(filePath, multipartFile.getBytes(), StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);

			UploadedFile entity = new UploadedFile();
			entity.setAlertCode(alertCode);
			entity.setDescription(form.getDescription());
			entity.setUser(form.getUploadUser());
			entity.setFileName(fileName);

			service.save(entity);

			return ResponseEntity.ok(new ApiResponse<>(true, "上傳成功", null));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new ApiResponse<>(false, "上傳失敗：" + e.getMessage(), null));
		}
	}

	@GetMapping("/{fileId:\\d+}")
	public ResponseEntity<ApiResponse<ExportDataDTO>> getFileById(@PathVariable("fileId") Long fileId) {
		try {
			UploadedFile uploadedFile = service.getFileById(fileId);
			if (uploadedFile == null) {
				return ResponseEntity.ok(new ApiResponse<>(false, "找不到對應的檔案描述", null));
			}

			String alertCode = uploadedFile.getAlertCode();
			String fileName = uploadedFile.getFileName();
			String userHome = System.getProperty("user.home");

			Path documentsDir = Paths.get(userHome, "UHD_Files", alertCode);
			if (!Files.exists(documentsDir)) {
				return ResponseEntity.ok(new ApiResponse<>(false, "指定的資料夾不存在：" + documentsDir, null));
			}

			if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
				return ResponseEntity.ok(new ApiResponse<>(false, "非法的檔名", null));
			}

			Path target = documentsDir.resolve(fileName);
			if (!target.normalize().startsWith(documentsDir.normalize())) {
				return ResponseEntity.ok(new ApiResponse<>(false, "存取被拒絕", null));
			}

			if (!Files.exists(target) || !Files.isRegularFile(target)) {
				return ResponseEntity.ok(new ApiResponse<>(false, "找不到對應的 SOP 檔案：" + target, null));
			}

			byte[] pdfBytes = Files.readAllBytes(target);
			String base64 = Base64.getEncoder().encodeToString(pdfBytes);

			FileExportDTO fileDto = new FileExportDTO(target.getFileName().toString(), base64);

			ExportDataDTO data = new ExportDataDTO();
			data.setDescription(uploadedFile.getDescription());
			data.setFile(fileDto);

			return ResponseEntity.ok(new ApiResponse<>(true, "SOP 檔案匯出成功", data));

		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.ok(new ApiResponse<>(false, "讀取檔案時發生錯誤", null));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteFile(@PathVariable Long id) {
		UploadedFile fileOpt = service.getFileById(id);
		if (fileOpt == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "File not found", null));
		}
		try {
			service.deleteById(id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage(), null));
		}

		return ResponseEntity.ok(new ApiResponse<>(true, "File deleted successfully", null));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> updateFileDescription(@PathVariable Long id,
			@RequestBody UploadFileUpdateRequest request) {
		String description = request.getDescription();
		try {
			service.updateFileDescritpion(id, description);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage(), null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "File updated successfully", null));
	}
}
