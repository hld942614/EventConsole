package com.project.uhd.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhd.dto.Condition;
import com.project.uhd.entity.Case;
import com.project.uhd.entity.Event;
import com.project.uhd.realtime.event.EventType;
import com.project.uhd.realtime.service.RealtimeEventService;
import com.project.uhd.repository.CaseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CaseClassifierService {

	private final CaseRepository caseRepository;
	private final ObjectMapper objectMapper;
	private final RealtimeEventService realtimeEventService;
	private final EventStatusService eventStatusService;

	public CaseClassifierService(CaseRepository caseRepository, ObjectMapper objectMapper,
			RealtimeEventService realtimeEventService, EventStatusService eventStatusService) {
		this.caseRepository = caseRepository;
		this.objectMapper = objectMapper;
		this.realtimeEventService = realtimeEventService;
		this.eventStatusService = eventStatusService;
	}
	
	@Transactional
	public List<Long> classifyAndAddEventToCases(Event event) {
		List<Case> cases = caseRepository.findAll();
		List<Case> matchedCases = new ArrayList<>();

		for (Case input : cases) {
			if (input.getConditions() == null || !Boolean.TRUE.equals(input.getRuleEnabled()))
				continue;
			if (!input.getStatus().isActive())
				continue;

			try {
				List<Condition> conditionList = objectMapper.readValue(input.getConditions(),
						new TypeReference<List<Condition>>() {
						});
				Map<Long, Condition> conditionMap = conditionList.stream()
						.collect(Collectors.toMap(Condition::getId, c -> c));

				Predicate<Event> predicate = EventPredicateBuilder.build(input.getLogic(), conditionMap);
				if (predicate.test(event)) {
					matchedCases.add(input);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (matchedCases.isEmpty()) {
			return List.of();
		}

		// 命中多筆時：Case 建立時間最晚者勝，同秒以 caseId 較大者勝
		Case winner = matchedCases.stream()
				.max(Comparator.comparing(Case::getCreatedAt).thenComparing(Case::getId))
				.orElseThrow();

		winner.addEvent(event);
		caseRepository.save(winner);

		eventStatusService.classifyIntoCase(event);

		realtimeEventService.publish(EventType.EVENT_CLASSIFIED, "CASE-EVENT", winner.getId(), event);

		return List.of(winner.getId());
	}
}
