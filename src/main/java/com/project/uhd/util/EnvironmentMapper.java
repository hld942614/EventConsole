package com.project.uhd.util;

/**
 * 把 Excel 原始環境代碼（例如 DVP、PA）轉成標準化的 ENVIRONMENT 值。
 *
 */
public final class EnvironmentMapper {

	private EnvironmentMapper() {
	}

	public static String toStandard(String raw) {
		if (raw == null || raw.isBlank()) {
			return "UNKNOWN";
		}
		String v = raw.trim().toUpperCase();
		switch (v) {
		case "DVP":
		case "DEV":
			return "DEV";
		case "TEST":
		case "SIT":
		case "UAT":
		case "QA":
			return "TEST";
		case "PA":
		case "PROD":
		case "PRD":
			return "PROD";
		case "PRE-PROD":
		case "PRE_PROD":
		case "STAGE":
			return "PRE_PROD";
		default:
			return "UNKNOWN";
		}
	}
}
