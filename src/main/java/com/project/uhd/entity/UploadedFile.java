package com.project.uhd.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "muhd_uploaded_file")
@NoArgsConstructor
@AllArgsConstructor
public class UploadedFile {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uploadfile_id_gen")
	@SequenceGenerator(name = "uploadfile_id_gen", sequenceName = "uploadfile_id_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "alertcode")
	private String alertCode;

	@Column(name = "description")
	private String description;

	@Column(name = "upload_user")
	private String user;

	@Column(name = "filename")
	private String fileName;

	@Column(name = "stored_file_name")
	private String storedFileName;

	@Column(name = "createtime", insertable = false)
	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime timestamp;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlertCode() {
		return alertCode;
	}

	public void setAlertCode(String alertCode) {
		this.alertCode = alertCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStoredFileName() {
		return storedFileName;
	}

	public void setStoredFileName(String storedFileName) {
		this.storedFileName = storedFileName;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		jo.put("alertcode", alertCode);
		jo.put("user", user);
		jo.put("fileName", fileName);
		jo.put("description", description);
		jo.put("timestamp", timestamp);
		jo.put("storedFileName", storedFileName);
		return jo.toString();
	}

}
