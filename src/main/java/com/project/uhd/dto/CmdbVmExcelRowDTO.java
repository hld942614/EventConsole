package com.project.uhd.dto;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;

public class CmdbVmExcelRowDTO {

	@ExcelProperty("Host")
	private String host;

	@ExcelProperty("Sec")
	private String secCode;

	@ExcelProperty("Env")
	private String environmentRaw;

	@ExcelProperty("Sys")
	private String systemCode;

	@ExcelProperty("Name")
	private String vmName;

	@ExcelProperty("IP Address")
	private String ipAddress;

	@ExcelProperty("Notes")
	private String notes;

	@ExcelProperty("Guest OS")
	private String guestOs;

	@ExcelIgnore
	private Integer sourceRowNo;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSecCode() {
		return secCode;
	}

	public void setSecCode(String secCode) {
		this.secCode = secCode;
	}

	public String getEnvironmentRaw() {
		return environmentRaw;
	}

	public void setEnvironmentRaw(String environmentRaw) {
		this.environmentRaw = environmentRaw;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getGuestOs() {
		return guestOs;
	}

	public void setGuestOs(String guestOs) {
		this.guestOs = guestOs;
	}

	public Integer getSourceRowNo() {
		return sourceRowNo;
	}

	public void setSourceRowNo(Integer sourceRowNo) {
		this.sourceRowNo = sourceRowNo;
	}
}