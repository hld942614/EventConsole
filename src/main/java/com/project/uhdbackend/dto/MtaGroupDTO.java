//package com.project.uhdbackend.dto;
//
//import com.project.uhdbackend.entity.MtaGroup;
//
//public class MtaGroupDTO {
//    private Long id;
//    private Long serverPk;
//    private String mtaGroupCode;
//    private String mtaGroupName;
//    private String mtaUser;
//    
//    public MtaGroupDTO() {
//    	
//    }
//
//    public MtaGroupDTO(MtaGroup g) {
//        this.id = g.getId();
//        this.serverPk = (g.getServer() != null) ? g.getServer().getId() : null;
//        this.mtaGroupCode = g.getMtaGroupCode();
//        this.mtaGroupName = g.getMtaGroupName();
//        this.mtaUser = g.getMtaUser();
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
//	public String getMtaGroupCode() {
//		return mtaGroupCode;
//	}
//
//	public void setMtaGroupCode(String mtaGroupCode) {
//		this.mtaGroupCode = mtaGroupCode;
//	}
//
//	public String getMtaGroupName() {
//		return mtaGroupName;
//	}
//
//	public void setMtaGroupName(String mtaGroupName) {
//		this.mtaGroupName = mtaGroupName;
//	}
//
//	public String getMtaUser() {
//		return mtaUser;
//	}
//
//	public void setMtaUser(String mtaUser) {
//		this.mtaUser = mtaUser;
//	}
//}
