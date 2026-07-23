//package com.project.uhdbackend.entity;
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
//import com.project.uhdbackend.dto.MaintenanceRecordDTO;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "muhd_server_maintenance_record")
//@NoArgsConstructor
//@AllArgsConstructor
//public class MaintenanceRecord {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "maintenance_id_gen")
//	@SequenceGenerator(name = "maintenance_id_gen", sequenceName = "maintenance_id_seq", allocationSize = 1)
//	@Column(name = "id")
//    private Long id;
//
//	@OneToOne(fetch = FetchType.LAZY, optional = false)
//	@JoinColumn(name = "SERVER_ID", nullable = false, unique = true)
//	@JsonIgnore
//	private ServerInfo server;
//	
//	@Column(name = "LARGE_TYPE")
//    private String largeType;
//
//    @Column(name = "MAINT_START")
//    private String maintStart;
//
//    @Column(name = "MAINT_END")
//    private String maintEnd;
//
//    @Column(name = "MAINT_TYPE")
//    private String maintType;
//
//    @Column(name = "MAINT_VENDOR")
//    private String maintVendor;
//    
//    @Column(name = "REMARK")
//    private String remark;
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
//	public void updateFrom(MaintenanceRecordDTO dto) {
//	    if (dto == null) {
//	        return;
//	    }
//	    if (dto.getMaintStart() != null) {
//	        this.maintStart = dto.getMaintStart();
//	    }
//	    if (dto.getMaintEnd() != null) {
//	        this.maintEnd = dto.getMaintEnd();
//	    }
//	    if (dto.getMaintType() != null) {
//	        this.maintType = dto.getMaintType();
//	    }
//	    if (dto.getMaintVendor() != null) {
//	        this.maintVendor = dto.getMaintVendor();
//	    }
//	    if (dto.getLargeType() != null) {
//	        this.largeType = dto.getLargeType();
//	    }
//	    if (dto.getRemark() != null) {
//	        this.remark = dto.getRemark();
//	    }
//	}
//	
//	@Override
//	public String toString() {
//	    JSONObject jo = new JSONObject();
//	    jo.put("id", id);
//	    jo.put("serverId", server != null ? server.getId() : JSONObject.NULL);
//	    jo.put("maintStart", maintStart != null ? maintStart.toString() : JSONObject.NULL);
//	    jo.put("maintEnd", maintEnd != null ? maintEnd.toString() : JSONObject.NULL);
//	    jo.put("maintType", maintType);
//	    jo.put("maintVendor", maintVendor);
//	    jo.put("largeType", largeType);
//	    jo.put("remark", remark);
//	    return jo.toString();
//	}
//}
