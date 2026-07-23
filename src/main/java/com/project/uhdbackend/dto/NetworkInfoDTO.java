//package com.project.uhdbackend.dto;
//
//import com.project.uhdbackend.entity.NetworkInfo;
//
//public class NetworkInfoDTO {
//    private Long id;
//    private Long serverPk;
//    private String ipUserlan;
//    private String bkIp;
//    private String iDRACIp;
//    private String remark;
//    
//    public NetworkInfoDTO() {
//    	
//    }
//
//    public NetworkInfoDTO(NetworkInfo n) {
//        this.id = n.getId();
//        this.serverPk = (n.getServer() != null) ? n.getServer().getId() : null;
//        this.ipUserlan = n.getIpUserlan();
//        this.bkIp = n.getBkIp();
//        this.iDRACIp = n.getiDRACIp();
//        this.remark = n.getRemark();
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
//	public String getIpUserlan() {
//		return ipUserlan;
//	}
//
//	public void setIpUserlan(String ipUserlan) {
//		this.ipUserlan = ipUserlan;
//	}
//
//	public String getBkIp() {
//		return bkIp;
//	}
//
//	public void setBkIp(String bkIp) {
//		this.bkIp = bkIp;
//	}
//
//	public String getiDRACIp() {
//		return iDRACIp;
//	}
//
//	public void setiDRACIp(String iDRACIp) {
//		this.iDRACIp = iDRACIp;
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
