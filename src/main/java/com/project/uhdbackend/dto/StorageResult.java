package com.project.uhdbackend.dto;

public class StorageResult {
	/** 儲存後的完整路徑 */
	private String path;

	/** 原始檔名 */
	private String originalFileName;

	/** 儲存檔名 */
	private String storedFileName;

	/** 實際檔案大小(Byte) */
	private long size;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getStoredFileName() {
		return storedFileName;
	}

	public void setStoredFileName(String storedFileName) {
		this.storedFileName = storedFileName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
