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
//import com.project.uhdbackend.dto.MtaGroupDTO;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "muhd_server_mta_group")
//@NoArgsConstructor
//@AllArgsConstructor
//public class MtaGroup {
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mta_group_id_gen")
//	@SequenceGenerator(name = "mta_group_id_gen", sequenceName = "mta_group_id_seq", allocationSize = 1)
//	@Column(name = "id")
//	private Long id;
//	
//	@OneToOne(fetch = FetchType.LAZY, optional = false)
//	@JoinColumn(name = "SERVER_ID", nullable = false, unique = true)
//	@JsonIgnore
//	private ServerInfo server;
//
//    @Column(name = "MTA_GROUP_CODE")
//    private String mtaGroupCode;
//
//    @Column(name = "MTA_GROUP_NAME")
//    private String mtaGroupName;
//
//    @Column(name = "MTA_USER")
//    private String mtaUser;
//
//    @Column(name = "CREATED_AT", insertable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column(name = "UPDATED_AT", insertable = false, updatable = false)
//    private LocalDateTime updatedAt;
//
//    public Long getId() {
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
//	public void updateFrom(MtaGroupDTO dto) {
//	    if (dto == null) {
//	        return;
//	    }
//
//	    if (dto.getMtaGroupCode() != null) {
//	        this.mtaGroupCode = dto.getMtaGroupCode();
//	    }
//	    if (dto.getMtaGroupName() != null) {
//	        this.mtaGroupName = dto.getMtaGroupName();
//	    }
//	    if (dto.getMtaUser() != null) {
//	        this.mtaUser = dto.getMtaUser();
//	    }
//	}
//
//	@Override
//    public String toString() {
//        JSONObject jo = new JSONObject();
//        jo.put("id", id);
//        jo.put("serverId", server != null ? server.getId() : JSONObject.NULL);
//        jo.put("mtaGroupCode", mtaGroupCode);
//        jo.put("mtaGroupName", mtaGroupName);
//        jo.put("mtaUser", mtaUser);
//        jo.put("createdAt", createdAt);
//        jo.put("updatedAt", updatedAt);
//        return jo.toString();
//    }
//}
