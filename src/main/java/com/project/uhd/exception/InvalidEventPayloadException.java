package com.project.uhd.exception;

/**
 * 新格式告警事件解析 / 驗證失敗時拋出。 例如：alertCode 缺失、environment 缺失、occurredAt 缺失、JSON 格式錯誤等。
 */
public class InvalidEventPayloadException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidEventPayloadException(String message) {
		super(message);
	}

	public InvalidEventPayloadException(String message, Throwable cause) {
		super(message, cause);
	}
}