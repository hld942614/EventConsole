package com.project.uhdbackend.utils;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 統一把 DB 讀出來的 OffsetDateTime（帶時區）轉成台北時間、 不含時區標記的顯示字串，例如 2026-07-08
 * 16:53:33.008。
 */
public final class TimestampFormatUtil {

	private static final ZoneId ZONE_TPE = ZoneId.of("Asia/Taipei");
	private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	private TimestampFormatUtil() {
	}

	public static String format(OffsetDateTime value) {
		if (value == null) {
			return null;
		}
		return value.atZoneSameInstant(ZONE_TPE).format(DISPLAY_FORMAT);
	}
}