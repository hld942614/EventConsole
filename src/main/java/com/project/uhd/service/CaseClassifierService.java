package com.project.uhd.service;

import java.util.ArrayList;
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

//	@Transactional
//	public boolean classifyAndAddMessageToCases(Message message) {
//		List<Case> cases = caseRepository.findAll();
//		boolean matched = false;
//		for (Case input : cases) {
//			if (input.getConditions() == null || !input.getRuleEnabled())
//				continue;
//
//			try {
//				List<Condition> conditionList = new ObjectMapper().readValue(input.getConditions(),
//						new TypeReference<List<Condition>>() {
//						});
//				Map<Long, Condition> conditionMap = conditionList.stream()
//						.collect(Collectors.toMap(Condition::getId, c -> c));
//
//				Predicate<Message> predicate = PredicateBuilder.build(input.getLogic(), conditionMap);
//				if (predicate.test(message)) {
//					input.addMessage(message);
//					message.addCase(input);
//					matched = true;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		caseRepository.saveAll(cases);
//		return matched;
//	}
//
//	public Predicate<Message> buildPredicate(Case inputCase) {
//		if (inputCase.getConditions() == null || !Boolean.TRUE.equals(inputCase.getRuleEnabled())) {
//			// 回傳永遠 false 的 predicate
//			return m -> false;
//		}
//
//		try {
//			List<Condition> conditionList = objectMapper.readValue(inputCase.getConditions(),
//					new TypeReference<List<Condition>>() {
//					});
//
//			Map<Long, Condition> conditionMap = conditionList.stream()
//					.collect(Collectors.toMap(Condition::getId, c -> c));
//
//			return PredicateBuilder.build(inputCase.getLogic(), conditionMap);
//		} catch (Exception e) {
//			throw new IllegalArgumentException("Failed to parse conditions for case " + inputCase.getId(), e);
//		}
//	}

	/**
	 * 新 Event 進來時，對所有 ruleEnabled=true 且狀態為 OPEN 的 Case 跑一次規則比對， 符合的就掛上去。已
	 * RESOLVED/CLOSED 的 Case 不再參與自動分類， 避免結案後又默默長出新的未處理事件。
	 */
	@Transactional
	public List<Long> classifyAndAddEventToCases(Event event) {
		List<Case> cases = caseRepository.findAll();
		List<Long> matchedCaseIds = new ArrayList<>();

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
					input.addEvent(event);
					event.getCases().add(input);
					matchedCaseIds.add(input.getId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		caseRepository.saveAll(cases);

		if (!matchedCaseIds.isEmpty()) {
			eventStatusService.classifyIntoCase(event);
		}

		for (Long caseId : matchedCaseIds) {
			realtimeEventService.publish(EventType.EVENT_CLASSIFIED, "CASE-EVENT", caseId, event);
		}

		return matchedCaseIds;
	}
}
