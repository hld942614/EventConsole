package com.project.uhdbackend.entity;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 應用系統主表。目前 Excel 匯入流程不涉及（Excel 沒有對應欄位）， 先建好骨架，之後若要用 SYSTEM_CODE 反查/建立
 * Application 再擴充 service。
 */
@Entity
@Table(name = "CMDB_APPLICATION")
public class CmdbApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "APPLICATION_ID", nullable = false, unique = true, length = 80)
	private String applicationId;

	@Column(name = "APPLICATION_CODE", nullable = false, length = 100)
	private String applicationCode;

	@Column(name = "APPLICATION_NAME", nullable = false, length = 200)
	private String applicationName;

	@Column(name = "ENVIRONMENT", length = 30)
	private String environment;

	@Column(name = "BUSINESS_OWNER_DEPT", length = 100)
	private String businessOwnerDept;

	@Column(name = "IT_OWNER_DEPT", length = 100)
	private String itOwnerDept;

	@Column(name = "IMPORTANCE_LEVEL", length = 30)
	private String importanceLevel;

	@Column(name = "SLA_LEVEL", length = 30)
	private String slaLevel;

	@Column(name = "STATUS", nullable = false, length = 30)
	private String status = "ACTIVE";

	@Lob
	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CREATED_AT", insertable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(name = "UPDATED_AT", insertable = false, updatable = false)
	private OffsetDateTime updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getBusinessOwnerDept() {
		return businessOwnerDept;
	}

	public void setBusinessOwnerDept(String businessOwnerDept) {
		this.businessOwnerDept = businessOwnerDept;
	}

	public String getItOwnerDept() {
		return itOwnerDept;
	}

	public void setItOwnerDept(String itOwnerDept) {
		this.itOwnerDept = itOwnerDept;
	}

	public String getImportanceLevel() {
		return importanceLevel;
	}

	public void setImportanceLevel(String importanceLevel) {
		this.importanceLevel = importanceLevel;
	}

	public String getSlaLevel() {
		return slaLevel;
	}

	public void setSlaLevel(String slaLevel) {
		this.slaLevel = slaLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}
}
