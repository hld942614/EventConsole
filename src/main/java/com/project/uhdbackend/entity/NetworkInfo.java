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
//import com.project.uhdbackend.dto.NetworkInfoDTO;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "muhd_server_network_info")
//@NoArgsConstructor
//@AllArgsConstructor
//public class NetworkInfo {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "network_id_gen")
//	@SequenceGenerator(name = "network_id_gen", sequenceName = "network_id_seq", allocationSize = 1)
//	@Column(name = "id")
//	private Long id;
//
//	@OneToOne(fetch = FetchType.LAZY, optional = false)
//	@JoinColumn(name = "SERVER_ID", nullable = false, unique = true)
//	@JsonIgnore
//	private ServerInfo server;
//
//	@Column(name = "IP_USERLAN")
//	private String ipUserlan;
//
//	@Column(name = "BK_IP")
//	private String bkIp;
//	
//	@Column(name = "IDRAC_IP")
//	private String iDRACIp;
//	
//	@Column(name = "REMARK")
//	private String remark;
//	
//	@Column(name = "CREATED_AT", insertable = false, updatable = false, nullable = false)
//    private LocalDateTime createdAt;
//
//    @Column(name = "UPDATED_AT", insertable = false, updatable = false, nullable = false)
//    private LocalDateTime updatedAt;
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
//	
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
//	public void updateFrom(NetworkInfoDTO dto) {
//	    if (dto == null) {
//	        return;
//	    }
//
//	    if (dto.getIpUserlan() != null) {
//	        this.ipUserlan = dto.getIpUserlan();
//	    }
//	    if (dto.getBkIp() != null) {
//	        this.bkIp = dto.getBkIp();
//	    }
//	    if (dto.getBkIp() != null) {
//	        this.bkIp = dto.getBkIp();
//	    }
//	    if (dto.getBkIp() != null) {
//	        this.bkIp = dto.getBkIp();
//	    }
//	    if (dto.getBkIp() != null) {
//	        this.bkIp = dto.getBkIp();
//	    }
//	    if (dto.getBkIp() != null) {
//	        this.bkIp = dto.getBkIp();
//	    }
//	}
//
//	@Override
//	public String toString() {
//		JSONObject jo = new JSONObject();
//		jo.put("id", id);
//		jo.put("serverId", server != null ? server.getId() : JSONObject.NULL);
//		jo.put("ipUserlan", ipUserlan);
//		jo.put("bkIp", bkIp);
//		jo.put("iDRACIp", iDRACIp);
//		jo.put("remark", remark);
//		jo.put("createdAt", createdAt);
//		jo.put("updatedAt", updatedAt);
//		return jo.toString();
//	}
//}