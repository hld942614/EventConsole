//package com.project.uhdbackend.dto;
//
//import com.project.uhdbackend.entity.Asset;
//
//public class AssetDTO {
//
//	private Long id;
//	private String assetNo;
//	private String acquiredDate;
//	private String acquisitionCost;
//	private String salvageValue;
//	private String custodianUnit;
//	private String disposalInfo;
//	private String remark;
//
//	public AssetDTO() {
//	}
//
//	public AssetDTO(Asset input) {
//		this.id = input.getId();
//		this.assetNo = input.getAssetNo();
//		this.acquiredDate = input.getAcquiredDate();
//		this.acquisitionCost = input.getAcquisitionCost();
//		this.salvageValue = input.getSalvageValue();
//		this.custodianUnit = input.getCustodianUnit();
//		this.disposalInfo = input.getDisposalInfo();
//		this.remark = input.getRemark();
//	}
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
//	public String getRemark() {
//		return remark;
//	}
//
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}
//}
