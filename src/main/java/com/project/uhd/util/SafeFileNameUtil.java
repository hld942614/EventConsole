package com.project.uhd.util;

import java.nio.file.Path;

/**
 * 上傳/下載共用的檔名與路徑安全檢查。
 */
public final class SafeFileNameUtil {

	private SafeFileNameUtil() {
	}

	/** 檔名/alertCode 等單一路徑片段的檢查，不可包含跳脫路徑用的字元 */
	public static void validateSegment(String value, String fieldName) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(fieldName + " 不可為空");
		}
		if (value.contains("..") || value.contains("/") || value.contains("\\")) {
			throw new IllegalArgumentException(fieldName + " 包含不合法字元: " + value);
		}
	}

	/** 組出目標路徑後，再次確認最終路徑仍落在預期的根目錄底下（雙重保險） */
	public static Path resolveSafely(Path baseDir, String fileName) {
		Path target = baseDir.resolve(fileName).normalize();
		if (!target.startsWith(baseDir.normalize())) {
			throw new IllegalArgumentException("非法的檔案路徑: " + fileName);
		}
		return target;
	}
}
