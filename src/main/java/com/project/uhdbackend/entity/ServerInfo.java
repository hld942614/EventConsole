//package com.project.uhdbackend.entity;
//
//import java.time.LocalDateTime;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.OneToOne;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//
//import org.json.JSONObject;
//
//import com.project.uhdbackend.dto.ServerInfoDTO;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "muhd_server_info")
//@NoArgsConstructor
//@AllArgsConstructor
//public class ServerInfo {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_id_gen")
//	@SequenceGenerator(name = "server_id_gen", sequenceName = "server_id_seq", allocationSize = 1)
//	@Column(name = "id")
//	private Long id;
//
//	@Column(name = "SEC", length = 20)
//    private String sec;
//
//    @Column(name = "LOCATION", length = 100)
//    private String location;
//
//    @Column(name = "RACK", length = 20)
//    private String rack;
//
//    @Column(name = "RACK_U", length = 10)
//    private String rackU;
//
//    @Column(name = "ENV", length = 10)
//    private String env;
//
//    @Column(name = "SYSTEM", length = 50)
//    private String system;
//
//    @Column(name = "SERVER_NAME", length = 120, nullable = false)
//    private String serverName;
//
//    @Column(name = "MODEL", length = 120)
//    private String model;
//
//    @Column(name = "SERVICE_TAG", length = 120)
//    private String serviceTag;
//
//    @Column(name = "SIZE_U", length = 10)
//    private String sizeU;
//
//    @Column(name = "FUNC", length = 255)
//    private String function;
//
//    @Column(name = "OS", length = 120)
//    private String os;
//
//    @Column(name = "REMARK", length = 4000)
//    private String remark;
//
//    @Column(name = "ROOM", length = 100)
//    private String room;
//
//    // 交給 DB DEFAULT SYSTIMESTAMP；避免更新時被 DTO 覆蓋
//    @Column(name = "CREATED_AT", insertable = false, updatable = false, nullable = false)
//    private LocalDateTime createdAt;
//
//    @Column(name = "UPDATED_AT", insertable = false, updatable = false, nullable = false)
//    private LocalDateTime updatedAt;
//
//    @OneToOne(mappedBy = "server", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private HardwareSpec hardwareSpec;
//
//    @OneToOne(mappedBy = "server", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private NetworkInfo networkInfo;
//
//    @OneToOne(mappedBy = "server", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private MaintenanceRecord maintenanceRecord;
//
//    @OneToOne(mappedBy = "server", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private MtaGroup mtaGroup;
//    
//    @OneToOne(mappedBy = "server", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private Asset asset;
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getSec() {
//		return sec;
//	}
//
//	public void setSec(String sec) {
//		this.sec = sec;
//	}
//
//	public String getRack() {
//		return rack;
//	}
//
//	public void setRack(String rack) {
//		this.rack = rack;
//	}
//
//	public String getRackU() {
//		return rackU;
//	}
//
//	public void setRackU(String rackU) {
//		this.rackU = rackU;
//	}
//
//	public String getEnv() {
//		return env;
//	}
//
//	public void setEnv(String env) {
//		this.env = env;
//	}
//
//	public String getSystem() {
//		return system;
//	}
//
//	public void setSystem(String system) {
//		this.system = system;
//	}
//
//	public String getServerName() {
//		return serverName;
//	}
//
//	public void setServerName(String serverName) {
//		this.serverName = serverName;
//	}
//
//	public String getModel() {
//		return model;
//	}
//
//	public void setModel(String model) {
//		this.model = model;
//	}
//
//	public String getServiceTag() {
//		return serviceTag;
//	}
//
//	public void setServiceTag(String serviceTag) {
//		this.serviceTag = serviceTag;
//	}
//
//	public String getSizeU() {
//		return sizeU;
//	}
//
//	public void setSizeU(String sizeU) {
//		this.sizeU = sizeU;
//	}
//
//	public String getFunction() {
//		return function;
//	}
//
//	public void setFunction(String function) {
//		this.function = function;
//	}
//
//	public String getOs() {
//		return os;
//	}
//
//	public void setOs(String os) {
//		this.os = os;
//	}
//
//	public String getRemark() {
//		return remark;
//	}
//
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}
//
//	public String getLocation() {
//		return location;
//	}
//
//	public void setLocation(String location) {
//		this.location = location;
//	}
//
//	public String getRoom() {
//		return room;
//	}
//
//	public void setRoom(String room) {
//		this.room = room;
//	}
//
//	public LocalDateTime getCreatedAt() {
//		return createdAt;
//	}
//
//	public void setCreatedAt(LocalDateTime createdAt) {
//		this.createdAt = createdAt;
//	}
//
//	public LocalDateTime getUpdatedAt() {
//		return updatedAt;
//	}
//
//	public void setUpdatedAt(LocalDateTime updatedAt) {
//		this.updatedAt = updatedAt;
//	}
//
//	public HardwareSpec getHardwareSpec() {
//		return hardwareSpec;
//	}
//
//	public void setHardwareSpec(HardwareSpec hardwareSpec) {
//		this.hardwareSpec = hardwareSpec;
//	}
//
//	public NetworkInfo getNetworkInfo() {
//		return networkInfo;
//	}
//
//	public void setNetworkInfo(NetworkInfo networkInfo) {
//		this.networkInfo = networkInfo;
//	}
//
//	public MaintenanceRecord getMaintenanceRecord() {
//		return maintenanceRecord;
//	}
//
//	public void setMaintenanceRecord(MaintenanceRecord maintenanceRecord) {
//		this.maintenanceRecord = maintenanceRecord;
//	}
//
//	public MtaGroup getMtaGroup() {
//		return mtaGroup;
//	}
//
//	public void setMtaGroup(MtaGroup mtaGroup) {
//		this.mtaGroup = mtaGroup;
//	}
//
//	public Asset getAsset() {
//		return asset;
//	}
//
//	public void setAsset(Asset asset) {
//		this.asset = asset;
//	}
//
//	public void updateFrom(ServerInfoDTO dto) {
//        if (dto == null) return;
//
//        if (dto.getSec() != null) this.sec = dto.getSec();
//        if (dto.getRack() != null) this.rack = dto.getRack();
//        if (dto.getRackU() != null) this.rackU = dto.getRackU();
//        if (dto.getEnv() != null) this.env = dto.getEnv();
//        if (dto.getSystem() != null) this.system = dto.getSystem();
//        if (dto.getServerName() != null) this.serverName = dto.getServerName();
//        if (dto.getModel() != null) this.model = dto.getModel();
//        if (dto.getServiceTag() != null) this.serviceTag = dto.getServiceTag();
//        if (dto.getSizeU() != null) this.sizeU = dto.getSizeU();
//        if (dto.getFunction() != null) this.function = dto.getFunction();
//        if (dto.getOs() != null) this.os = dto.getOs();
//        if (dto.getRemark() != null) this.remark = dto.getRemark();
//        if (dto.getLocation() != null) this.location = dto.getLocation();
//        if (dto.getRoom() != null) this.room = dto.getRoom();
//    }
//
//	@Override
//    public String toString() {
//        JSONObject jo = new JSONObject();
//        jo.put("id", id);
//        jo.put("location", location);
//        jo.put("sec", sec);
//        jo.put("rack", rack);
//        jo.put("rackU", rackU);
//        jo.put("env", env);
//        jo.put("system", system);
//        jo.put("serverName", serverName);
//        jo.put("model", model);
//        jo.put("serviceTag", serviceTag);
//        jo.put("sizeU", sizeU);
//        jo.put("function", function);
//        jo.put("os", os);
//        jo.put("remark", remark);
//        jo.put("room", room);
//        jo.put("createdAt", createdAt);
//        jo.put("updatedAt", updatedAt);
//        return jo.toString();
//    }
//}
