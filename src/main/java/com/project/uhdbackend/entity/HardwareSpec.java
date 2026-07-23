//package com.project.uhdbackend.entity;
//
//import java.time.LocalDateTime;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.OneToOne;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//
//import org.json.JSONObject;
//
//import com.project.uhdbackend.dto.HardwareSpecDTO;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "muhd_server_hardware_spec")
//@NoArgsConstructor
//@AllArgsConstructor
//public class HardwareSpec {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hardware_id_gen")
//	@SequenceGenerator(name = "hardware_id_gen", sequenceName = "hardware_id_seq", allocationSize = 1)
//	@Column(name = "ID")
//	private Long id;
//
//	@OneToOne(fetch = FetchType.LAZY, optional = false)
//	@JoinColumn(name = "SERVER_ID", nullable = false, unique = true)
//	@JsonIgnore
//	private ServerInfo server;
//
//	@Column(name = "CPU_MODEL", length = 100)
//	private String cpuModel;
//
//	@Column(name = "CORE_COUNT", length = 100)
//	private String coreCount;
//
//	@Column(name = "HD_SIZE", length = 100)
//	private String hdSize;
//
//	@Column(name = "HD_COUNT", length = 100)
//	private String hdCount;
//
//	@Column(name = "MEMORY_SPEC", length = 100)
//	private String memorySpec;
//
//	@Column(name = "HBA_CARD", length = 100)
//	private String hbaCard;
//
//	@Column(name = "NIC_COUNT", length = 100)
//	private String nicCount;
//
//	@Column(name = "PSU_TYPE", length = 100)
//	private String psuType;
//
//	@Column(name = "GPU", length = 100)
//	private String gpu;
//
//	@Column(name = "GPU_RAM", length = 100)
//	private String gpuRam;
//
//	@Column(name = "VOLTAGE", length = 100)
//	private String voltage;
//
//	@Column(name = "VOLTAGE_RANGE", length = 100)
//	private String voltageRange;
//
//	@Column(name = "POWER_WATTS", length = 100)
//	private String powerWatts;
//
//	@Column(name = "REMARK", length = 4000)
//	private String remark;
//
//	@Column(name = "CREATED_AT", insertable = false, updatable = false)
//	private LocalDateTime createdAt;
//
//	@Column(name = "UPDATED_AT", insertable = false, updatable = false)
//	private LocalDateTime updatedAt;
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public ServerInfo getServer() {
//		return server;
//	}
//
//	public void setServer(ServerInfo server) {
//		this.server = server;
//	}
//
//	public String getCpuModel() {
//		return cpuModel;
//	}
//
//	public void setCpuModel(String cpuModel) {
//		this.cpuModel = cpuModel;
//	}
//
//	public String getCoreCount() {
//		return coreCount;
//	}
//
//	public void setCoreCount(String coreCount) {
//		this.coreCount = coreCount;
//	}
//
//	public String getHdSize() {
//		return hdSize;
//	}
//
//	public void setHdSize(String hdSize) {
//		this.hdSize = hdSize;
//	}
//
//	public String getHdCount() {
//		return hdCount;
//	}
//
//	public void setHdCount(String hdCount) {
//		this.hdCount = hdCount;
//	}
//
//	public String getMemorySpec() {
//		return memorySpec;
//	}
//
//	public void setMemorySpec(String memorySpec) {
//		this.memorySpec = memorySpec;
//	}
//
//	public String getHbaCard() {
//		return hbaCard;
//	}
//
//	public void setHbaCard(String hbaCard) {
//		this.hbaCard = hbaCard;
//	}
//
//	public String getNicCount() {
//		return nicCount;
//	}
//
//	public void setNicCount(String nicCount) {
//		this.nicCount = nicCount;
//	}
//
//	public String getPsuType() {
//		return psuType;
//	}
//
//	public void setPsuType(String psuType) {
//		this.psuType = psuType;
//	}
//
//	public String getGpu() {
//		return gpu;
//	}
//
//	public void setGpu(String gpu) {
//		this.gpu = gpu;
//	}
//
//	public String getGpuRam() {
//		return gpuRam;
//	}
//
//	public void setGpuRam(String gpuRam) {
//		this.gpuRam = gpuRam;
//	}
//
//	public String getVoltage() {
//		return voltage;
//	}
//
//	public void setVoltage(String voltage) {
//		this.voltage = voltage;
//	}
//
//	public String getVoltageRange() {
//		return voltageRange;
//	}
//
//	public void setVoltageRange(String voltageRange) {
//		this.voltageRange = voltageRange;
//	}
//
//	public String getPowerWatts() {
//		return powerWatts;
//	}
//
//	public void setPowerWatts(String powerWatts) {
//		this.powerWatts = powerWatts;
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
//	public LocalDateTime getCreatedAt() {
//		return createdAt;
//	}
//
//	public LocalDateTime getUpdatedAt() {
//		return updatedAt;
//	}
//
//	public void updateFrom(HardwareSpecDTO dto) {
//		if (dto == null)
//			return;
//
//		if (dto.getCpuModel() != null)
//			this.cpuModel = dto.getCpuModel();
//		if (dto.getCoreCount() != null)
//			this.coreCount = dto.getCoreCount();
//		if (dto.getHdSize() != null)
//			this.hdSize = dto.getHdSize();
//		if (dto.getHdCount() != null)
//			this.hdCount = dto.getHdCount();
//		if (dto.getMemorySpec() != null)
//			this.memorySpec = dto.getMemorySpec();
//		if (dto.getHbaCard() != null)
//			this.hbaCard = dto.getHbaCard();
//		if (dto.getNicCount() != null)
//			this.nicCount = dto.getNicCount();
//		if (dto.getPsuType() != null)
//			this.psuType = dto.getPsuType();
//		if (dto.getGpu() != null)
//			this.gpu = dto.getGpu();
//		if (dto.getGpu_ram() != null)
//			this.gpuRam = dto.getGpu_ram();
//		if (dto.getVoltage() != null)
//			this.voltage = dto.getVoltage();
//		if (dto.getVoltageRange() != null)
//			this.voltageRange = dto.getVoltageRange();
//		if (dto.getPowerWatts() != null)
//			this.powerWatts = dto.getPowerWatts();
//		if (dto.getRemark() != null)
//			this.remark = dto.getRemark();
//	}
//
//	@Override
//	public String toString() {
//		JSONObject jo = new JSONObject();
//		jo.put("id", id);
//		jo.put("serverId", server != null ? server.getId() : JSONObject.NULL);
//		jo.put("cpuModel", cpuModel);
//		jo.put("coreCount", coreCount);
//		jo.put("hdSize", hdSize);
//		jo.put("hdCount", hdCount);
//		jo.put("memorySpec", memorySpec);
//		jo.put("hbaCard", hbaCard);
//		jo.put("nicCount", nicCount);
//		jo.put("psuType", psuType);
//
//		jo.put("gpu", gpu);
//		jo.put("gram", gpuRam); // 你對外仍想叫 gram 就這樣輸出
//		jo.put("voltage", voltage);
//		jo.put("voltageRange", voltageRange);
//		jo.put("powerConsumption", powerWatts);
//		jo.put("remark", remark);
//
//		jo.put("createdAt", createdAt);
//		jo.put("updatedAt", updatedAt);
//		return jo.toString();
//	}
//}
