package com.project.uhdbackend.dto;

public class MessageGroupInsertResult {
    private Long messageId;
    private String status;
    private String reason;

    public MessageGroupInsertResult(Long messageId, String status, String reason) {
        this.messageId = messageId;
        this.status = status;
        this.reason = reason;
    }

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

    
}
