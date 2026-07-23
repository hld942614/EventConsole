package com.project.uhdbackend.dto;

import java.io.InputStream;

public class AttachmentInfoDTO {
	/** 原始檔名 */
	private String fileName;

	/** MIME Type */
	private String contentType;

	/** 檔案大小(Byte) */
	private long size;

	/** 附件內容 */
	private InputStream inputStream;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
