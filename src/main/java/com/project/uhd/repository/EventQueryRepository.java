package com.project.uhd.repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.uhd.dto.EventDTO;
import com.project.uhd.enums.EventStatus;
import com.project.uhd.util.TimestampFormatUtil;

@Repository
public class EventQueryRepository {

	private final NamedParameterJdbcTemplate jdbc;
	private static final ZoneId ZONE_TPE = ZoneId.of("Asia/Taipei");

	public EventQueryRepository(NamedParameterJdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	private static final RowMapper<EventDTO> ROW_MAPPER = (rs, rowNum) -> {
		EventDTO dto = new EventDTO();
		dto.setId(rs.getLong("ID"));
		dto.setEventId(rs.getString("EVENT_ID"));

		String statusStr = rs.getString("EVENT_STATUS");
		if (statusStr != null) {
			dto.setStatus(EventStatus.valueOf(statusStr.toUpperCase()));
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

	public List<EventDTO> getEventsByFilters(List<EventStatus> statusArray, String subject, String moduleCode,
			String sender, String content, String startDay, String endDay) {

		String baseSql = """
				SELECT e.*,
					CASE WHEN e.CASE_ID IS NOT NULL THEN 1 ELSE 0 END AS CASE_COUNT,
					TO_CHAR(e.CASE_ID) AS CASE_IDS
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

		if (startDay != null && !startDay.isBlank() && endDay != null && !endDay.isBlank()) {
			UtcRange r = calcUtcRangeFromTaipeiDayRange(startDay.trim(), endDay.trim());
			where.add("e.OCCURRED_AT >= :startUtc");
			where.add("e.OCCURRED_AT < :endUtc");
			p.addValue("startUtc", r.startUtc);
			p.addValue("endUtc", r.endUtc);
		}

		String sql = baseSql + " WHERE " + String.join(" AND ", where) + " ORDER BY e.OCCURRED_AT DESC";

		return jdbc.query(sql, p, ROW_MAPPER);
	}

	/** startDay 00:00（台北時區）到 endDay 隔天 00:00（台北時區）換算成 UTC，結束日為含當天一整天 */
	private UtcRange calcUtcRangeFromTaipeiDayRange(String startDay, String endDay) {

		LocalDate start = LocalDate.parse(startDay);
		LocalDate end = LocalDate.parse(endDay);

		if (end.isBefore(start)) {
			throw new IllegalArgumentException("endDay 不可早於 startDay: startDay=" + startDay + ", endDay=" + endDay);
		}

		ZonedDateTime startTpe = start.atStartOfDay(ZONE_TPE);
		ZonedDateTime endTpe = end.plusDays(1).atStartOfDay(ZONE_TPE); // 含 endDay 當天整天

		OffsetDateTime startUtc = startTpe.toOffsetDateTime().withOffsetSameInstant(ZoneOffset.UTC);
		OffsetDateTime endUtc = endTpe.toOffsetDateTime().withOffsetSameInstant(ZoneOffset.UTC);

		return new UtcRange(startUtc, endUtc);
	}

	public List<EventDTO> findEventsByCaseId(Long caseId) {
		String sql = """
				SELECT e.*,
				       CASE WHEN e.CASE_ID IS NOT NULL THEN 1 ELSE 0 END AS CASE_COUNT,
				       TO_CHAR(e.CASE_ID) AS CASE_IDS
				FROM MUHD_EVENT e
				WHERE e.CASE_ID = :caseId
				ORDER BY e.OCCURRED_AT DESC
				""";

		return jdbc.query(sql, new MapSqlParameterSource("caseId", caseId), ROW_MAPPER);
	}

	public Optional<EventDTO> findByEventId(String eventId) {
		String sql = """
				SELECT e.*,
				       CASE WHEN e.CASE_ID IS NOT NULL THEN 1 ELSE 0 END AS CASE_COUNT,
				       TO_CHAR(e.CASE_ID) AS CASE_IDS
				FROM MUHD_EVENT e
				WHERE e.EVENT_ID = :eventId
				""";

		List<EventDTO> result = jdbc.query(sql, new MapSqlParameterSource("eventId", eventId), ROW_MAPPER);
		return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
	}
	
	public List<EventDTO> findAllByEventIdIn(Collection<String> eventIds) {
	    if (eventIds == null || eventIds.isEmpty()) {
	        return Collections.emptyList();
	    }

	    String sql = """
	            SELECT e.*,
				       CASE WHEN e.CASE_ID IS NOT NULL THEN 1 ELSE 0 END AS CASE_COUNT,
				       TO_CHAR(e.CASE_ID) AS CASE_IDS
				FROM MUHD_EVENT e
	            WHERE e.EVENT_ID IN (:eventIds)
	            """;

	    return jdbc.query(sql, new MapSqlParameterSource("eventIds", eventIds), ROW_MAPPER);
	}

	private static class UtcRange {
		final OffsetDateTime startUtc;
		final OffsetDateTime endUtc;

		UtcRange(OffsetDateTime startUtc, OffsetDateTime endUtc) {
			this.startUtc = startUtc;
			this.endUtc = endUtc;
		}
	}
}