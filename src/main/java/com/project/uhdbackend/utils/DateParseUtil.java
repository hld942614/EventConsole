package com.project.uhdbackend.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.DateUtil;

import com.project.uhdbackend.exception.RowValidationException;

/**
 * 解析 Excel 日期欄位的文字值。
 *
 * Excel 的日期欄位實務上有兩種樣子： - 儲存格被設成「日期」格式時，FastExcel 讀出來的字串其實是數字序列值 （例如 46000
 * 代表某一天），要用 POI 的 DateUtil 轉換 - 使用者直接打字進去的文字，格式可能是 2026/2/4、2026-02-04
 * 等，月/日不一定補零
 *
 * 這裡刻意不用 FastExcel 的 Converter 機制處理，而是把日期欄位在 DTO 裡當純文字接進來， 在 converter
 * 自己的程式碼裡呼叫這個 util 解析——這樣解析失敗時可以直接組出 「哪個欄位、原始文字是什麼」的錯誤訊息，並且保證會落在
 * CmdbImportService 既有的「一列失敗記錄進 ImportResultDTO.errors，其他列繼續跑」的機制裡， 不會在
 * FastExcel 讀檔階段就整批中斷。
 */
public final class DateParseUtil {

	private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+(\\.\\d+)?$");

	private static final List<DateTimeFormatter> STRING_FORMATS = List.of(DateTimeFormatter.ofPattern("yyyy/M/d"),
			DateTimeFormatter.ofPattern("yyyy/MM/dd"), DateTimeFormatter.ofPattern("yyyy-M-d"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd"), DateTimeFormatter.ofPattern("yyyy.M.d"),
			DateTimeFormatter.ofPattern("yyyy.MM.dd"));

	private DateParseUtil() {
	}

	/**
	 * @param raw       Excel 該格的原始文字（可能是日期字串，也可能是數字序列值）
	 * @param fieldName 這是哪個欄位，用於錯誤訊息（例如 "取得日期"、"維護起始日"）
	 * @return 解析結果；raw 是空白時回傳 null
	 * @throws RowValidationException 所有已知格式都解析失敗時拋出，訊息包含欄位名稱與原始文字
	 */
	public static LocalDate parse(String raw, String fieldName) {
		if (raw == null || raw.isBlank()) {
			return null;
		}
		String trimmed = raw.trim();

		if (NUMERIC_PATTERN.matcher(trimmed).matches()) {
			try {
				double serial = Double.parseDouble(trimmed);
				return DateUtil.getJavaDate(serial).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			} catch (Exception e) {
				throw new RowValidationException(fieldName + " 無法解析為日期（數值序列值 '" + trimmed + "' 轉換失敗）");
			}
		}

		for (DateTimeFormatter fmt : STRING_FORMATS) {
			try {
				return LocalDate.parse(trimmed, fmt);
			} catch (DateTimeParseException ignored) {
				// 換下一種格式再試
			}
		}

		throw new RowValidationException(fieldName + " 日期格式無法解析: '" + raw + "'（已嘗試 yyyy/M/d、yyyy-MM-dd 等常見格式）");
	}
}
