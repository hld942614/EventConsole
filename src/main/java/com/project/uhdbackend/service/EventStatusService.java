package com.project.uhdbackend.service;

import java.time.OffsetDateTime;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhdbackend.dto.EventDTO;
import com.project.uhdbackend.entity.Event;
import com.project.uhdbackend.enums.EventStatus;
import com.project.uhdbackend.realtime.event.EventType;
import com.project.uhdbackend.realtime.service.RealtimeEventService;
import com.project.uhdbackend.repository.EventRepository;
import com.project.uhdbackend.utils.CommentStatus;

@Service
public class EventStatusService {

	private final EventRepository eventRepository;
	private final RealtimeEventService realtimeEventService;

	public EventStatusService(EventRepository eventRepository, RealtimeEventService realtimeEventService) {
		this.eventRepository = eventRepository;
		this.realtimeEventService = realtimeEventService;
	}

	/**
	 * 使用者「開啟事件」時呼叫。 - UNREAD -> ACKNOWLEDGED（推播 EVENT_READ
	 */
	@Transactional
	public Event markAsRead(String eventId, String readBy) {
		Event event = getEventOrThrow(eventId);
		EventStatus current = event.getEventStatus();

		if (current != EventStatus.UNREAD) {
			return event;
		}

		event.setReadBy(readBy);
		event.setAcknowledgedAt(OffsetDateTime.now());
		event.setEventStatus(EventStatus.ACKNOWLEDGED);
		return saveAndPublish(event, EventType.EVENT_READ);
	}

	/**
	 * 事件被分類進 Case 時呼叫（自動分類 / 手動加入皆走此處）。 只要目前不是終態（RESOLVED/CLOSED）也還沒是
	 * CLASSIFIED，一律釘死成 CLASSIFIED， 後續讀取/留言不再改變它，只交由所屬 Case 的 resolve/close 或移出 Case
	 * 來驅動。
	 */
	@Transactional
	public void classifyIntoCase(Event event) {
		EventStatus current = event.getEventStatus();
		if (current == EventStatus.RESOLVED || current == EventStatus.CLOSED || current == EventStatus.CLASSIFIED) {
			return;
		}
		event.setEventStatus(EventStatus.CLASSIFIED);
		saveAndPublish(event, EventType.EVENT_CLASSIFIED);
	}

	/**
	 * 留言時呼叫。理論上留言前必先開啟事件，這裡對 UNREAD 保留防呆 （留言動作本身視同一次已讀），推播統一用 EVENT_PROCESSING。
	 */
	@Transactional
	public void ensureProcessingOnComment(Event event, String actor) {
		EventStatus current = event.getEventStatus();

		if (current == EventStatus.UNREAD) {
			event.setReadBy(actor);
			event.setAcknowledgedAt(OffsetDateTime.now());
			event.setEventStatus(EventStatus.PROCESSING);
			saveAndPublish(event, EventType.EVENT_PROCESSING);
		} else if (current == EventStatus.ACKNOWLEDGED) {
			event.setEventStatus(EventStatus.PROCESSING);
			saveAndPublish(event, EventType.EVENT_PROCESSING);
		}
	}

	/**
	 * 事件從 Case 移出、且不再屬於任何 Case 時呼叫。 只有還在凍結態 CLASSIFIED（尚未被任何 Case resolve/close
	 * 過）的事件才重置回 UNREAD； 若已經被 cascade 成 RESOLVED/CLOSED，維持原狀不動。
	 */
	@Transactional
	public void unclassifyFromCase(Event event, boolean stillHasCase) {
		if (stillHasCase || event.getEventStatus() != EventStatus.CLASSIFIED) {
			return;
		}
		event.setEventStatus(EventStatus.UNREAD);
		saveAndPublish(event, EventType.EVENT_UNCLASSIFIED);
	}

	@Transactional
	public Event resolve(String eventId, String resolvedBy) {
		Event event = getEventOrThrow(eventId);
		if (!EventStatus.RESOLVED.isStrictUpgradeFrom(event.getEventStatus())) {
			throw new IllegalStateException(
					"不允許的狀態轉換: " + event.getEventStatus() + " -> RESOLVED (eventId=" + eventId + ")");
		}
		event.setResolvedAt(OffsetDateTime.now());
		event.setResolvedBy(resolvedBy);
		event.setEventStatus(EventStatus.RESOLVED);
		event.setProcessingDetailStatus(CommentStatus.RESOLVED);
		return saveAndPublish(event, EventType.EVENT_RESOLVED);
	}

	@Transactional
	public Event close(String eventId, String closedBy) {
		Event event = getEventOrThrow(eventId);
		if (!EventStatus.CLOSED.isStrictUpgradeFrom(event.getEventStatus())) {
			throw new IllegalStateException(
					"不允許的狀態轉換: " + event.getEventStatus() + " -> CLOSED (eventId=" + eventId + ")");
		}
		event.setClosedAt(OffsetDateTime.now());
		event.setClosedBy(closedBy);
		event.setEventStatus(EventStatus.CLOSED);
		event.setProcessingDetailStatus(CommentStatus.CLOSED);
		return saveAndPublish(event, EventType.EVENT_CLOSED);
	}

	/** 供 Case resolve 時 cascade 呼叫：只有還在 CLASSIFIED 的事件才會被推動 */
	@Transactional
	public void cascadeResolveFromCase(Event event, String resolvedBy) {
		if (event.getEventStatus() != EventStatus.CLASSIFIED) {
			return;
		}
		event.setResolvedAt(OffsetDateTime.now());
		event.setResolvedBy(resolvedBy);
		event.setEventStatus(EventStatus.RESOLVED);
		saveAndPublish(event, EventType.EVENT_RESOLVED);
	}

	/** 供 Case close 時 cascade 呼叫：CLASSIFIED 或（已被上一步 cascade 過的）RESOLVED 都會被推動 */
	@Transactional
	public void cascadeCloseFromCase(Event event, String closedBy) {
		EventStatus current = event.getEventStatus();
		if (current != EventStatus.CLASSIFIED && current != EventStatus.RESOLVED) {
			return;
		}
		event.setClosedAt(OffsetDateTime.now());
		event.setClosedBy(closedBy);
		event.setEventStatus(EventStatus.CLOSED);
		saveAndPublish(event, EventType.EVENT_CLOSED);
	}

	/**
	 * 留言時同步指定的狀態轉換。 targetStatus 為 null 或 PROCESSING：沿用原本 ensureProcessingOnComment
	 * 的自動判斷邏輯 （只有 UNREAD/ACKNOWLEDGED 會被推進到 PROCESSING，其餘狀態留言不影響狀態）。 targetStatus 為
	 * RESOLVED / CLOSED：走嚴格升級檢查，不合法的轉換會拋 IllegalStateException。
	 * 其餘狀態（UNREAD/ACKNOWLEDGED/CLASSIFIED/INVALID）視為非法輸入。
	 */
	@Transactional
	public void applyCommentStatus(Event event, EventStatus targetStatus, String actor) {
		if (targetStatus == null || targetStatus == EventStatus.PROCESSING) {
			ensureProcessingOnComment(event, actor);
			return;
		}
		switch (targetStatus) {
		case RESOLVED:
			doResolve(event, actor);
			break;
		case CLOSED:
			doClose(event, actor);
			break;
		default:
			throw new IllegalArgumentException("留言無法將 Event 轉換為狀態: " + targetStatus);
		}
	}

	private Event doResolve(Event event, String resolvedBy) {
		if (!EventStatus.RESOLVED.isStrictUpgradeFrom(event.getEventStatus())) {
			throw new IllegalStateException(
					"不允許的狀態轉換: " + event.getEventStatus() + " -> RESOLVED (eventId=" + event.getEventId() + ")");
		}
		event.setResolvedAt(OffsetDateTime.now());
		event.setResolvedBy(resolvedBy);
		event.setEventStatus(EventStatus.RESOLVED);
		return saveAndPublish(event, EventType.EVENT_RESOLVED);
	}

	private Event doClose(Event event, String closedBy) {
		if (!EventStatus.CLOSED.isStrictUpgradeFrom(event.getEventStatus())) {
			throw new IllegalStateException(
					"不允許的狀態轉換: " + event.getEventStatus() + " -> CLOSED (eventId=" + event.getEventId() + ")");
		}
		event.setClosedAt(OffsetDateTime.now());
		event.setClosedBy(closedBy);
		event.setEventStatus(EventStatus.CLOSED);
		return saveAndPublish(event, EventType.EVENT_CLOSED);
	}

	private Event saveAndPublish(Event event, EventType type) {
		Event saved = eventRepository.save(event);
		EventDTO dto = new EventDTO(saved);
		realtimeEventService.publish(type, "EVENT", saved.getEventId(), dto);
		return saved;
	}

	private Event getEventOrThrow(String eventId) {
		return eventRepository.findByEventId(eventId)
				.orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
	}
}