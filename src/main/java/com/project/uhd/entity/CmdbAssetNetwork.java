package com.project.uhd.entity;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CMDB_ASSET_NETWORK")
public class CmdbAssetNetwork {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "ASSET_ID", nullable = false, length = 80)
	private String assetId;

	@Column(name = "IP_ID", length = 80)
	private String ipId;

	@Column(name = "IP_ADDRESS", nullable = false, length = 50)
	private String ipAddress;

	@Column(name = "DNS_NAME", length = 200)
	private String dnsName;

	@Column(name = "IS_PRIMARY", nullable = false, length = 1)
	private String isPrimary = "N";

	@Column(name = "DESCRIPTION", length = 300)
	private String description;

	@Column(name = "CREATED_AT", insertable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(name = "UPDATED_AT", insertable = false, updatable = false)
	private OffsetDateTime updatedAt;

	@Column(name = "INTERFACE_NAME", length = 100)
	private String interfaceName;

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

	public String getIpId() {
		return ipId;
	}

	public void setIpId(String ipId) {
		this.ipId = ipId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDnsName() {
		return dnsName;
	}

	public void setDnsName(String dnsName) {
		this.dnsName = dnsName;
	}

	public String getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
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

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
