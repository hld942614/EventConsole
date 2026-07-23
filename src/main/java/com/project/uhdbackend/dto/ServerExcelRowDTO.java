package com.project.uhdbackend.dto;

import cn.idev.excel.annotation.ExcelProperty;

public class ServerExcelRowDTO {

    // === ServerInfo ===
    @ExcelProperty("sec")
    private String sec;

    @ExcelProperty("Rack")
    private String rack;

    @ExcelProperty("U")
    private String rackU;

    @ExcelProperty("Env.")
    private String env;

    @ExcelProperty("System")
    private String system;

    @ExcelProperty("ServerName")
    private String serverName;

    @ExcelProperty("Model")
    private String model;

    @ExcelProperty("ServiceTag")
    private String serviceTag;

    @ExcelProperty("size(U)")
    private String sizeU;

    @ExcelProperty("Function")
    private String function;

    @ExcelProperty("OS")
    private String os;

    @ExcelProperty("remark")
    private String remark;

    // === HardwareSpec ===
    @ExcelProperty("CPU")
    private String cpuModel;

    @ExcelProperty("CORE數")
    private String coreCount;

    @ExcelProperty("HD大小")
    private String hdSize;

    @ExcelProperty("HD數量")
    private String hdCount;

    @ExcelProperty("記憶體大小(G)")
    private String memorySpec;

    @ExcelProperty("HBA卡")
    private String hbaCard;

    @ExcelProperty("網卡")
    private String nicCount;

    @ExcelProperty("電源")
    private String psuType;

    @ExcelProperty("電壓")
    private String voltage;

    @ExcelProperty("功耗")
    private String powerWatts;

    @ExcelProperty("電壓範圍")
    private String voltageRange;

    // === MaintenanceRecord ===
    @ExcelProperty("類型")
    private String largeType;

    @ExcelProperty("報修廠商")
    private String maintVendor;

    @ExcelProperty("維護起始日")
    private String maintStart;

    @ExcelProperty("維護到期日")
    private String maintEnd;

    @ExcelProperty("維護類型")
    private String maintType;

    // === NetworkInfo ===
    @ExcelProperty("IP(UserLan)")
    private String ipUserlan;

    @ExcelProperty("BK-IP")
    private String bkIp;

    @ExcelProperty("iDrac IP")
    private String iDRACIp;

    // === Asset ===
    @ExcelProperty("財產編號")
    private String assetNo;

    @ExcelProperty("取得日期")
    private String acquiredDate;
    
    @ExcelProperty("取得成本")
    private String acquisitionCost;

    @ExcelProperty("保管單位")
    private String custodianUnit;

    // === MtaGroup ===

    @ExcelProperty("MTA群組")
    private String mtaGroupCode;

    @ExcelProperty("MTA群組名稱")
    private String mtaGroupName;

    @ExcelProperty("MTA使用者")
    private String mtaUser;

	public String getSec() {
		return sec;
	}

	public void setSec(String sec) {
		this.sec = sec;
	}

	public String getRack() {
		return rack;
	}

	public void setRack(String rack) {
		this.rack = rack;
	}

	public String getRackU() {
		return rackU;
	}

	public void setRackU(String rackU) {
		this.rackU = rackU;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
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

	public String getSizeU() {
		return sizeU;
	}

	public void setSizeU(String sizeU) {
		this.sizeU = sizeU;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCpuModel() {
		return cpuModel;
	}

	public void setCpuModel(String cpuModel) {
		this.cpuModel = cpuModel;
	}

	public String getCoreCount() {
		return coreCount;
	}

	public void setCoreCount(String coreCount) {
		this.coreCount = coreCount;
	}

	public String getHdSize() {
		return hdSize;
	}

	public void setHdSize(String hdSize) {
		this.hdSize = hdSize;
	}

	public String getHdCount() {
		return hdCount;
	}

	public void setHdCount(String hdCount) {
		this.hdCount = hdCount;
	}

	public String getMemorySpec() {
		return memorySpec;
	}

	public void setMemorySpec(String memorySpec) {
		this.memorySpec = memorySpec;
	}

	public String getHbaCard() {
		return hbaCard;
	}

	public void setHbaCard(String hbaCard) {
		this.hbaCard = hbaCard;
	}

	public String getNicCount() {
		return nicCount;
	}

	public void setNicCount(String nicCount) {
		this.nicCount = nicCount;
	}

	public String getPsuType() {
		return psuType;
	}

	public void setPsuType(String psuType) {
		this.psuType = psuType;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	public String getPowerWatts() {
		return powerWatts;
	}

	public void setPowerWatts(String powerWatts) {
		this.powerWatts = powerWatts;
	}

	public String getVoltageRange() {
		return voltageRange;
	}

	public void setVoltageRange(String voltageRange) {
		this.voltageRange = voltageRange;
	}

	public String getLargeType() {
		return largeType;
	}

	public void setLargeType(String largeType) {
		this.largeType = largeType;
	}

	public String getMaintVendor() {
		return maintVendor;
	}

	public void setMaintVendor(String maintVendor) {
		this.maintVendor = maintVendor;
	}

	public String getMaintStart() {
		return maintStart;
	}

	public void setMaintStart(String maintStart) {
		this.maintStart = maintStart;
	}

	public String getMaintEnd() {
		return maintEnd;
	}

	public void setMaintEnd(String maintEnd) {
		this.maintEnd = maintEnd;
	}

	public String getMaintType() {
		return maintType;
	}

	public void setMaintType(String maintType) {
		this.maintType = maintType;
	}

	public String getIpUserlan() {
		return ipUserlan;
	}

	public void setIpUserlan(String ipUserlan) {
		this.ipUserlan = ipUserlan;
	}

	public String getBkIp() {
		return bkIp;
	}

	public void setBkIp(String bkIp) {
		this.bkIp = bkIp;
	}

	public String getIDRACIp() {
		return iDRACIp;
	}

	public void setIDRACIp(String iDRACIp) {
		this.iDRACIp = iDRACIp;
	}

	public String getAssetNo() {
		return assetNo;
	}

	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}

	public String getAcquiredDate() {
		return acquiredDate;
	}

	public void setAcquiredDate(String acquiredDate) {
		this.acquiredDate = acquiredDate;
	}

	public String getCustodianUnit() {
		return custodianUnit;
	}

	public void setCustodianUnit(String custodianUnit) {
		this.custodianUnit = custodianUnit;
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
}
