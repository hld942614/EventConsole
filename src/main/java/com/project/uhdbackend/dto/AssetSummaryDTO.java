package com.project.uhdbackend.dto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AssetSummaryDTO {

	private String assetId;

	private String assetName;

	private String functionDesc;

	private String secCode;

	private String rackNo;

	private String sizeU;

	private String env;

	private String systemCode;

	private String serviceTag;

	private String uPosition;

	private List<String> ipList;

	public AssetSummaryDTO() {
	}

	public AssetSummaryDTO(String assetId, String assetName, String functionDesc, String secCode, String rackNo,
			String sizeU, String env, String systemCode, String serviceTag, String ipList, String uPosition) {

		this.assetId = assetId;
		this.assetName = assetName;
		this.functionDesc = functionDesc;
		this.secCode = secCode;
		this.rackNo = rackNo;
		this.sizeU = sizeU;
		this.env = env;
		this.systemCode = systemCode;
		this.serviceTag = serviceTag;

		if (ipList == null || ipList.isBlank()) {
			this.ipList = Collections.emptyList();
		} else {
			this.ipList = Arrays.asList(ipList.split(","));
		}

		this.uPosition = uPosition;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getFunctionDesc() {
		return functionDesc;
	}

	public void setFunctionDesc(String functionDesc) {
		this.functionDesc = functionDesc;
	}

	public String getSecCode() {
		return secCode;
	}

	public void setSecCode(String secCode) {
		this.secCode = secCode;
	}

	public String getRackNo() {
		return rackNo;
	}

	public void setRackNo(String rackNo) {
		this.rackNo = rackNo;
	}

	public String getSizeU() {
		return sizeU;
	}

	public void setSizeU(String sizeU) {
		this.sizeU = sizeU;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getServiceTag() {
		return serviceTag;
	}

	public void setServiceTag(String serviceTag) {
		this.serviceTag = serviceTag;
	}

	public List<String> getIpList() {
		return ipList;
	}

	public void setIpList(List<String> ipList) {
		this.ipList = ipList;
	}

	public String getUPosition() {
		return uPosition;
	}

	public void setUPosition(String uPosition) {
		this.uPosition = uPosition;
	}

}
