//package com.project.uhdbackend.dto;
//
//import com.project.uhdbackend.entity.MaintenanceRecord;
//
//public class MaintenanceRecordDTO {
//    private Long id;
//    private Long serverPk;
//    private String maintVendor;
//    private String largeType;
//    private String maintStart;
//    private String maintEnd;
//    private String maintType;
//    private String remark;
//
//    public MaintenanceRecordDTO() {
//    	
//    }
//    
//    public MaintenanceRecordDTO(MaintenanceRecord m) {
//        this.id = m.getId();
//        this.serverPk = (m.getServer() != null) ? m.getServer().getId() : null;
//        this.largeType = m.getLargeType();
//        this.remark = m.getRemark();
//        this.maintStart = m.getMaintStart();
//        this.maintEnd = m.getMaintEnd();
//        this.maintType = m.getMaintType();
//        this.maintVendor = m.getMaintVendor();
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
//	public String getMaintStart() {
//		return maintStart;
//	}
//
//	public void setMaintStart(String maintStart) {
//		this.maintStart = maintStart;
//	}
//
//	public String getMaintEnd() {
//		return maintEnd;
//	}
//
//	public void setMaintEnd(String maintEnd) {
//		this.maintEnd = maintEnd;
//	}
//
//	public String getMaintType() {
//		return maintType;
//	}
//
//	public void setMaintType(String maintType) {
//		this.maintType = maintType;
//	}
//
//	public String getMaintVendor() {
//		return maintVendor;
//	}
//
//	public void setMaintVendor(String maintVendor) {
//		this.maintVendor = maintVendor;
//	}
//
//	public String getLargeType() {
//		return largeType;
//	}
//
//	public void setLargeType(String largeType) {
//		this.largeType = largeType;
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
//}
