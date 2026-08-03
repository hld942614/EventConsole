package com.project.uhd.enums;

/**
 * 對應 CMDB_ASSET.ASSET_TYPE 的 CHECK 約束。 OTHER 保留作為未來新類型出現時的過渡值，避免匯入直接失敗。
 */
public enum AssetType {
	CCTV, MAINFRAME, SERVER, NETWORK_DEVICE, VENDOR_EQUIPMENT, VIRTUAL_MACHINE, OTHER;

	/**
	 * 依照 Excel 的「類型」欄原始文字，轉換成標準化 AssetType。 對應不到的一律視為 OTHER，並把原始文字存進 ASSET_TYPE_RAW
	 * 保留可追溯性， 而不是直接匯入失敗。
	 */
	public static AssetType fromRawText(String raw) {
		if (raw == null || raw.isBlank()) {
			return OTHER;
		}
		String v = raw.trim();
		switch (v) {
		case "CCTV":
			return CCTV;
		case "大型主機":
			return MAINFRAME;
		case "小型主機":
			return SERVER;
		case "網路設備":
			return NETWORK_DEVICE;
		case "廠商設備":
			return VENDOR_EQUIPMENT;
		case "VM":
		case "虛擬機":
			return VIRTUAL_MACHINE;
		default:
			return OTHER;
		}
	}

	/** 對應 CMDB_ASSET_SEQ_NO.ASSET_TYPE_CODE 的短碼 */
	public String seqCode() {
		switch (this) {
		case CCTV:
			return "CCTV";
		case MAINFRAME:
			return "MF";
		case SERVER:
			return "SRV";
		case NETWORK_DEVICE:
			return "NET";
		case VENDOR_EQUIPMENT:
			return "VEN";
		case VIRTUAL_MACHINE:
			return "VM";
		default:
			return "OTH";
		}
	}

	/** 對應 CMDB_ASSET.ASSET_ID 的前綴，例如 CI-SRV-00000001 */
	public String assetIdPrefix() {
		return "CI-" + seqCode();
	}
}
