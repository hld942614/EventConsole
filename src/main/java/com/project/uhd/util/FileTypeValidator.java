package com.project.uhd.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * 驗證檔案實際內容是否為 PDF，透過檢查檔頭 magic number（%PDF-）， 而非僅依賴使用者可任意竄改的副檔名。
 */
public final class FileTypeValidator {

	private static final byte[] PDF_MAGIC_NUMBER = { 0x25, 0x50, 0x44, 0x46, 0x2D }; // "%PDF-"

	private FileTypeValidator() {
	}

	public static boolean isPdf(InputStream inputStream) throws IOException {
		byte[] header = new byte[PDF_MAGIC_NUMBER.length];
		int bytesRead = inputStream.read(header);
		if (bytesRead < PDF_MAGIC_NUMBER.length) {
			return false;
		}
		for (int i = 0; i < PDF_MAGIC_NUMBER.length; i++) {
			if (header[i] != PDF_MAGIC_NUMBER[i]) {
				return false;
			}
		}
		return true;
	}
}
