package com.project.uhd.dto;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;

/**
 * 對應「實體資產清單」Excel 一列的攤平 DTO（採用名稱對應，不依賴欄位順序）。 欄位順序：sec, Rack, U, Env., System,
 * ServerName, Model, ServiceTag, 類型, 報修廠商, size(U), CPU, CORE數, HD大小, HD數量,
 * 記憶體大小(G), HBA卡, 網卡, Function, IP(UserLan), BK-IP, iDrac IP, OS, 財產編號, 取得日期,
 * 保管單位, 維護起始日, 維護到期日, 維護類型, 電源, 電壓, 功耗, 電壓範圍, remark, 搬遷規劃, MTA群組, MTA群組名稱,
 * MTA使用者 —— 這是實際會拿到的表頭。
 *
 * VM 清單（Host/Sec/Env/Sys/Name/IP/Notes/Guest OS）欄位格式不同， 暫不在此 DTO
 * 涵蓋範圍內，之後有需要可以比照這個模式另外做一個 CmdbVmExcelRowDTO + CmdbVmExcelConverter。
 */
public class CmdbAssetExcelRowDTO {

	@ExcelProperty("sec")
	private String secCode;

	@ExcelProperty("Rack")
	private String rackNo;

	@ExcelProperty("U")
	private String uPosition;

	@ExcelProperty("Env.")
	private String environmentRaw;

	@ExcelProperty("System")
	private String systemCode;

	@ExcelProperty("ServerName")
	private String serverName;

	@ExcelProperty("Model")
	private String model;

	@ExcelProperty("ServiceTag")
	private String serviceTag;

	@ExcelProperty("類型")
	private String assetTypeRaw;

	@ExcelProperty("報修廠商")
	private String maintainVendor;

	@ExcelProperty("size(U)")
	private String sizeU;

	// ---- 硬體規格 ----

	@ExcelProperty("CPU")
	private String cpuModel;

	@ExcelProperty("CORE數")
	private String coreDesc;

	@ExcelProperty("HD大小")
	private String diskSize;

	@ExcelProperty("HD數量")
	private String diskCount;

	@ExcelProperty("記憶體大小(G)")
	private String memorySize;

	@ExcelProperty("HBA卡")
	private String hbaCard;

	@ExcelProperty("網卡")
	private String networkCard;

	@ExcelProperty("Function")
	private String functionDesc;

	// ---- 網路（一台設備可能有多個 IP，各自獨立欄位） ----

	@ExcelProperty("IP(UserLan)")
	private String userLanIp;

	@ExcelProperty("BK-IP")
	private String backupIp;

	@ExcelProperty("iDrac IP")
	private String idracIp;

	// ---- OS ----

	@ExcelProperty("OS")
	private String osName;

	@ExcelProperty("財產編號")
	private String assetNo;

	@ExcelProperty("取得日期")
	private String acquireDate;

	@ExcelProperty("保管單位")
	private String custodyDept;

	@ExcelProperty("維護起始日")
	private String maintainStartDate;

	@ExcelProperty("維護到期日")
	private String maintainEndDate;

	@ExcelProperty("維護類型")
	private String maintainType;

	@ExcelProperty("電源")
	private String powerSupply;

	@ExcelProperty("電壓")
	private String voltage;

	@ExcelProperty("功耗")
	private String powerConsumption;

	@ExcelProperty("電壓範圍")
	private String voltageRange;

	@ExcelProperty("remark")
	private String remark;

	@ExcelProperty("搬遷規劃")
	private String migrationPlan;

	@ExcelProperty("MTA群組")
	private String mtaGroupCode;

	@ExcelProperty("MTA群組名稱")
	private String mtaGroupName;

	@ExcelProperty("MTA使用者")
	private String mtaUser;

	// 匯入時由程式賦值，非 Excel 欄位：這一列在 Excel 原始的列號，方便追查
	@ExcelIgnore
	private Integer sourceRowNo;

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

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getServiceTag() {
		return serviceTag;
	}

	public void setServiceTag(String serviceTag) {
		this.serviceTag = serviceTag;
	}

	public String getAssetTypeRaw() {
		return assetTypeRaw;
	}

	public void setAssetTypeRaw(String assetTypeRaw) {
		this.assetTypeRaw = assetTypeRaw;
	}

	public String getMaintainVendor() {
		return maintainVendor;
	}

	public void setMaintainVendor(String maintainVendor) {
		this.maintainVendor = maintainVendor;
	}

	public String getSizeU() {
		return sizeU;
	}

	public void setSizeU(String sizeU) {
		this.sizeU = sizeU;
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

	public String getFunctionDesc() {
		return functionDesc;
	}

	public void setFunctionDesc(String functionDesc) {
		this.functionDesc = functionDesc;
	}

	public String getUserLanIp() {
		return userLanIp;
	}

	public void setUserLanIp(String userLanIp) {
		this.userLanIp = userLanIp;
	}

	public String getBackupIp() {
		return backupIp;
	}

	public void setBackupIp(String backupIp) {
		this.backupIp = backupIp;
	}

	public String getIdracIp() {
		return idracIp;
	}

	public void setIdracIp(String idracIp) {
		this.idracIp = idracIp;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getAssetNo() {
		return assetNo;
	}

	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}

	public String getAcquireDate() {
		return acquireDate;
	}

	public void setAcquireDate(String acquireDate) {
		this.acquireDate = acquireDate;
	}

	public String getCustodyDept() {
		return custodyDept;
	}

	public void setCustodyDept(String custodyDept) {
		this.custodyDept = custodyDept;
	}

	public String getMaintainStartDate() {
		return maintainStartDate;
	}

	public void setMaintainStartDate(String maintainStartDate) {
		this.maintainStartDate = maintainStartDate;
	}

	public String getMaintainEndDate() {
		return maintainEndDate;
	}

	public void setMaintainEndDate(String maintainEndDate) {
		this.maintainEndDate = maintainEndDate;
	}

	public String getMaintainType() {
		return maintainType;
	}

	public void setMaintainType(String maintainType) {
		this.maintainType = maintainType;
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

	public Integer getSourceRowNo() {
		return sourceRowNo;
	}

	public void setSourceRowNo(Integer sourceRowNo) {
		this.sourceRowNo = sourceRowNo;
	}
}
