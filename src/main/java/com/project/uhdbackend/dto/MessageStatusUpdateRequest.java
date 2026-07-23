package com.project.uhdbackend.dto;

import com.project.uhdbackend.enums.MessageStatus;

public class MessageStatusUpdateRequest {
    private Long messageId;
    private MessageStatus status;

    public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}
