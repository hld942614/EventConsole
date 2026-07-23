package com.project.uhdbackend.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.project.uhdbackend.enums.AssetStatus;
import com.project.uhdbackend.enums.AssetType;

@Entity
@Table(name = "CMDB_ASSET")
public class CmdbAsset {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "ASSET_ID", nullable = false, unique = true, length = 80)
	private String assetId;

	@Column(name = "ASSET_CODE", length = 100)
	private String assetCode;

	@Column(name = "ASSET_NAME", length = 200)
	private String assetName;

	@Column(name = "SERVER_NAME", length = 100)
	private String serverName;

	@Column(name = "ASSET_NO", length = 100)
	private String assetNo;

	@Column(name = "SERVICE_TAG", length = 100)
	private String serviceTag;

	@Enumerated(EnumType.STRING)
	@Column(name = "ASSET_TYPE", nullable = false, length = 50)
	private AssetType assetType;

	@Column(name = "ASSET_TYPE_RAW", length = 100)
	private String assetTypeRaw;

	@Column(name = "MODEL", length = 200)
	private String model;

	@Column(name = "IS_VIRTUAL", nullable = false, length = 1)
	private String isVirtual = "N";

	@Column(name = "HOST_NAME", length = 100)
	private String hostName;

	@Column(name = "PARENT_ASSET_ID", length = 80)
	private String parentAssetId;

	@Column(name = "VIRTUALIZATION_TYPE", length = 50)
	private String virtualizationType;

	@Column(name = "ENVIRONMENT", length = 30)
	private String environment;

	@Column(name = "ENVIRONMENT_RAW", length = 50)
	private String environmentRaw;

	@Column(name = "SYSTEM_CODE", length = 100)
	private String systemCode;

	@Column(name = "SYSTEM_NAME", length = 200)
	private String systemName;

	@Column(name = "FUNCTION_DESC", length = 1000)
	private String functionDesc;

	@Column(name = "SEC_CODE", length = 50)
	private String secCode;

	@Column(name = "RACK_NO", length = 50)
	private String rackNo;

	@Column(name = "U_POSITION", length = 50)
	private String uPosition;

	@Column(name = "SIZE_U", length = 20)
	private String sizeU;

	@Column(name = "CUSTODY_DEPT", length = 100)
	private String custodyDept;

	@Column(name = "OWNER_DEPT", length = 100)
	private String ownerDept;

	@Column(name = "OWNER_TEAM", length = 100)
	private String ownerTeam;

	@Column(name = "MTA_GROUP_CODE", length = 50)
	private String mtaGroupCode;

	@Column(name = "MTA_GROUP_NAME", length = 200)
	private String mtaGroupName;

	@Column(name = "MTA_USER", length = 100)
	private String mtaUser;

	@Column(name = "IMPORTANCE_LEVEL", length = 30)
	private String importanceLevel;

	@Column(name = "SLA_LEVEL", length = 30)
	private String slaLevel;

	@Column(name = "ACQUIRE_DATE")
	private LocalDate acquireDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false, length = 30)
	private AssetStatus status = AssetStatus.ACTIVE;

	@Column(name = "REMARK", length = 1000)
	private String remark;

	@Column(name = "MIGRATION_PLAN", length = 200)
	private String migrationPlan;

	@Column(name = "SOURCE_SYSTEM", length = 100)
	private String sourceSystem = "EXCEL";

	@Column(name = "SOURCE_ASSET_ID", length = 100)
	private String sourceAssetId;

	@Column(name = "SOURCE_ROW_NO")
	private Long sourceRowNo;

	@Column(name = "LAST_SYNC_AT")
	private OffsetDateTime lastSyncAt;

	@Lob
	@Column(name = "RAW_DATA")
	private String rawData;

	@Column(name = "CREATED_AT", insertable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(name = "UPDATED_AT", insertable = false, updatable = false)
	private OffsetDateTime updatedAt;

	@Column(name = "LOCATION", length = 200)
	private String location;

	@Column(name = "ROOM", length = 100)
	private String room;

	// --- getters / setters ---

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

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getAssetNo() {
		return assetNo;
	}

	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}

	public String getServiceTag() {
		return serviceTag;
	}

	public void setServiceTag(String serviceTag) {
		this.serviceTag = serviceTag;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public String getAssetTypeRaw() {
		return assetTypeRaw;
	}

	public void setAssetTypeRaw(String assetTypeRaw) {
		this.assetTypeRaw = assetTypeRaw;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getIsVirtual() {
		return isVirtual;
	}

	public void setIsVirtual(String isVirtual) {
		this.isVirtual = isVirtual;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getParentAssetId() {
		return parentAssetId;
	}

	public void setParentAssetId(String parentAssetId) {
		this.parentAssetId = parentAssetId;
	}

	public String getVirtualizationType() {
		return virtualizationType;
	}

	public void setVirtualizationType(String virtualizationType) {
		this.virtualizationType = virtualizationType;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getEnvironmentRaw() {
		return environmentRaw;
	}

	public void setEnvironmentRaw(String environmentRaw) {
		this.environmentRaw = environmentRaw;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getFunctionDesc() {
		return functionDesc;
	}

	public void setFunctionDesc(String functionDesc) {
		this.functionDesc = functionDesc;
	}

	public String getSecCode() {
		return secCode;
	}

	public void setSecCode(String secCode) {
		this.secCode = secCode;
	}

	public String getRackNo() {
		return rackNo;
	}

	public void setRackNo(String rackNo) {
		this.rackNo = rackNo;
	}

	public String getUPosition() {
		return uPosition;
	}

	public void setUPosition(String uPosition) {
		this.uPosition = uPosition;
	}

	public String getSizeU() {
		return sizeU;
	}

	public void setSizeU(String sizeU) {
		this.sizeU = sizeU;
	}

	public String getCustodyDept() {
		return custodyDept;
	}

	public void setCustodyDept(String custodyDept) {
		this.custodyDept = custodyDept;
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

	public String getMtaGroupCode() {
		return mtaGroupCode;
	}

	public void setMtaGroupCode(String mtaGroupCode) {
		this.mtaGroupCode = mtaGroupCode;
	}

	public String getMtaGroupName() {
		return mtaGroupName;
	}

	public void setMtaGroupName(String mtaGroupName) {
		this.mtaGroupName = mtaGroupName;
	}

	public String getMtaUser() {
		return mtaUser;
	}

	public void setMtaUser(String mtaUser) {
		this.mtaUser = mtaUser;
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

	public LocalDate getAcquireDate() {
		return acquireDate;
	}

	public void setAcquireDate(LocalDate acquireDate) {
		this.acquireDate = acquireDate;
	}

	public AssetStatus getStatus() {
		return status;
	}

	public void setStatus(AssetStatus status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMigrationPlan() {
		return migrationPlan;
	}

	public void setMigrationPlan(String migrationPlan) {
		this.migrationPlan = migrationPlan;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getSourceAssetId() {
		return sourceAssetId;
	}

	public void setSourceAssetId(String sourceAssetId) {
		this.sourceAssetId = sourceAssetId;
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

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}
}
