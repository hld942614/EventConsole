//package com.project.uhdbackend.dto;
//
//import com.project.uhdbackend.entity.ServerInfo;
//
//public class ServerListDTO {
//	private ServerInfoDTO serverInfo;
//	private HardwareSpecDTO hardwareSpec;
//	private MaintenanceRecordDTO maintenanceRecord;
//	private MtaGroupDTO mtaGroup;
//	private NetworkInfoDTO networkInfo;
//	private AssetDTO asset;
//
//	public ServerListDTO(ServerInfoDTO serverInfo, HardwareSpecDTO hardwareSpec,
//			MaintenanceRecordDTO maintenanceRecord, MtaGroupDTO mtaGroup, NetworkInfoDTO networkInfo, AssetDTO asset) {
//		super();
//		this.serverInfo = serverInfo;
//		this.hardwareSpec = hardwareSpec;
//		this.maintenanceRecord = maintenanceRecord;
//		this.mtaGroup = mtaGroup;
//		this.networkInfo = networkInfo;
//		this.asset = asset;
//	}
//
//	public ServerListDTO(ServerInfo s) {
//		this.serverInfo = new ServerInfoDTO(s);
//		this.hardwareSpec = (s.getHardwareSpec() == null) ? null : new HardwareSpecDTO(s.getHardwareSpec());
//		this.networkInfo = (s.getNetworkInfo() == null) ? null : new NetworkInfoDTO(s.getNetworkInfo());
//		this.maintenanceRecord = (s.getMaintenanceRecord() == null) ? null
//				: new MaintenanceRecordDTO(s.getMaintenanceRecord());
//		this.mtaGroup = (s.getMtaGroup() == null) ? null : new MtaGroupDTO(s.getMtaGroup());
//		this.asset = (s.getAsset() == null) ? null : new AssetDTO(s.getAsset());;
//	}
//
//	public ServerInfoDTO getServerInfo() {
//		return serverInfo;
//	}
//
//	public void setServerInfo(ServerInfoDTO serverInfo) {
//		this.serverInfo = serverInfo;
//	}
//
//	public HardwareSpecDTO getHardwareSpec() {
//		return hardwareSpec;
//	}
//
//	public void setHardwareSpec(HardwareSpecDTO hardwareSpec) {
//		this.hardwareSpec = hardwareSpec;
//	}
//
//	public MaintenanceRecordDTO getMaintenanceRecord() {
//		return maintenanceRecord;
//	}
//
//	public void setMaintenanceRecord(MaintenanceRecordDTO maintenanceRecord) {
//		this.maintenanceRecord = maintenanceRecord;
//	}
//
//	public MtaGroupDTO getMtaGroup() {
//		return mtaGroup;
//	}
//
//	public void setMtaGroup(MtaGroupDTO mtaGroup) {
//		this.mtaGroup = mtaGroup;
//	}
//
//	public NetworkInfoDTO getNetworkInfo() {
//		return networkInfo;
//	}
//
//	public void setNetworkInfo(NetworkInfoDTO networkInfo) {
//		this.networkInfo = networkInfo;
//	}
//
//	public AssetDTO getAsset() {
//		return asset;
//	}
//
//	public void setAsset(AssetDTO asset) {
//		this.asset = asset;
//	}
//
//}
