package com.project.uhd.entity;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 網段主表。目前的 Excel 匯入流程不涉及網段資料（Excel 沒有對應欄位）， 先建好 entity/repository
 * 骨架，之後若要匯入或手動維護網段資料再擴充 service。
 */
@Entity
@Table(name = "IPAM_SUBNET")
public class IpamSubnet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "SUBNET_ID", nullable = false, unique = true, length = 80)
	private String subnetId;

	@Column(name = "SUBNET_NAME", length = 200)
	private String subnetName;

	@Column(name = "NETWORK_ADDRESS", nullable = false, length = 50)
	private String networkAddress;

	@Column(name = "CIDR", nullable = false)
	private Integer cidr;

	@Column(name = "NETMASK", length = 50)
	private String netmask;

	@Column(name = "GATEWAY", length = 50)
	private String gateway;

	@Column(name = "IP_VERSION", nullable = false, length = 10)
	private String ipVersion = "IPv4";

	@Column(name = "VLAN_ID", length = 50)
	private String vlanId;

	@Column(name = "VLAN_NAME", length = 200)
	private String vlanName;

	@Column(name = "ENVIRONMENT", length = 30)
	private String environment;

	@Column(name = "LOCATION", length = 200)
	private String location;

	@Column(name = "IDC_NAME", length = 100)
	private String idcName;

	@Column(name = "PURPOSE", length = 300)
	private String purpose;

	@Column(name = "OWNER_DEPT", length = 100)
	private String ownerDept;

	@Column(name = "OWNER_TEAM", length = 100)
	private String ownerTeam;

	@Column(name = "STATUS", nullable = false, length = 30)
	private String status = "ACTIVE";

	@Lob
	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "SOURCE_SYSTEM", length = 100)
	private String sourceSystem = "EXCEL";

	@Column(name = "SOURCE_SUBNET_ID", length = 100)
	private String sourceSubnetId;

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

	public String getSubnetId() {
		return subnetId;
	}

	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}

	public String getSubnetName() {
		return subnetName;
	}

	public void setSubnetName(String subnetName) {
		this.subnetName = subnetName;
	}

	public String getNetworkAddress() {
		return networkAddress;
	}

	public void setNetworkAddress(String networkAddress) {
		this.networkAddress = networkAddress;
	}

	public Integer getCidr() {
		return cidr;
	}

	public void setCidr(Integer cidr) {
		this.cidr = cidr;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getIpVersion() {
		return ipVersion;
	}

	public void setIpVersion(String ipVersion) {
		this.ipVersion = ipVersion;
	}

	public String getVlanId() {
		return vlanId;
	}

	public void setVlanId(String vlanId) {
		this.vlanId = vlanId;
	}

	public String getVlanName() {
		return vlanName;
	}

	public void setVlanName(String vlanName) {
		this.vlanName = vlanName;
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

	public String getIdcName() {
		return idcName;
	}

	public void setIdcName(String idcName) {
		this.idcName = idcName;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getOwnerDept() {
		return ownerDept;
	}

	public void setOwnerDept(String ownerDept) {
		this.ownerDept = ownerDept;
	}

	public String getOwnerTeam() {
		return ownerTeam;
	}

	public void setOwnerTeam(String ownerTeam) {
		this.ownerTeam = ownerTeam;
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

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getSourceSubnetId() {
		return sourceSubnetId;
	}

	public void setSourceSubnetId(String sourceSubnetId) {
		this.sourceSubnetId = sourceSubnetId;
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
