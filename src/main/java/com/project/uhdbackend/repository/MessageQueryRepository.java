//package com.project.uhdbackend.repository;
//
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.time.ZoneOffset;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import com.project.uhdbackend.dto.MessageDTO;
//import com.project.uhdbackend.enums.MessageStatus;
//
//@Repository
//public class MessageQueryRepository {
//
//	private final NamedParameterJdbcTemplate jdbc;
//	private static final ZoneId ZONE_TPE = ZoneId.of("Asia/Taipei");
//
//	public MessageQueryRepository(NamedParameterJdbcTemplate jdbc) {
//		this.jdbc = jdbc;
//	}
//
//	private static final RowMapper<MessageDTO> ROW_MAPPER = (rs, rowNum) -> {
//		MessageDTO dto = new MessageDTO();
//		dto.setAlertTimestamp(rs.getString("MESSAGE_ALERTTIMESTAMP"));
//		dto.setAlertCode(rs.getString("MESSAGE_ALERTCODE"));
//		dto.setSubject(rs.getString("MESSAGE_SUBJECT"));
//		dto.setSourceIp(rs.getString("MESSAGE_SOURCEIP"));
//		dto.setData(rs.getString("MESSAGE_DATA"));
//		dto.setSender(rs.getString("MESSAGE_SENDER"));
//		dto.setReceiver(rs.getString("MESSAGE_RECEIVER"));
//		dto.setMessageId(rs.getLong("MESSAGE_ID"));
//		dto.setEmailTimestamp(rs.getString("MESSAGE_EMAILTIMESTAMP"));
//		dto.setDbTimestamp(rs.getString("MESSAGE_DBTIMESTAMP"));
//		dto.setMainCategoryTitle(rs.getString("MAIN_CATEGORY_TITLE"));
//		dto.setMainCategoryCode(rs.getString("MAIN_CATEGORY_CODE"));
//
//		String status = rs.getString("MESSAGE_STATUS");
//		if (status != null)
//			dto.setStatus(MessageStatus.valueOf(status.toUpperCase()));
//
//		int caseCount = rs.getInt("CASE_COUNT");
//		dto.setHasCase(caseCount > 0);
//
//		String caseIdsStr = rs.getString("CASE_IDS");
//		if (caseIdsStr != null && !caseIdsStr.isBlank()) {
//			List<Long> ids = Arrays.stream(caseIdsStr.split(",")).map(String::trim).filter(s -> !s.isEmpty())
//					.map(Long::valueOf).toList();
//			dto.setCaseIds(ids);
//		} else {
//			dto.setCaseIds(Collections.emptyList());
//		}
//
//		return dto;
//	};
//
//	private static final RowMapper<MessageDTO> CASE_MESSAGE_ROW_MAPPER = (rs, rowNum) -> {
//		MessageDTO dto = new MessageDTO();
//		dto.setMessageId(rs.getLong("MESSAGE_ID"));
//		dto.setAlertTimestamp(rs.getString("MESSAGE_ALERTTIMESTAMP"));
//		dto.setAlertCode(rs.getString("MESSAGE_ALERTCODE"));
//		dto.setSubject(rs.getString("MESSAGE_SUBJECT"));
//		dto.setSourceIp(rs.getString("MESSAGE_SOURCEIP"));
//		dto.setData(rs.getString("MESSAGE_DATA"));
//		dto.setSender(rs.getString("MESSAGE_SENDER"));
//		dto.setReceiver(rs.getString("MESSAGE_RECEIVER"));
//		dto.setEmailTimestamp(rs.getString("MESSAGE_EMAILTIMESTAMP"));
//		dto.setDbTimestamp(rs.getString("MESSAGE_DBTIMESTAMP"));
//
//		String status = rs.getString("MESSAGE_STATUS");
//		if (status != null)
//			dto.setStatus(MessageStatus.valueOf(status.toUpperCase()));
//
//		dto.setMainCategoryTitle(rs.getString("MAIN_CATEGORY_TITLE"));
//		dto.setMainCategoryCode(rs.getString("MAIN_CATEGORY_CODE"));
//
//		// 這支 query 沒有 CASE_COUNT / CASE_IDS，就不要讀
//		dto.setHasCase(true); // 在 case 詳細頁通常可直接 true
//		dto.setCaseIds(Collections.emptyList());
//
//		return dto;
//	};
//
//	public List<MessageDTO> getMessagesByFilters(List<MessageStatus> statusArray, String subject, String mainCategory,
//			String sender, String content, String day) {
//
//		String cat = (mainCategory == null || mainCategory.isBlank()) ? "ALL" : mainCategory.trim();
//
//		String baseSql = """
//				SELECT m.*,
//				       main.CATEGORY_TITLE AS MAIN_CATEGORY_TITLE,
//				       main.CATEGORY_CODE AS MAIN_CATEGORY_CODE,
//				       (SELECT COUNT(*) FROM MUHD.MUHD_CASE_MESSAGE cmx WHERE cmx.MESSAGE_ID = m.MESSAGE_ID) AS CASE_COUNT,
//				       (SELECT LISTAGG(TO_CHAR(cmx.CASE_ID), ',') WITHIN GROUP (ORDER BY cmx.CASE_ID)
//				          FROM MUHD.MUHD_CASE_MESSAGE cmx WHERE cmx.MESSAGE_ID = m.MESSAGE_ID) AS CASE_IDS
//				FROM MUHD.MUHD_MESSAGE m
//				LEFT JOIN MUHD.MUHD_CATEGORY code ON m.MESSAGE_ALERTCODE = code.CATEGORY_CODE
//				LEFT JOIN MUHD.MUHD_CATEGORY sub  ON code.CATEGORY_PARENTID = sub.CATEGORY_ID
//				LEFT JOIN MUHD.MUHD_CATEGORY main ON sub.CATEGORY_PARENTID = main.CATEGORY_ID
//				""";
//
//		List<String> where = new ArrayList<>();
//		MapSqlParameterSource p = new MapSqlParameterSource();
//		where.add("1=1");
//
//		if (statusArray != null && !statusArray.isEmpty()) {
//			List<String> statusNames = statusArray.stream().filter(Objects::nonNull).map(Enum::name).distinct().toList();
//
//			if (!statusNames.isEmpty()) {
//				where.add("m.MESSAGE_STATUS IN (:statuses)");
//				p.addValue("statuses", statusNames);
//			}
//		}
//
//		if (subject != null && !subject.isBlank()) {
//			where.add("LOWER(NVL(m.MESSAGE_SUBJECT, '')) LIKE :subject");
//			p.addValue("subject", "%" + subject.trim().toLowerCase() + "%");
//		}
//
//		if (sender != null && !sender.isBlank()) {
//			where.add("LOWER(NVL(m.MESSAGE_SENDER, '')) LIKE :sender");
//			p.addValue("sender", "%" + sender.trim().toLowerCase() + "%");
//		}
//
//		if (content != null && !content.isBlank()) {
//			where.add("LOWER(NVL(m.MESSAGE_DATA, '')) LIKE :content");
//			p.addValue("content", "%" + content.trim().toLowerCase() + "%");
//		}
//
//		if ("Others".equalsIgnoreCase(cat)) {
//			where.add("(main.CATEGORY_CODE IS NULL OR main.CATEGORY_CODE = '')");
//		} else if (!"ALL".equalsIgnoreCase(cat)) {
//			where.add("main.CATEGORY_CODE = :cat");
//			p.addValue("cat", cat);
//		}
//
//		if (day != null && !day.isBlank()) {
//			UtcRange r = calcUtcRangeFromTaipeiDay(day.trim());
//			where.add("m.MESSAGE_EMAILTIMESTAMP >= :startUtc");
//			where.add("m.MESSAGE_EMAILTIMESTAMP < :endUtc");
//			p.addValue("startUtc", r.startUtc);
//			p.addValue("endUtc", r.endUtc);
//		}
//
//		String sql = baseSql + " WHERE " + String.join(" AND ", where) + " ORDER BY m.MESSAGE_EMAILTIMESTAMP DESC";
//
//		return jdbc.query(sql, p, ROW_MAPPER);
//	}
//
//	public List<MessageDTO> findMessagesByCaseIdWithMainCategory(Long caseId) {
//		String sql = """
//				SELECT m.*,
//				       main.CATEGORY_TITLE AS MAIN_CATEGORY_TITLE,
//				       main.CATEGORY_CODE  AS MAIN_CATEGORY_CODE
//				FROM MUHD.MUHD_CASE_MESSAGE cm
//				JOIN MUHD.MUHD_MESSAGE m
//				  ON m.MESSAGE_ID = cm.MESSAGE_ID
//				LEFT JOIN MUHD.MUHD_CATEGORY code ON m.MESSAGE_ALERTCODE = code.CATEGORY_CODE
//				LEFT JOIN MUHD.MUHD_CATEGORY sub  ON code.CATEGORY_PARENTID = sub.CATEGORY_ID
//				LEFT JOIN MUHD.MUHD_CATEGORY main ON sub.CATEGORY_PARENTID = main.CATEGORY_ID
//				WHERE cm.CASE_ID = :caseId
//				ORDER BY m.MESSAGE_EMAILTIMESTAMP DESC
//				""";
//
//		return jdbc.query(sql, new MapSqlParameterSource("caseId", caseId), CASE_MESSAGE_ROW_MAPPER);
//	}
//
//	public List<MessageDTO> getMessagesWithMainCategoryTitle(String categoryCode) {
//		String sql = """
//				SELECT msg.*, main.CATEGORY_TITLE AS MAIN_CATEGORY_TITLE
//				FROM MUHD.MUHD_MESSAGE msg
//				LEFT JOIN MUHD.MUHD_CATEGORY code ON msg.MESSAGE_ALERTCODE = code.CATEGORY_CODE
//				LEFT JOIN MUHD.MUHD_CATEGORY sub  ON code.CATEGORY_PARENTID = sub.CATEGORY_ID
//				LEFT JOIN MUHD.MUHD_CATEGORY main ON sub.CATEGORY_PARENTID = main.CATEGORY_ID
//				WHERE (:categoryCode IS NULL OR main.CATEGORY_CODE = :categoryCode)
//				ORDER BY msg.MESSAGE_ID
//				""";
//
//		MapSqlParameterSource p = new MapSqlParameterSource().addValue("categoryCode", categoryCode);
//
//		return jdbc.query(sql, p, ROW_MAPPER);
//	}
//
//	public List<MessageDTO> getUncategorizedMessagesWithMainCategoryTitle() {
//		String sql = """
//				SELECT m.*, NULL AS MAIN_CATEGORY_TITLE
//				FROM MUHD.MUHD_MESSAGE m
//				WHERE m.MESSAGE_ALERTCODE IS NULL
//				   OR NOT EXISTS (
//				        SELECT 1
//				        FROM MUHD.MUHD_CATEGORY c
//				        WHERE c.CATEGORY_CODE = m.MESSAGE_ALERTCODE
//				   )
//				ORDER BY m.MESSAGE_ID
//				""";
//
//		return jdbc.query(sql, new MapSqlParameterSource(), ROW_MAPPER);
//	}
//
//	private static class UtcRange {
//		final String startUtc;
//		final String endUtc;
//
//		UtcRange(String startUtc, String endUtc) {
//			this.startUtc = startUtc;
//			this.endUtc = endUtc;
//		}
//	}
//
//	private UtcRange calcUtcRangeFromTaipeiDay(String day) {
//		// day: "2026-01-06"
//		LocalDate localDate = LocalDate.parse(day);
//		ZonedDateTime startTpe = localDate.atStartOfDay(ZONE_TPE);
//		ZonedDateTime endTpe = startTpe.plusDays(1);
//
//		Instant startUtcInstant = startTpe.toInstant();
//		Instant endUtcInstant = endTpe.toInstant();
//
//		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC);
//
//		return new UtcRange(fmt.format(startUtcInstant), fmt.format(endUtcInstant));
//	}
//}
