package com.project.uhdbackend.utils;

import java.time.LocalDate;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

public class DebugLocalDateConverter implements Converter<LocalDate> {

	@Override
	public Class<?> supportJavaTypeKey() {
		return LocalDate.class;
	}

	@Override
	public LocalDate convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty property,
			GlobalConfiguration globalConfiguration) {
		// 先印出來看看到底是什麼型別、什麼內容
		System.out.println("CellType=" + cellData.getType() + ", StringValue=" + cellData.getStringValue()
				+ ", NumberValue=" + cellData.getNumberValue());
		// ...再決定怎麼轉
		return null;
	}
}
