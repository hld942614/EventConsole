package com.project.uhd.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CMDB_ASSET_OS")
public class CmdbAssetOs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "ASSET_ID", nullable = false, length = 80)
	private String assetId;

	@Column(name = "OS_NAME", length = 200)
	private String osName;

	@Column(name = "OS_VERSION", length = 100)
	private String osVersion;

	@Column(name = "OS_FAMILY", length = 100)
	private String osFamily;

	@Column(name = "INSTALL_DATE")
	private LocalDate installDate;

	@Column(name = "EOL_DATE")
	private LocalDate eolDate;

	@Column(name = "IS_CURRENT", nullable = false, length = 1)
	private String isCurrent = "Y";

	@Column(name = "CREATED_AT", insertable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(name = "UPDATED_AT", insertable = false, updatable = false)
	private OffsetDateTime updatedAt;

	@Column(name = "REMARK", length = 500)
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getOsFamily() {
		return osFamily;
	}

	public void setOsFamily(String osFamily) {
		this.osFamily = osFamily;
	}

	public LocalDate getInstallDate() {
		return installDate;
	}

	public void setInstallDate(LocalDate installDate) {
		this.installDate = installDate;
	}

	public LocalDate getEolDate() {
		return eolDate;
	}

	public void setEolDate(LocalDate eolDate) {
		this.eolDate = eolDate;
	}

	public String getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(String isCurrent) {
		this.isCurrent = isCurrent;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
