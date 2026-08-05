package com.project.uhd.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.uhd.authentication.CustomUserDetails;
import com.project.uhd.dto.ExportDataDTO;
import com.project.uhd.dto.FileExportDTO;
import com.project.uhd.dto.StorageResult;
import com.project.uhd.dto.UploadFileRequest;
import com.project.uhd.entity.UploadedFile;
import com.project.uhd.repository.UploadedFileRepository;

@Service
public class UploadedFileService {

	private final UploadedFileRepository repository;
	private final SopFileStorageService storageService;

	public UploadedFileService(UploadedFileRepository repository, SopFileStorageService storageService) {
		this.repository = repository;
		this.storageService = storageService;
	}

	public List<UploadedFile> getFilesByAlertCode(String alertCode) {
		return repository.findByAlertCodeOrderByTimestampDesc(alertCode);
	}

	/**
	 * 上傳 SOP PDF：落地檔名一律用 UUID 產生，允許同名檔案重複上傳而不互相覆蓋。 先落地檔案，再寫 DB metadata；DB
	 * 寫入失敗時清掉剛落地的檔案，避免孤兒檔案。
	 */
	public void uploadFile(UploadFileRequest form, CustomUserDetails currentUser) {
		String alertCode = form.getAlertCode();
		if (alertCode == null || alertCode.isBlank()) {
			throw new IllegalArgumentException("alertCode 不可為空");
		}

		MultipartFile multipartFile = form.getFile();
		if (multipartFile == null || multipartFile.isEmpty()) {
			throw new IllegalArgumentException("file 不可為空");
		}

		String displayFileName = (form.getFileName() != null && !form.getFileName().isBlank()) ? form.getFileName()
				: multipartFile.getOriginalFilename();

		StorageResult stored;
		try {
			stored = storageService.store(alertCode, displayFileName, multipartFile);
		} catch (IOException e) {
			throw new IllegalStateException("上傳失敗：" + e.getMessage(), e);
		}

		try {
			UploadedFile entity = new UploadedFile();
			entity.setAlertCode(alertCode);
			entity.setDescription(form.getDescription());
			entity.setUser(currentUser.getChineseName());
			entity.setUserId(currentUser.getId());
			entity.setFileName(stored.getOriginalFileName());
			entity.setStoredFileName(stored.getStoredFileName());
			repository.save(entity);
		} catch (RuntimeException e) {
			storageService.deleteQuietly(stored.getPath());
			throw e;
		}
	}

	/** 依 fileId 匯出檔案內容（base64）+ 描述；下載時顯示原始檔名，跟磁碟上的 UUID 落地檔名無關 */
	public ExportDataDTO exportFile(Long fileId) {
		UploadedFile uploadedFile = repository.findById(fileId)
				.orElseThrow(() -> new NoSuchElementException("找不到對應的檔案描述: " + fileId));

		byte[] pdfBytes;
		try {
			pdfBytes = storageService.read(uploadedFile.getAlertCode(), uploadedFile.getStoredFileName());
		} catch (IOException e) {
			throw new IllegalStateException("讀取檔案時發生錯誤: " + e.getMessage(), e);
		}

		String base64 = Base64.getEncoder().encodeToString(pdfBytes);
		FileExportDTO fileDto = new FileExportDTO(uploadedFile.getFileName(), base64);

		ExportDataDTO data = new ExportDataDTO();
		data.setDescription(uploadedFile.getDescription());
		data.setFile(fileDto);
		return data;
	}

	/**
	 * 刪除檔案：先刪 DB 紀錄（可於失敗時 rollback），成功後才刪實體檔案。 若實體檔案刪除失敗，拋例外讓整個交易 rollback，維持 DB
	 * 與磁碟狀態一致。
	 */
	@Transactional
	public void deleteFile(Long id) {
		UploadedFile uploadedFile = repository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("File not found: " + id));

		repository.deleteById(id);

		try {
			storageService.delete(uploadedFile.getAlertCode(), uploadedFile.getStoredFileName());
		} catch (IOException e) {
			throw new IllegalStateException("DB 紀錄已刪除，但實體檔案刪除失敗：" + e.getMessage(), e);
		}
	}

	@Transactional
	public void updateFileDescription(Long id, String description) {
		UploadedFile file = repository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("File not found: " + id));
		file.setDescription(description);
	}
}
