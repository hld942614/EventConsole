package com.project.uhdbackend.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.apache.poi.ss.usermodel.DateUtil;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

/**
 * 日期欄位的容錯 converter。
 *
 * Excel 的日期欄位在實務上常常不是統一格式： - NUMBER：Excel 內部的日期序列值（該格被設成「日期」格式時儲存的方式） -
 * STRING：使用者直接打字進去的文字，格式可能是 2026/2/4、2026-02-04、 2026/02/04 等等，月/日不一定補零
 *
 * 預設的 LocalDateStringConverter 只認一種固定格式，遇到 "2026/2/4" 這種 沒補零的字串就會丟
 * DateTimeParseException。這裡依 cellData.getType()
 * 分開處理，字串再多嘗試幾種常見格式，全部失敗才真正報錯（不會靜默吃掉錯誤， 讓這一列在 ImportResultDTO.errors
 * 裡看得到原始文字方便追查）。
 */
public class SafeLocalDateConverter implements Converter<LocalDate> {

	private static final List<DateTimeFormatter> STRING_FORMATS = List.of(DateTimeFormatter.ofPattern("yyyy/M/d"),
			DateTimeFormatter.ofPattern("yyyy/MM/dd"), DateTimeFormatter.ofPattern("yyyy-M-d"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd"), DateTimeFormatter.ofPattern("yyyy.M.d"),
			DateTimeFormatter.ofPattern("yyyy.MM.dd"));

	@Override
	public Class<?> supportJavaTypeKey() {
		return LocalDate.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return null; // 同時接受 STRING / NUMBER，由 convertToJavaData 自行判斷
	}

	@Override
	public LocalDate convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		if (cellData == null) {
			return null;
		}
		switch (cellData.getType()) {
		case NUMBER:
			double excelSerial = cellData.getNumberValue().doubleValue();
			return DateUtil.getJavaDate(excelSerial).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
		case STRING:
			String raw = cellData.getStringValue();
			if (raw == null || raw.isBlank()) {
				return null;
			}
			return parseFlexible(raw.trim());
		case EMPTY:
			return null;
		default:
			throw new IllegalArgumentException("不支援的日期儲存格類型: " + cellData.getType() + "，原始值: " + cellData);
		}
	}

	private LocalDate parseFlexible(String raw) {
		for (DateTimeFormatter fmt : STRING_FORMATS) {
			try {
				return LocalDate.parse(raw, fmt);
			} catch (DateTimeParseException ignored) {
				// 換下一種格式再試
			}
		}
		throw new DateTimeParseException("無法解析日期字串（已嘗試 yyyy/M/d、yyyy-MM-dd 等常見格式）: " + raw, raw, 0);
	}

	@Override
	public WriteCellData<?> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		// 這個 converter 目前只用在匯入（讀取），不需要支援匯出
		return new WriteCellData<>(value == null ? "" : value.toString());
	}
}
