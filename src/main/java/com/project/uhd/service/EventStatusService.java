package com.project.uhd.service;

import java.time.OffsetDateTime;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhd.authentication.CustomUserDetails;
import com.project.uhd.dto.EventDTO;
import com.project.uhd.entity.Event;
import com.project.uhd.enums.EventStatus;
import com.project.uhd.realtime.event.EventType;
import com.project.uhd.realtime.service.RealtimeEventService;
import com.project.uhd.repository.EventRepository;

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
	public Event markAsRead(String eventId, CustomUserDetails currentUser) {
		Event event = getEventOrThrow(eventId);
		if (event.getStatus() != EventStatus.UNREAD) {
			return event;
		}
		event.setReadBy(currentUser.getChineseName());
		event.setReadById(currentUser.getId());
		event.setAcknowledgedAt(OffsetDateTime.now());
		event.setStatus(EventStatus.ACKNOWLEDGED);
		return saveAndPublish(event, EventType.EVENT_READ, "EVENT");
	}

	/**
	 * 事件被分類進 Case 時呼叫（自動分類 / 手動加入皆走此處）。 只要目前不是終態（RESOLVED/CLOSED）也還沒是
	 * CLASSIFIED，一律釘死成 CLASSIFIED， 後續讀取/留言不再改變它，只交由所屬 Case 的 resolve/close 或移出 Case
	 * 來驅動。
	 */
	@Transactional
	public void classifyIntoCase(Event event) {
		EventStatus current = event.getStatus();
		if (current == EventStatus.RESOLVED || current == EventStatus.CLOSED || current == EventStatus.CLASSIFIED) {
			return;
		}
		event.setStatus(EventStatus.CLASSIFIED);
//		saveAndPublish(event, EventType.EVENT_CLASSIFIED, "Case-Event");
	}

	/**
	 * 留言時呼叫。理論上留言前必先開啟事件，這裡對 UNREAD 保留防呆 （留言動作本身視同一次已讀），推播統一用 EVENT_PROCESSING。
	 */
	@Transactional
	public void ensureProcessingOnComment(Event event, CustomUserDetails currentUser) {
		EventStatus current = event.getStatus();
		if (current == EventStatus.UNREAD) {
			event.setReadBy(currentUser.getChineseName());
			event.setReadById(currentUser.getId());
			event.setAcknowledgedAt(OffsetDateTime.now());
			event.setStatus(EventStatus.PROCESSING);
			saveAndPublish(event, EventType.EVENT_PROCESSING, "EVENT");
		} else if (current == EventStatus.ACKNOWLEDGED) {
			event.setStatus(EventStatus.PROCESSING);
			saveAndPublish(event, EventType.EVENT_PROCESSING, "EVENT");
		}
	}

	/**
	 * 事件從 Case 移出、且不再屬於任何 Case 時呼叫。 只有還在凍結態 CLASSIFIED（尚未被任何 Case resolve/close
	 * 過）的事件才重置回 UNREAD； 若已經被 cascade 成 RESOLVED/CLOSED，維持原狀不動。
	 */
	@Transactional
	public void unclassifyFromCase(Event event) {
		if (event.getStatus() != EventStatus.CLASSIFIED) {
			return;
		}
		event.setStatus(EventStatus.UNREAD);
	}

	@Transactional
	public Event resolve(String eventId, CustomUserDetails currentUser) {
		Event event = getEventOrThrow(eventId);
		if (!EventStatus.RESOLVED.isStrictUpgradeFrom(event.getStatus())) {
			throw new IllegalStateException("不允許的狀態轉換: " + event.getStatus() + " -> RESOLVED (eventId=" + eventId + ")");
		}
		event.setResolvedAt(OffsetDateTime.now());
		event.setResolvedBy(currentUser.getChineseName());
		event.setResolvedById(currentUser.getId());
		event.setStatus(EventStatus.RESOLVED);
		event.setProcessingDetailStatus(null);
		return saveAndPublish(event, EventType.EVENT_RESOLVED, "EVENT");
	}

	@Transactional
	public Event close(String eventId, CustomUserDetails currentUser) {
		Event event = getEventOrThrow(eventId);
		if (!EventStatus.CLOSED.isStrictUpgradeFrom(event.getStatus())) {
			throw new IllegalStateException("不允許的狀態轉換: " + event.getStatus() + " -> CLOSED (eventId=" + eventId + ")");
		}
		event.setClosedAt(OffsetDateTime.now());
		event.setClosedBy(currentUser.getChineseName());
		event.setClosedById(currentUser.getId());
		event.setStatus(EventStatus.CLOSED);
		event.setProcessingDetailStatus(null);
		return saveAndPublish(event, EventType.EVENT_CLOSED, "EVENT");
	}

	@Transactional
	public void cascadeResolveFromCase(Event event, CustomUserDetails currentUser) {
		if (event.getStatus() != EventStatus.CLASSIFIED) {
			return;
		}
		event.setResolvedAt(OffsetDateTime.now());
		event.setResolvedBy(currentUser.getChineseName());
		event.setResolvedById(currentUser.getId());
		event.setStatus(EventStatus.RESOLVED);
		event.setProcessingDetailStatus(null);
		saveAndPublish(event, EventType.EVENT_RESOLVED, "EVENT");
	}

	@Transactional
	public void cascadeCloseFromCase(Event event, CustomUserDetails currentUser) {
		EventStatus current = event.getStatus();
		if (current != EventStatus.CLASSIFIED && current != EventStatus.RESOLVED) {
			return;
		}
		event.setClosedAt(OffsetDateTime.now());
		event.setClosedBy(currentUser.getChineseName());
		event.setClosedById(currentUser.getId());
		event.setStatus(EventStatus.CLOSED);
		event.setProcessingDetailStatus(null);
		saveAndPublish(event, EventType.EVENT_CLOSED, "EVENT");
	}

	/**
	 * 留言時同步指定的狀態轉換。 targetStatus 為 null 或 PROCESSING：沿用原本 ensureProcessingOnComment
	 * 的自動判斷邏輯 （只有 UNREAD/ACKNOWLEDGED 會被推進到 PROCESSING，其餘狀態留言不影響狀態）。 targetStatus 為
	 * RESOLVED / CLOSED：走嚴格升級檢查，不合法的轉換會拋 IllegalStateException。
	 * 其餘狀態（UNREAD/ACKNOWLEDGED/CLASSIFIED/INVALID）視為非法輸入。
	 */
	@Transactional
	public void applyCommentStatus(Event event, EventStatus targetStatus, CustomUserDetails currentUser) {
		if (targetStatus == null || targetStatus == EventStatus.PROCESSING) {
			ensureProcessingOnComment(event, currentUser);
			return;
		}
		switch (targetStatus) {
		case RESOLVED:
			doResolve(event, currentUser);
			break;
		case CLOSED:
			doClose(event, currentUser);
			break;
		default:
			throw new IllegalArgumentException("留言無法將 Event 轉換為狀態: " + targetStatus);
		}
	}

	private Event doResolve(Event event, CustomUserDetails currentUser) {
		if (!EventStatus.RESOLVED.isStrictUpgradeFrom(event.getStatus())) {
			throw new IllegalStateException("不允許的狀態轉換: " + event.getStatus() + " -> RESOLVED (eventId=" + event.getEventId() + ")");
		}
		event.setResolvedAt(OffsetDateTime.now());
		event.setResolvedBy(currentUser.getChineseName());
		event.setResolvedById(currentUser.getId());
		event.setStatus(EventStatus.RESOLVED);
		event.setProcessingDetailStatus(null);
		return saveAndPublish(event, EventType.EVENT_RESOLVED, "EVENT");
	}

	private Event doClose(Event event, CustomUserDetails currentUser) {
		if (!EventStatus.CLOSED.isStrictUpgradeFrom(event.getStatus())) {
			throw new IllegalStateException("不允許的狀態轉換: " + event.getStatus() + " -> CLOSED (eventId=" + event.getEventId() + ")");
		}
		event.setClosedAt(OffsetDateTime.now());
		event.setClosedBy(currentUser.getChineseName());
		event.setClosedById(currentUser.getId());
		event.setStatus(EventStatus.CLOSED);
		event.setProcessingDetailStatus(null);
		return saveAndPublish(event, EventType.EVENT_CLOSED, "EVENT");
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