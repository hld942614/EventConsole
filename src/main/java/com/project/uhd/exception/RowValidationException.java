package com.project.uhd.exception;

/**
 * 單一 Excel 列資料不合法時拋出。
 *
 * 註：CMDB_ASSET 這張表沒有像 MUHD_EVENT 那樣的 VALIDATION_ERROR_MESSAGE 欄位， 所以這裡不把無效列寫進
 * DB，而是讓 CmdbImportService 攔截後彙整進 ImportResultDTO.errors 回傳給呼叫端，錯誤一樣不會被吞掉。
 * 如果之後想比照 UHD Console 把無效列也存進 DB 方便追查， 可以幫 CMDB_ASSET 加一個
 * VALIDATION_ERROR_MESSAGE 欄位，做法會完全一致。
 */
public class RowValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RowValidationException(String message) {
		super(message);
	}
}
