package com.project.uhd.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhd.authentication.CustomUserDetails;
import com.project.uhd.dto.EventDTO;
import com.project.uhd.entity.Event;
import com.project.uhd.enums.ChangeSource;
import com.project.uhd.enums.EventStatus;
import com.project.uhd.enums.StatusLogTargetType;
import com.project.uhd.realtime.event.EventType;
import com.project.uhd.realtime.service.RealtimeEventService;
import com.project.uhd.repository.EventRepository;

@Service
public class EventStatusService {

	private final EventRepository eventRepository;
	private final RealtimeEventService realtimeEventService;
	private final StatusLogService statusLogService;

	public EventStatusService(EventRepository eventRepository, RealtimeEventService realtimeEventService,
			StatusLogService statusLogService) {
		this.eventRepository = eventRepository;
		this.realtimeEventService = realtimeEventService;
		this.statusLogService = statusLogService;
	}

	@Transactional
	public Event markAsRead(String eventId, CustomUserDetails currentUser) {
		Event event = getEventOrThrow(eventId);
		if (event.getStatus() != EventStatus.UNREAD) {
			return event;
		}
		setStatusWithLog(event, EventStatus.ACKNOWLEDGED, currentUser, ChangeSource.USER, null);
		return saveAndPublish(event, EventType.EVENT_READ, "EVENT");
	}

	/** currentUser 為 null 時代表系統規則自動分類；有值時代表人為手動加入。 */
	@Transactional
	public void classifyIntoCase(Event event, CustomUserDetails currentUser, ChangeSource source) {
		EventStatus current = event.getStatus();
		if (current == EventStatus.RESOLVED || current == EventStatus.CLOSED || current == EventStatus.CLASSIFIED) {
			return;
		}
		setStatusWithLog(event, EventStatus.CLASSIFIED, currentUser, source, null);
	}

	@Transactional
	public void unclassifyFromCase(Event event, CustomUserDetails currentUser) {
		if (event.getStatus() != EventStatus.CLASSIFIED) {
			return;
		}
		setStatusWithLog(event, EventStatus.UNREAD, currentUser, ChangeSource.USER, null);
	}

	@Transactional
	public Event resolve(String eventId, CustomUserDetails currentUser) {
		Event event = getEventOrThrow(eventId);
		return doResolve(event, currentUser, ChangeSource.USER, null);
	}

	@Transactional
	public Event close(String eventId, CustomUserDetails currentUser) {
		Event event = getEventOrThrow(eventId);
		return doClose(event, currentUser, ChangeSource.USER, null);
	}

	@Transactional
	public void cascadeResolveFromCase(Event event, CustomUserDetails currentUser) {
		if (event.getStatus() != EventStatus.CLASSIFIED) {
			return;
		}
		setStatusWithLog(event, EventStatus.RESOLVED, currentUser, ChangeSource.SYSTEM, null);
		saveAndPublish(event, EventType.EVENT_RESOLVED, "EVENT");
	}

	@Transactional
	public void cascadeCloseFromCase(Event event, CustomUserDetails currentUser) {
		EventStatus current = event.getStatus();
		if (current != EventStatus.CLASSIFIED && current != EventStatus.RESOLVED) {
			return;
		}
		setStatusWithLog(event, EventStatus.CLOSED, currentUser, ChangeSource.SYSTEM, null);
		saveAndPublish(event, EventType.EVENT_CLOSED, "EVENT");
	}

	/**
	 * 留言時呼叫。無論狀態實際上有沒有變化，只要留言帶了 status，就記一筆活動歷程
	 * （例如同樣是 INVESTIGATING 底下，內容不同的兩次留言，各自都要留痕）。
	 */
	@Transactional
	public void applyCommentStatus(Event event, EventStatus targetStatus, CustomUserDetails currentUser,
			Long relatedCommentId) {
		if (targetStatus == EventStatus.RESOLVED) {
			doResolve(event, currentUser, ChangeSource.USER, relatedCommentId);
			return;
		}
		if (targetStatus == EventStatus.CLOSED) {
			doClose(event, currentUser, ChangeSource.USER, relatedCommentId);
			return;
		}

		EventStatus effectiveTarget = (targetStatus == null) ? EventStatus.PROCESSING : targetStatus;
		if (!effectiveTarget.isProcessingPhase()) {
			throw new IllegalArgumentException("留言無法將 Event 轉換為狀態: " + targetStatus);
		}

		EventStatus current = event.getStatus();
		if (current == EventStatus.UNREAD || current == EventStatus.ACKNOWLEDGED || current.isProcessingPhase()) {
			setStatusWithLog(event, effectiveTarget, currentUser, ChangeSource.USER, relatedCommentId);
			saveAndPublish(event, EventType.EVENT_PROCESSING, "EVENT");
		}
	}

	private Event doResolve(Event event, CustomUserDetails currentUser, ChangeSource source, Long relatedCommentId) {
		if (!EventStatus.RESOLVED.isStrictUpgradeFrom(event.getStatus())) {
			throw new IllegalStateException(
					"不允許的狀態轉換: " + event.getStatus() + " -> RESOLVED (eventId=" + event.getEventId() + ")");
		}
		setStatusWithLog(event, EventStatus.RESOLVED, currentUser, source, relatedCommentId);
		return saveAndPublish(event, EventType.EVENT_RESOLVED, "EVENT");
	}

	private Event doClose(Event event, CustomUserDetails currentUser, ChangeSource source, Long relatedCommentId) {
		if (!EventStatus.CLOSED.isStrictUpgradeFrom(event.getStatus())) {
			throw new IllegalStateException(
					"不允許的狀態轉換: " + event.getStatus() + " -> CLOSED (eventId=" + event.getEventId() + ")");
		}
		setStatusWithLog(event, EventStatus.CLOSED, currentUser, source, relatedCommentId);
		return saveAndPublish(event, EventType.EVENT_CLOSED, "EVENT");
	}

	private void setStatusWithLog(Event event, EventStatus newStatus, CustomUserDetails currentUser,
			ChangeSource source, Long relatedCommentId) {
		event.setStatus(newStatus);
		statusLogService.log(StatusLogTargetType.EVENT, event.getId(), newStatus.name(),
				currentUser != null ? currentUser.getChineseName() : null,
				currentUser != null ? currentUser.getId() : null, source, relatedCommentId);
	}

	private Event saveAndPublish(Event event, EventType type, String aggType) {
		Event saved = eventRepository.save(event);
		EventDTO dto = new EventDTO(saved);
		realtimeEventService.publish(type, aggType, saved.getEventId(), dto);
		return saved;
	}

	private Event getEventOrThrow(String eventId) {
		return eventRepository.findByEventId(eventId)
				.orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
	}
}