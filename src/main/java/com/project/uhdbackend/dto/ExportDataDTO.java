package com.project.uhdbackend.dto;

public class ExportDataDTO {
	private String description;
	private FileExportDTO file;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FileExportDTO getFile() {
		return file;
	}

	public void setFile(FileExportDTO file) {
		this.file = file;
	}
}
