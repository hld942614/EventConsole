package com.project.uhdbackend.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhdbackend.entity.Case;
import com.project.uhdbackend.entity.CaseEvent;
import com.project.uhdbackend.entity.CaseEventId;
import com.project.uhdbackend.entity.Event;
import com.project.uhdbackend.enums.CaseStatus;
import com.project.uhdbackend.enums.EventStatus;
import com.project.uhdbackend.realtime.event.EventType;
import com.project.uhdbackend.realtime.service.RealtimeEventService;
import com.project.uhdbackend.repository.CaseEventRepository;
import com.project.uhdbackend.repository.CaseRepository;
import com.project.uhdbackend.repository.EventRepository;

/**
 * 對外（Controller）一律用業務鍵 EVENT_ID（String）操作， 內部因為 CaseEvent join table 存的是
 * MUHD_EVENT 的內部 PK（EVENT_PK）， 所以需要先用 eventRepository 把 EVENT_ID 轉成內部
 * PK，才能寫入/刪除 join table。
 */
@Service
public class CaseEventService {

	private final CaseEventRepository caseEventRepository;
	private final EventRepository eventRepository;
	private final RealtimeEventService realtimeEventService;
	private final CaseRepository caseRepository;
	private final EventStatusService eventStatusService;

	public CaseEventService(CaseEventRepository caseEventRepository, EventRepository eventRepository,
			RealtimeEventService realtimeEventService, CaseRepository caseRepository,
			EventStatusService eventStatusService) {
		this.caseEventRepository = caseEventRepository;
		this.eventRepository = eventRepository;
		this.realtimeEventService = realtimeEventService;
		this.caseRepository = caseRepository;
		this.eventStatusService = eventStatusService;
	}

	@Transactional
	public void addEventsToCase(Long caseId, List<String> eventIds, String assignedTo) {
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

		for (Event event : foundEvents) {
			CaseEventId id = new CaseEventId(caseId, event.getId());
			if (!caseEventRepository.existsById(id)) {
				caseEventRepository.save(new CaseEvent(caseId, event.getId()));
			}

			eventStatusService.classifyIntoCase(event);
		}
	}

	@Transactional
	public void removeCaseEvents(Long caseId, List<String> eventIds) {
		if (caseId == null || eventIds == null || eventIds.isEmpty()) {
			return;
		}
		List<String> filteredEventIds = eventIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
		if (filteredEventIds.isEmpty()) {
			return;
		}

		List<Event> foundEvents = eventRepository.findAllByEventIdIn(filteredEventIds);

		List<CaseEventId> ids = foundEvents.stream().map(e -> new CaseEventId(caseId, e.getId()))
				.collect(Collectors.toList());

		caseEventRepository.deleteAllByIdInBatch(ids);

		// 逐一檢查移出這個 Case 後，是否還屬於其他 Case
		for (Event event : foundEvents) {
			boolean stillHasCase = caseEventRepository.findByEventId(event.getId()).stream()
					.anyMatch(ce -> !ce.getCaseId().equals(caseId));
			eventStatusService.unclassifyFromCase(event, stillHasCase);
		}

		realtimeEventService.publish(EventType.EVENT_RECLASSIFIED, "CASE-EVENT", caseId, filteredEventIds);
	}

	public List<CaseEvent> getEventsByCaseId(Long caseId) {
		return caseEventRepository.findByCaseId(caseId);
	}

	@Transactional
	public void updateEventsStatusByCaseId(Long caseId, EventStatus targetStatus) {
		List<CaseEvent> relations = caseEventRepository.findByCaseId(caseId);
		if (relations.isEmpty()) {
			return;
		}
		List<Long> eventPks = relations.stream().map(CaseEvent::getEventId).distinct().collect(Collectors.toList());
		List<Event> events = eventRepository.findAllById(eventPks);
		for (Event e : events) {
			e.setEventStatus(targetStatus);
		}
	}
}