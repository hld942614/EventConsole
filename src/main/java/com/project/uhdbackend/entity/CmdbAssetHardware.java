package com.project.uhdbackend.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CMDB_ASSET_HARDWARE")
public class CmdbAssetHardware {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "ASSET_ID", nullable = false, unique = true, length = 80)
	private String assetId;

	@Column(name = "CPU_MODEL", length = 200)
	private String cpuModel;

	@Column(name = "CORE_DESC", length = 100)
	private String coreDesc;

	@Column(name = "DISK_SIZE", length = 100)
	private String diskSize;

	@Column(name = "DISK_COUNT")
	private String diskCount;

	@Column(name = "MEMORY_SIZE", length = 100)
	private String memorySize;

	@Column(name = "HBA_CARD", length = 100)
	private String hbaCard;

	@Column(name = "NETWORK_CARD", length = 100)
	private String networkCard;

	@Column(name = "POWER_SUPPLY", length = 50)
	private String powerSupply;

	@Column(name = "VOLTAGE", length = 50)
	private String voltage;

	@Column(name = "POWER_CONSUMPTION", length = 100)
	private String powerConsumption;

	@Column(name = "VOLTAGE_RANGE", length = 100)
	private String voltageRange;

	@Column(name = "MAINTAIN_VENDOR", length = 100)
	private String maintainVendor;

	@Column(name = "MAINTAIN_START_DATE")
	private LocalDate maintainStartDate;

	@Column(name = "MAINTAIN_END_DATE")
	private LocalDate maintainEndDate;

	@Column(name = "MAINTAIN_TYPE", length = 100)
	private String maintainType;

	@Column(name = "CREATED_AT", insertable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(name = "UPDATED_AT", insertable = false, updatable = false)
	private OffsetDateTime updatedAt;

	@Column(name = "GPU_MODEL", length = 200)
	private String gpuModel;

	@Column(name = "GPU_COUNT")
	private Integer gpuCount;

	@Column(name = "REMARK", length = 1000)
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

	public String getCpuModel() {
		return cpuModel;
	}

	public void setCpuModel(String cpuModel) {
		this.cpuModel = cpuModel;
	}

	public String getCoreDesc() {
		return coreDesc;
	}

	public void setCoreDesc(String coreDesc) {
		this.coreDesc = coreDesc;
	}

	public String getDiskSize() {
		return diskSize;
	}

	public void setDiskSize(String diskSize) {
		this.diskSize = diskSize;
	}

	public String getDiskCount() {
		return diskCount;
	}

	public void setDiskCount(String diskCount) {
		this.diskCount = diskCount;
	}

	public String getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(String memorySize) {
		this.memorySize = memorySize;
	}

	public String getHbaCard() {
		return hbaCard;
	}

	public void setHbaCard(String hbaCard) {
		this.hbaCard = hbaCard;
	}

	public String getNetworkCard() {
		return networkCard;
	}

	public void setNetworkCard(String networkCard) {
		this.networkCard = networkCard;
	}

	public String getPowerSupply() {
		return powerSupply;
	}

	public void setPowerSupply(String powerSupply) {
		this.powerSupply = powerSupply;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	public String getPowerConsumption() {
		return powerConsumption;
	}

	public void setPowerConsumption(String powerConsumption) {
		this.powerConsumption = powerConsumption;
	}

	public String getVoltageRange() {
		return voltageRange;
	}

	public void setVoltageRange(String voltageRange) {
		this.voltageRange = voltageRange;
	}

	public String getMaintainVendor() {
		return maintainVendor;
	}

	public void setMaintainVendor(String maintainVendor) {
		this.maintainVendor = maintainVendor;
	}

	public LocalDate getMaintainStartDate() {
		return maintainStartDate;
	}

	public void setMaintainStartDate(LocalDate maintainStartDate) {
		this.maintainStartDate = maintainStartDate;
	}

	public LocalDate getMaintainEndDate() {
		return maintainEndDate;
	}

	public void setMaintainEndDate(LocalDate maintainEndDate) {
		this.maintainEndDate = maintainEndDate;
	}

	public String getMaintainType() {
		return maintainType;
	}

	public void setMaintainType(String maintainType) {
		this.maintainType = maintainType;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}

	public String getGpuModel() {
		return gpuModel;
	}

	public void setGpuModel(String gpuModel) {
		this.gpuModel = gpuModel;
	}

	public Integer getGpuCount() {
		return gpuCount;
	}

	public void setGpuCount(Integer gpuCount) {
		this.gpuCount = gpuCount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
