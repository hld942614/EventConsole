package com.project.uhd.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhd.authentication.CustomUserDetails;
import com.project.uhd.dto.EventDTO;
import com.project.uhd.entity.Case;
import com.project.uhd.entity.Event;
import com.project.uhd.enums.CaseStatus;
import com.project.uhd.enums.ChangeSource;
import com.project.uhd.enums.EventStatus;
import com.project.uhd.realtime.event.EventType;
import com.project.uhd.realtime.service.RealtimeEventService;
import com.project.uhd.repository.CaseRepository;
import com.project.uhd.repository.EventQueryRepository;
import com.project.uhd.repository.EventRepository;

/**
 * 對外（Controller）一律用業務鍵 EVENT_ID（String）操作， 內部因為 CaseEvent join table 存的是
 * MUHD_EVENT 的內部 PK（EVENT_PK）， 所以需要先用 eventRepository 把 EVENT_ID 轉成內部
 * PK，才能寫入/刪除 join table。
 */
@Service
public class CaseEventService {

	private final EventRepository eventRepository;
	private final RealtimeEventService realtimeEventService;
	private final CaseRepository caseRepository;
	private final EventStatusService eventStatusService;
	private final EventQueryRepository eventQueryRepository;
	private final EntityManager entityManager;

	public CaseEventService(EventRepository eventRepository, RealtimeEventService realtimeEventService,
			CaseRepository caseRepository, EventStatusService eventStatusService,
			EventQueryRepository eventQueryRepository, EntityManager entityManager) {
		this.eventRepository = eventRepository;
		this.realtimeEventService = realtimeEventService;
		this.caseRepository = caseRepository;
		this.eventStatusService = eventStatusService;
		this.eventQueryRepository = eventQueryRepository;
		this.entityManager = entityManager;
	}

	@Transactional
	public void addEventsToCase(Long caseId, List<String> eventIds, String assignedTo,
			CustomUserDetails currentUser) {
		if (caseId == null || eventIds == null || eventIds.isEmpty()) {
			return;
		}

		Case targetCase = caseRepository.findById(caseId)
				.orElseThrow(() -> new NoSuchElementException("Case not found: " + caseId));

		if (!targetCase.getStatus().isActive()) {
			throw new IllegalStateException("Case 已" + (targetCase.getStatus() == CaseStatus.CLOSED ? "結案" : "解決")
					+ "，無法再手動加入事件: caseId=" + caseId);
		}

		List<String> filteredEventIds = eventIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
		if (filteredEventIds.isEmpty()) {
			return;
		}

		List<Event> foundEvents = eventRepository.findAllByEventIdIn(filteredEventIds);

		List<String> foundEventIds = foundEvents.stream().map(Event::getEventId).collect(Collectors.toList());
		List<String> notFound = filteredEventIds.stream().filter(id -> !foundEventIds.contains(id))
				.collect(Collectors.toList());
		if (!notFound.isEmpty()) {
			throw new NoSuchElementException("Event not found: " + notFound);
		}

		List<String> alreadyClassified = foundEvents.stream()
				.filter(e -> e.getCaze() != null && !e.getCaze().getId().equals(caseId))
				.map(e -> e.getEventId() + " (Case #" + e.getCaze().getId() + ")")
				.collect(Collectors.toList());
		if (!alreadyClassified.isEmpty()) {
			throw new IllegalStateException("以下事件已分類至其他 Case，請先移除後再加入: " + alreadyClassified);
		}

		for (Event event : foundEvents) {
			event.setCaze(targetCase);
			eventStatusService.classifyIntoCase(event, currentUser, ChangeSource.USER);
		}
		entityManager.flush();
		List<EventDTO> updatedEventDtos = eventQueryRepository.findAllByEventIdIn(filteredEventIds);
		realtimeEventService.publish(EventType.EVENT_RECLASSIFIED, "CASE-EVENT", caseId, updatedEventDtos);
	}

	@Transactional
	public void removeCaseEvents(Long caseId, List<String> eventIds, CustomUserDetails currentUser) {
		if (caseId == null || eventIds == null || eventIds.isEmpty()) {
			return;
		}
		List<String> filteredEventIds = eventIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
		if (filteredEventIds.isEmpty()) {
			return;
		}

		List<Event> foundEvents = eventRepository.findAllByEventIdIn(filteredEventIds);

		for (Event event : foundEvents) {
			if (event.getCaze() != null && event.getCaze().getId().equals(caseId)) {
				event.setCaze(null);
				eventStatusService.unclassifyFromCase(event, currentUser);
			}
		}
		entityManager.flush();
		List<EventDTO> updatedEventDtos = eventQueryRepository.findAllByEventIdIn(filteredEventIds);
		realtimeEventService.publish(EventType.EVENT_RECLASSIFIED, "CASE-EVENT", caseId, updatedEventDtos);
	}

	@Transactional
	public void updateEventsStatusByCaseId(Long caseId, EventStatus targetStatus) {
		List<Event> events = eventRepository.findByCaze_Id(caseId);
		for (Event e : events) {
			e.setStatus(targetStatus);
		}
	}
}