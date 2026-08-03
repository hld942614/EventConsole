package com.project.uhd.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.uhd.dto.StorageResult;
import com.project.uhd.util.FileTypeValidator;
import com.project.uhd.util.SafeFileNameUtil;

/**
 * SOP PDF 檔案（UHD_Files/{alertCode}/{storedFileName}）的實體讀寫。 落地檔名一律為 UUID，允許同一個
 * alertCode 底下上傳多筆同名（顯示名稱相同）的檔案， 彼此在磁碟上是獨立檔案，不會互相覆蓋。原始檔名只存在 DB，不影響磁碟路徑。
 */
@Service
public class SopFileStorageService {

	private static final String ROOT_DIR = "UHD_Files";

	/** 儲存檔案，回傳落地結果（含原始檔名、UUID 落地檔名、絕對路徑、檔案大小） */
	public StorageResult store(String alertCode, String displayFileName, MultipartFile multipartFile)
			throws IOException {
		SafeFileNameUtil.validateSegment(alertCode, "alertCode");

		if (displayFileName == null || displayFileName.isBlank()) {
			throw new IllegalArgumentException("檔名不可為空");
		}

		// 副檔名驗證：對象是「實際上傳的檔案」，跟使用者輸入的顯示名稱完全無關
		String uploadedFileName = multipartFile.getOriginalFilename();
		if (uploadedFileName == null || !uploadedFileName.toLowerCase().endsWith(".pdf")) {
			throw new IllegalArgumentException("只能上傳 PDF 檔案：" + uploadedFileName);
		}

		// 內容驗證：確保檔案實際內容真的是 PDF，不是偽裝副檔名的檔案
		try (InputStream headerCheckStream = multipartFile.getInputStream()) {
			if (!FileTypeValidator.isPdf(headerCheckStream)) {
				throw new IllegalArgumentException("檔案內容不是有效的 PDF 格式：" + uploadedFileName);
			}
		}

		Path documentsDir = resolveAlertCodeDir(alertCode);
		Files.createDirectories(documentsDir);

		String storedFileName = UUID.randomUUID() + ".pdf";
		Path target = SafeFileNameUtil.resolveSafely(documentsDir, storedFileName);

		Files.write(target, multipartFile.getBytes(), StandardOpenOption.CREATE_NEW);

		StorageResult result = new StorageResult();
		result.setOriginalFileName(displayFileName); // 原樣保留，不做任何修改
		result.setStoredFileName(storedFileName);
		result.setPath(target.toString());
		result.setSize(multipartFile.getSize());
		return result;
	}

	/** 讀取檔案內容，用實體落地檔名（storedFileName） */
	public byte[] read(String alertCode, String storedFileName) throws IOException {
		Path target = resolveExistingFile(alertCode, storedFileName);
		return Files.readAllBytes(target);
	}

	/** 依實體落地檔名刪除；找不到就當作已經刪除，不視為錯誤 */
	public void delete(String alertCode, String storedFileName) throws IOException {
		SafeFileNameUtil.validateSegment(alertCode, "alertCode");
		SafeFileNameUtil.validateSegment(storedFileName, "檔名");

		Path documentsDir = resolveAlertCodeDir(alertCode);
		Path target = SafeFileNameUtil.resolveSafely(documentsDir, storedFileName);
		Files.deleteIfExists(target);
	}

	/** 依絕對路徑刪除（上傳流程中，DB 寫入失敗時清理剛落地的檔案用） */
	public void deleteQuietly(String absolutePath) {
		try {
			if (absolutePath != null) {
				Files.deleteIfExists(Paths.get(absolutePath));
			}
		} catch (IOException ignored) {
			// 清理動作失敗不應該掩蓋原始例外，僅放棄即可
		}
	}

	private Path resolveExistingFile(String alertCode, String storedFileName) throws IOException {
		SafeFileNameUtil.validateSegment(alertCode, "alertCode");
		SafeFileNameUtil.validateSegment(storedFileName, "檔名");

		Path documentsDir = resolveAlertCodeDir(alertCode);
		if (!Files.exists(documentsDir)) {
			throw new NoSuchElementException("指定的資料夾不存在：" + documentsDir);
		}

		Path target = SafeFileNameUtil.resolveSafely(documentsDir, storedFileName);
		if (!Files.exists(target) || !Files.isRegularFile(target)) {
			throw new NoSuchElementException("找不到對應的 SOP 檔案：" + target);
		}
		return target;
	}

	private Path resolveAlertCodeDir(String alertCode) {
		String userHome = System.getProperty("user.home");
		return Paths.get(userHome, ROOT_DIR, alertCode);
	}
}
