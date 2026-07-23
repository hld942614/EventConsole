package com.project.uhdbackend.entity;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.project.uhdbackend.enums.IpStatus;
import com.project.uhdbackend.enums.IpType;

@Entity
@Table(name = "IPAM_IP_ADDRESS")
public class IpamIpAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "IP_ID", nullable = false, unique = true, length = 80)
	private String ipId;

	@Column(name = "SUBNET_ID", length = 80)
	private String subnetId;

	@Column(name = "IP_ADDRESS", nullable = false, unique = true, length = 50)
	private String ipAddress;

	@Enumerated(EnumType.STRING)
	@Column(name = "IP_TYPE", nullable = false, length = 30)
	private IpType ipType = IpType.USER_LAN;

	@Enumerated(EnumType.STRING)
	@Column(name = "IP_STATUS", nullable = false, length = 30)
	private IpStatus ipStatus = IpStatus.USED;

	@Column(name = "ASSET_ID", length = 80)
	private String assetId;

	@Column(name = "SERVER_NAME", length = 100)
	private String serverName;

	@Column(name = "SYSTEM_CODE", length = 100)
	private String systemCode;

	@Column(name = "DNS_NAME", length = 200)
	private String dnsName;

	@Column(name = "MAC_ADDRESS", length = 50)
	private String macAddress;

	@Column(name = "ENVIRONMENT", length = 30)
	private String environment;

	@Column(name = "LOCATION", length = 200)
	private String location;

	@Column(name = "DESCRIPTION", length = 500)
	private String description;

	@Column(name = "SOURCE_SYSTEM", length = 100)
	private String sourceSystem = "EXCEL";

	@Column(name = "SOURCE_ROW_NO")
	private Long sourceRowNo;

	@Column(name = "LAST_SYNC_AT")
	private OffsetDateTime lastSyncAt;

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

	public String getIpId() {
		return ipId;
	}

	public void setIpId(String ipId) {
		this.ipId = ipId;
	}

	public String getSubnetId() {
		return subnetId;
	}

	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public IpType getIpType() {
		return ipType;
	}

	public void setIpType(IpType ipType) {
		this.ipType = ipType;
	}

	public IpStatus getIpStatus() {
		return ipStatus;
	}

	public void setIpStatus(IpStatus ipStatus) {
		this.ipStatus = ipStatus;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getDnsName() {
		return dnsName;
	}

	public void setDnsName(String dnsName) {
		this.dnsName = dnsName;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public Long getSourceRowNo() {
		return sourceRowNo;
	}

	public void setSourceRowNo(Long sourceRowNo) {
		this.sourceRowNo = sourceRowNo;
	}

	public OffsetDateTime getLastSyncAt() {
		return lastSyncAt;
	}

	public void setLastSyncAt(OffsetDateTime lastSyncAt) {
		this.lastSyncAt = lastSyncAt;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}
}
