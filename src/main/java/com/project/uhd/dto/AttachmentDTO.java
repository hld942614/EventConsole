package com.project.uhd.dto;

import com.project.uhd.entity.Attachment;

public class AttachmentDTO {

	private Long attachmentId;

	private String originalFileName;

	private String contentType;

	private Long fileSize;

	public AttachmentDTO(Attachment attachment) {

		this.attachmentId = attachment.getId();
		this.originalFileName = attachment.getOriginalFileName();
		this.contentType = attachment.getContentType();
		this.fileSize = attachment.getFileSize();
	}

	public Long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
}
