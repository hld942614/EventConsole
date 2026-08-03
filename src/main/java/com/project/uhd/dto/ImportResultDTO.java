package com.project.uhd.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel 匯入結果。無效的列不會讓整個匯入失敗，而是記錄在 errors 裡， 這跟 UHD Console Event 用
 * VALIDATION_ERROR_MESSAGE 保留無效紀錄的作法一致： 讓使用者事後看得到「哪一列、為什麼失敗」，而不是整批 rollback
 * 或悄悄跳過。
 */
public class ImportResultDTO {

	private int totalRows;
	private int successCount;
	private int failureCount;
	private final List<RowError> errors = new ArrayList<>();

	public void addSuccess() {
		totalRows++;
		successCount++;
	}

	public void addFailure(int rowNo, String assetHint, String message) {
		totalRows++;
		failureCount++;
		errors.add(new RowError(rowNo, assetHint, message));
	}

	public int getTotalRows() {
		return totalRows;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public int getFailureCount() {
		return failureCount;
	}

	public List<RowError> getErrors() {
		return errors;
	}

	public static class RowError {
		private final int rowNo;
		private final String assetHint;
		private final String message;

		public RowError(int rowNo, String assetHint, String message) {
			this.rowNo = rowNo;
			this.assetHint = assetHint;
			this.message = message;
		}

		public int getRowNo() {
			return rowNo;
		}

		public String getAssetHint() {
			return assetHint;
		}

		public String getMessage() {
			return message;
		}
	}
}
