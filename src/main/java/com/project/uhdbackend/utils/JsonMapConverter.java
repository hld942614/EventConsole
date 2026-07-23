//package com.project.uhdbackend.utils;
//
//import java.util.Map;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
///**
// * Event.details（動態欄位，隨 alertCode 而異）存成 CLOB JSON， Java 端對應成 Map<String,
// * Object>。
// */
//@Converter
//public class JsonMapConverter implements AttributeConverter<Map<String, Object>, String> {
//
//	private static final ObjectMapper MAPPER = new ObjectMapper();
//
//	@Override
//	public String convertToDatabaseColumn(Map<String, Object> attribute) {
//		if (attribute == null) {
//			return null;
//		}
//		try {
//			return MAPPER.writeValueAsString(attribute);
//		} catch (JsonProcessingException e) {
//			throw new IllegalStateException("無法序列化 details", e);
//		}
//	}
//
//	@Override
//	public Map<String, Object> convertToEntityAttribute(String dbData) {
//		if (dbData == null) {
//			return null;
//		}
//		try {
//			return MAPPER.readValue(dbData, new TypeReference<Map<String, Object>>() {
//			});
//		} catch (JsonProcessingException e) {
//			throw new IllegalStateException("無法反序列化 details", e);
//		}
//	}
//}
