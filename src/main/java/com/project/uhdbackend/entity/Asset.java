//package com.project.uhdbackend.entity;
//
//import java.time.LocalDateTime;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import com.project.uhdbackend.dto.AssetDTO;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "MUHD_SERVER_ASSETS")
//@NoArgsConstructor
//@AllArgsConstructor
//public class Asset {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "ID", nullable = false)
//	private Long id;
//
//	@Column(name = "ASSET_NO", length = 1000)
//	private String assetNo;
//
//	@Column(name = "ACQUIRED_DATE", length = 1000)
//	private String acquiredDate;
//
//	@Column(name = "ACQUISITION_COST", length = 100)
//	private String acquisitionCost;
//
//	@Column(name = "SALVAGE_VALUE", length = 100)
//	private String salvageValue;
//
//	@Column(name = "CUSTODIAN_UNIT", length = 100)
//	private String custodianUnit;
//
//	@Column(name = "DISPOSAL_INFO", length = 100)
//	private String disposalInfo;
//
//	@Column(name = "CREATED_AT", insertable = false, updatable = false)
//	private LocalDateTime createdAt;
//
//	@Column(name = "UPDATED_AT")
//	private LocalDateTime updatedAt;
//
//	@Column(name = "REMARK", length = 100)
//	private String remark;
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getAssetNo() {
//		return assetNo;
//	}
//
//	public void setAssetNo(String assetNo) {
//		this.assetNo = assetNo;
//	}
//
//	public String getAcquiredDate() {
//		return acquiredDate;
//	}
//
//	public void setAcquiredDate(String acquiredDate) {
//		this.acquiredDate = acquiredDate;
//	}
//
//	public String getAcquisitionCost() {
//		return acquisitionCost;
//	}
//
//	public void setAcquisitionCost(String acquisitionCost) {
//		this.acquisitionCost = acquisitionCost;
//	}
//
//	public String getSalvageValue() {
//		return salvageValue;
//	}
//
//	public void setSalvageValue(String salvageValue) {
//		this.salvageValue = salvageValue;
//	}
//
//	public String getCustodianUnit() {
//		return custodianUnit;
//	}
//
//	public void setCustodianUnit(String custodianUnit) {
//		this.custodianUnit = custodianUnit;
//	}
//
//	public String getDisposalInfo() {
//		return disposalInfo;
//	}
//
//	public void setDisposalInfo(String disposalInfo) {
//		this.disposalInfo = disposalInfo;
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
//	public String getRemark() {
//		return remark;
//	}
//
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}
//
//	public void updateFrom(AssetDTO dto) {
//		if (dto == null) {
//			return;
//		}
//		if (dto.getAssetNo() != null) {
//			this.assetNo = dto.getAssetNo();
//		}
//		if (dto.getAcquiredDate() != null) {
//			this.acquiredDate = dto.getAcquiredDate();
//		}
//		if (dto.getAcquisitionCost() != null) {
//			this.acquisitionCost = dto.getAcquisitionCost();
//		}
//		if (dto.getSalvageValue() != null) {
//			this.salvageValue = dto.getSalvageValue();
//		}
//		if (dto.getCustodianUnit() != null) {
//			this.custodianUnit = dto.getCustodianUnit();
//		}
//		if (dto.getDisposalInfo() != null) {
//			this.disposalInfo = dto.getDisposalInfo();
//		}
//		if (dto.getRemark() != null) {
//			this.remark = dto.getRemark();
//		}
//	}
//}