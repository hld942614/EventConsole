package com.project.uhdbackend.repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.uhdbackend.dto.EventDTO;
import com.project.uhdbackend.enums.EventStatus;
import com.project.uhdbackend.utils.TimestampFormatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class EventQueryRepository {

	private final NamedParameterJdbcTemplate jdbc;
	private static final ZoneId ZONE_TPE = ZoneId.of("Asia/Taipei");
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public EventQueryRepository(NamedParameterJdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	private static final RowMapper<EventDTO> ROW_MAPPER = (rs, rowNum) -> {
		EventDTO dto = new EventDTO();
		dto.setId(rs.getLong("ID"));
		dto.setEventId(rs.getString("EVENT_ID"));

		String status = rs.getString("EVENT_STATUS");
		if (status != null) {
			dto.setEventStatus(EventStatus.valueOf(status.toUpperCase()));
		}
		dto.setModuleCode(rs.getString("MODULE_CODE"));
		dto.setHasAttachment("Y".equalsIgnoreCase(rs.getString("HAS_ATTACHMENT")));

		dto.setAlertCode(rs.getString("ALERT_CODE"));
		dto.setSource(EventDTO.buildSource(rs.getString("ENVIRONMENT"), rs.getString("SOURCE_HOST"),
				rs.getString("SOURCE_IP")));
		dto.setSeverity(rs.getString("SEVERITY"));
		dto.setTitle(rs.getString("TITLE"));
		dto.setMessage(rs.getString("MESSAGE_CONTENT"));
		dto.setOccurredAt(TimestampFormatUtil.format(rs.getObject("OCCURRED_AT", OffsetDateTime.class)));
		dto.setDetails(rs.getString("DETAILS"));
		dto.setRawJsonPayload(rs.getString("RAW_JSON_PAYLOAD"));
		dto.setAssignedTo(rs.getString("ASSIGNED_TO"));
		dto.setAcknowledgedAt(TimestampFormatUtil.format(rs.getObject("ACKNOWLEDGED_AT", OffsetDateTime.class)));
		dto.setResolvedBy(rs.getString("RESOLVED_BY"));
		dto.setResolvedAt(TimestampFormatUtil.format(rs.getObject("RESOLVED_AT", OffsetDateTime.class)));
		dto.setClosedBy(rs.getString("CLOSED_BY"));
		dto.setClosedAt(TimestampFormatUtil.format(rs.getObject("CLOSED_AT", OffsetDateTime.class)));

		int caseCount = rs.getInt("CASE_COUNT");
		dto.setHasCase(caseCount > 0);

		String caseIdsStr = rs.getString("CASE_IDS");
		if (caseIdsStr != null && !caseIdsStr.isBlank()) {
			List<Long> ids = Arrays.stream(caseIdsStr.split(",")).map(String::trim).filter(s -> !s.isEmpty())
					.map(Long::valueOf).toList();
			dto.setCaseIds(ids);
		} else {
			dto.setCaseIds(Collections.emptyList());
		}

		dto.setValidationErrorMessage(rs.getString("VALIDATION_ERROR_MESSAGE"));

		return dto;
	};

	/** DETAILS 存的是 CLOB JSON 字串，這裡解析成 List；解析失敗不影響其他欄位，details 就回 null */
//	private static List<Map<String, Object>> parseDetails(String json) {
//		if (json == null || json.isBlank()) {
//			return null;
//		}
//		try {
//			return OBJECT_MAPPER.readValue(json, new TypeReference<List<Map<String, Object>>>() {
//			});
//		} catch (Exception e) {
//			return null;
//		}
//	}

	public List<EventDTO> getEventsByFilters(List<EventStatus> statusArray, String subject, String moduleCode,
			String sender, String content, String day) {

		String baseSql = """
				SELECT e.*,
				       (SELECT COUNT(*) FROM MUHD.MUHD_CASE_EVENT cex WHERE cex.EVENT_PK = e.ID) AS CASE_COUNT,
				       (SELECT LISTAGG(TO_CHAR(cex.CASE_ID), ',') WITHIN GROUP (ORDER BY cex.CASE_ID)
				          FROM MUHD.MUHD_CASE_EVENT cex WHERE cex.EVENT_PK = e.ID) AS CASE_IDS
				FROM MUHD_EVENT e
				""";

		List<String> where = new ArrayList<>();
		MapSqlParameterSource p = new MapSqlParameterSource();
		where.add("1=1");

		if (statusArray != null && !statusArray.isEmpty()) {
			List<String> statusNames = statusArray.stream().filter(Objects::nonNull).map(Enum::name).distinct()
					.toList();
			if (!statusNames.isEmpty()) {
				where.add("e.EVENT_STATUS IN (:statuses)");
				p.addValue("statuses", statusNames);
			}
		}

		if (subject != null && !subject.isBlank()) {
			where.add("LOWER(NVL(e.SUBJECT, '')) LIKE :subject");
			p.addValue("subject", "%" + subject.trim().toLowerCase() + "%");
		}

		if (sender != null && !sender.isBlank()) {
			where.add("LOWER(NVL(e.SENDER, '')) LIKE :sender");
			p.addValue("sender", "%" + sender.trim().toLowerCase() + "%");
		}

		if (content != null && !content.isBlank()) {
			where.add("LOWER(NVL(e.MESSAGE_CONTENT, '')) LIKE :content");
			p.addValue("content", "%" + content.trim().toLowerCase() + "%");
		}

		if (moduleCode != null && !moduleCode.isBlank()) {
			if ("Others".equalsIgnoreCase(moduleCode)) {
				where.add("(e.MODULE_CODE IS NULL OR e.MODULE_CODE = '')");
			} else if (!"ALL".equalsIgnoreCase(moduleCode)) {
				where.add("e.MODULE_CODE = :moduleCode");
				p.addValue("moduleCode", moduleCode);
			}
		}

		if (day != null && !day.isBlank()) {
			UtcRange r = calcUtcRangeFromTaipeiDay(day.trim());
			where.add("e.OCCURRED_AT >= :startUtc");
			where.add("e.OCCURRED_AT < :endUtc");
			p.addValue("startUtc", r.startUtc);
			p.addValue("endUtc", r.endUtc);
		}

		String sql = baseSql + " WHERE " + String.join(" AND ", where) + " ORDER BY e.OCCURRED_AT DESC";

		return jdbc.query(sql, p, ROW_MAPPER);
	}

	public List<EventDTO> findEventsByCaseId(Long caseId) {
		String sql = """
				SELECT e.*,
				       (SELECT COUNT(*) FROM MUHD.MUHD_CASE_EVENT cex WHERE cex.EVENT_PK = e.ID) AS CASE_COUNT,
				       (SELECT LISTAGG(TO_CHAR(cex.CASE_ID), ',') WITHIN GROUP (ORDER BY cex.CASE_ID)
				          FROM MUHD.MUHD_CASE_EVENT cex WHERE cex.EVENT_PK = e.ID) AS CASE_IDS
				FROM MUHD_CASE_EVENT ce
				JOIN MUHD_EVENT e ON e.ID = ce.EVENT_PK
				WHERE ce.CASE_ID = :caseId
				ORDER BY e.OCCURRED_AT DESC
				""";

		return jdbc.query(sql, new MapSqlParameterSource("caseId", caseId), ROW_MAPPER);
	}

	public Optional<EventDTO> findByEventId(String eventId) {
		String sql = """
				SELECT e.*,
				       (SELECT COUNT(*) FROM MUHD.MUHD_CASE_EVENT cex WHERE cex.EVENT_PK = e.ID) AS CASE_COUNT,
				       (SELECT LISTAGG(TO_CHAR(cex.CASE_ID), ',') WITHIN GROUP (ORDER BY cex.CASE_ID)
				          FROM MUHD.MUHD_CASE_EVENT cex WHERE cex.EVENT_PK = e.ID) AS CASE_IDS
				FROM MUHD_EVENT e
				WHERE e.EVENT_ID = :eventId
				""";

		List<EventDTO> result = jdbc.query(sql, new MapSqlParameterSource("eventId", eventId), ROW_MAPPER);
		return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
	}

	private static class UtcRange {
		final OffsetDateTime startUtc;
		final OffsetDateTime endUtc;

		UtcRange(OffsetDateTime startUtc, OffsetDateTime endUtc) {
			this.startUtc = startUtc;
			this.endUtc = endUtc;
		}
	}

	private UtcRange calcUtcRangeFromTaipeiDay(String day) {

		LocalDate localDate = LocalDate.parse(day);
		ZonedDateTime startTpe = localDate.atStartOfDay(ZONE_TPE);
		ZonedDateTime endTpe = startTpe.plusDays(1);

		OffsetDateTime startUtc = startTpe.toOffsetDateTime().withOffsetSameInstant(ZoneOffset.UTC);
		OffsetDateTime endUtc = endTpe.toOffsetDateTime().withOffsetSameInstant(ZoneOffset.UTC);

		return new UtcRange(startUtc, endUtc);
	}
}