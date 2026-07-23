//package com.project.uhdbackend.dto;
//
//import com.project.uhdbackend.entity.HardwareSpec;
//
//public class HardwareSpecDTO {
//
//    private Long id;
//    private Long serverPk;
//    private String cpuModel;
//    private String coreCount;
//    private String hdSize;
//    private String hdCount;
//    private String memorySpec;
//    private String gpu;
//    private String gpu_ram;
//    private String hbaCard;
//    private String nicCount;
//    private String psuType;
//    private String voltage;
//    private String powerWatts;
//    private String voltageRange;
//    private String remark;
//
//    public HardwareSpecDTO() {}
//
//    public HardwareSpecDTO(HardwareSpec h) {
//        this.id = h.getId();
//        this.serverPk = (h.getServer() != null) ? h.getServer().getId() : null;
//        this.cpuModel = h.getCpuModel();
//        this.coreCount = h.getCoreCount();
//        this.hdSize = h.getHdSize();
//        this.hdCount = h.getHdCount();
//        this.memorySpec = h.getMemorySpec();
//        this.hbaCard = h.getHbaCard();
//        this.nicCount = h.getNicCount();
//        this.psuType = h.getPsuType();
//        this.gpu = h.getGpu();
//        this.gpu_ram = h.getGpuRam();             
//        this.voltage = h.getVoltage();
//        this.powerWatts = h.getPowerWatts(); 
//        this.voltageRange = h.getVoltageRange();
//        this.remark = h.getRemark();
//    }
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public Long getServerPk() {
//		return serverPk;
//	}
//
//	public void setServerPk(Long serverPk) {
//		this.serverPk = serverPk;
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
//	public String getGpu_ram() {
//		return gpu_ram;
//	}
//
//	public void setGpu_ram(String gpu_ram) {
//		this.gpu_ram = gpu_ram;
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
//	public String getPowerWatts() {
//		return powerWatts;
//	}
//
//	public void setPowerWatts(String powerWatts) {
//		this.powerWatts = powerWatts;
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
//	public String getRemark() {
//		return remark;
//	}
//
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}
//}
