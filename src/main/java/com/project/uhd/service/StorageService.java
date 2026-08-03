package com.project.uhd.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.project.uhd.dto.StorageResult;

@Service
public class StorageService {

	private final String ATTACHEMENT_PATH = "UHD_attachment";

	/**
	 * 儲存附件
	 */
	public StorageResult storeAttachment(String eventId, InputStream inputStream, String fileName, String contentType)
			throws IOException {

		StorageResult result = new StorageResult();
		String userHome = System.getProperty("user.home");

		Path folder = Paths.get(userHome, ATTACHEMENT_PATH, eventId);

		Files.createDirectories(folder);

		String extension = getExtension(fileName);

		String storedFileName = UUID.randomUUID() + (extension.isBlank() ? "" : "." + extension);

		Path target = folder.resolve(storedFileName);

		MessageDigest digest;

		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (Exception e) {
			throw new IOException(e);
		}

		long size = 0;

		try (DigestInputStream dis = new DigestInputStream(inputStream, digest);
				OutputStream os = Files.newOutputStream(target)) {

			byte[] buffer = new byte[8192];

			int len;

			while ((len = dis.read(buffer)) != -1) {

				os.write(buffer, 0, len);

				size += len;
			}
		}
		result.setOriginalFileName(fileName);
		result.setStoredFileName(storedFileName);
		result.setPath(target.toString());
		result.setSize(size);

		return result;
	}

	/**
	 * 刪除附件
	 */
	public void delete(String path) throws IOException {
		Files.deleteIfExists(Paths.get(path));
	}

	/**
	 * 判斷檔案是否存在
	 */
	public boolean exists(String path) {
		return Files.exists(Paths.get(path));
	}

	/**
	 * 讀取附件
	 */
	public InputStream load(String path) throws IOException {
		return Files.newInputStream(Paths.get(path));
	}

	private String getExtension(String fileName) {

		if (fileName == null) {
			return "";
		}

		int index = fileName.lastIndexOf('.');

		if (index < 0) {
			return "";
		}

		return fileName.substring(index + 1);
	}

}