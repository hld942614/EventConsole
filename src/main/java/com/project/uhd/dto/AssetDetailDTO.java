package com.project.uhd.dto;

import java.time.LocalDate;
import java.util.List;

public class AssetDetailDTO {

	private AssetInfo asset;
	private HardwareInfo hardware;
	private OsInfo os;
	private List<NetworkInfo> networkList;
	private List<ApplicationInfo> applications;
	private List<VmSummary> vmList;
	private Integer vmCount;

	public AssetInfo getAsset() {
		return asset;
	}

	public void setAsset(AssetInfo asset) {
		this.asset = asset;
	}

	public HardwareInfo getHardware() {
		return hardware;
	}

	public void setHardware(HardwareInfo hardware) {
		this.hardware = hardware;
	}

	public OsInfo getOs() {
		return os;
	}

	public void setOs(OsInfo os) {
		this.os = os;
	}

	public List<NetworkInfo> getNetworkList() {
		return networkList;
	}

	public void setNetworkList(List<NetworkInfo> networkList) {
		this.networkList = networkList;
	}

	public List<ApplicationInfo> getApplications() {
		return applications;
	}

	public void setApplications(List<ApplicationInfo> applications) {
		this.applications = applications;
	}

	public List<VmSummary> getVmList() {
		return vmList;
	}

	public void setVmList(List<VmSummary> vmList) {
		this.vmList = vmList;
	}

	public Integer getVmCount() {
		return vmCount;
	}

	public void setVmCount(Integer vmCount) {
		this.vmCount = vmCount;
	}

	public static class AssetInfo {
		private String assetId;
		private String assetCode;
		private String assetName;
		private String serverName;
		private String assetNo;
		private String serviceTag;
		private String assetType;
		private String assetTypeRaw;
		private String model;
		private String isVirtual;
		private String hostName;
		private String parentAssetId;
		private String environment;
		private String environmentRaw;
		private String systemCode;
		private String functionDesc;
		private String secCode;
		private String rackNo;
		private String uPosition;
		private String sizeU;
		private String custodyDept;
		private String mtaGroupCode;
		private String mtaGroupName;
		private String mtaUser;
		private LocalDate acquireDate;
		private String status;
		private String remark;
		private String migrationPlan;
		private String location;
		private String room;

		// getters / setters
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

		public String getAssetType() {
			return assetType;
		}

		public void setAssetType(String assetType) {
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

		public String getuPosition() {
			return uPosition;
		}

		public void setuPosition(String uPosition) {
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

		public LocalDate getAcquireDate() {
			return acquireDate;
		}

		public void setAcquireDate(LocalDate acquireDate) {
			this.acquireDate = acquireDate;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
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

	public static class HardwareInfo {
		private String cpuModel;
		private String coreDesc;
		private String diskSize;
		private String diskCount;
		private String memorySize;
		private String hbaCard;
		private String networkCard;
		private String powerSupply;
		private String voltage;
		private String powerConsumption;
		private String voltageRange;
		private String maintainVendor;
		private LocalDate maintainStartDate;
		private LocalDate maintainEndDate;
		private String maintainType;
		private String gpuModel;
		private Integer gpuCount;
		private String remark;

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

	public static class OsInfo {
		private String osName;
		private String osVersion;
		private String osFamily;
		private String isCurrent;
		private String remark;

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

		public String getIsCurrent() {
			return isCurrent;
		}

		public void setIsCurrent(String isCurrent) {
			this.isCurrent = isCurrent;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
	}

	public static class NetworkInfo {
		private String ipAddress;
		private String macAddress;
		private String dnsName;
		private String isPrimary;
		private String description;
		private String interfaceName;
		private String remark;

		public String getIpAddress() {
			return ipAddress;
		}

		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}

		public String getMacAddress() {
			return macAddress;
		}

		public void setMacAddress(String macAddress) {
			this.macAddress = macAddress;
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

	public static class ApplicationInfo {
		private String applicationCode;
		private String applicationName;
		private String relationType;

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

		public String getRelationType() {
			return relationType;
		}

		public void setRelationType(String relationType) {
			this.relationType = relationType;
		}
	}

	public static class VmSummary {
		private String assetId;
		private String host;
		private String sec;
		private String env;
		private String sys;
		private String name;
		private List<String> ipList;
		private String notes;
		private String guestOs;
		private String status;

		public String getAssetId() {
			return assetId;
		}

		public void setAssetId(String assetId) {
			this.assetId = assetId;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getSec() {
			return sec;
		}

		public void setSec(String sec) {
			this.sec = sec;
		}

		public String getEnv() {
			return env;
		}

		public void setEnv(String env) {
			this.env = env;
		}

		public String getSys() {
			return sys;
		}

		public void setSys(String sys) {
			this.sys = sys;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<String> getIpList() {
			return ipList;
		}

		public void setIpList(List<String> ipList) {
			this.ipList = ipList;
		}

		public String getNotes() {
			return notes;
		}

		public void setNotes(String notes) {
			this.notes = notes;
		}

		public String getGuestOs() {
			return guestOs;
		}

		public void setGuestOs(String guestOs) {
			this.guestOs = guestOs;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}
}