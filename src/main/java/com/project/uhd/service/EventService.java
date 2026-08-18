package com.project.uhd.service;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhd.dto.AttachmentInfoDTO;
import com.project.uhd.dto.EventDTO;
import com.project.uhd.dto.StatusLogDTO;
import com.project.uhd.entity.Event;
import com.project.uhd.enums.ChangeSource;
import com.project.uhd.enums.EventStatus;
import com.project.uhd.enums.StatusLogTargetType;
import com.project.uhd.exception.InvalidEventPayloadException;
import com.project.uhd.realtime.event.EventType;
import com.project.uhd.realtime.service.RealtimeEventService;
import com.project.uhd.repository.EventQueryRepository;
import com.project.uhd.repository.EventRepository;
import com.project.uhd.repository.UploadedFileRepository;

import jakarta.mail.Address;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;

/**
 * 新格式告警事件（.eml，本文為 JSON）的解析與落地。 完全獨立於既有 MessageService /
 * transferMailToMsg，不共用、不影響現有流程。
 */
@Service
public class EventService {

	private static final Logger log = LoggerFactory.getLogger(EventService.class);
	private final EventRepository eventRepository;
	private final ModuleCodeResolver moduleCodeResolver;
	private final EventIdGeneratorService eventIdGeneratorService;
	private final AttachmentService attachmentService;
	private final EventQueryRepository eventQueryRepository;
	private final RealtimeEventService realtimeEventService;
	private final CaseClassifierService caseClassifierService;
	private final UploadedFileRepository uploadedFileRepository;
	private final StatusLogService statusLogService;

	public EventService(EventRepository eventRepository, ModuleCodeResolver moduleCodeResolver,
			EventIdGeneratorService eventIdGeneratorService,
			AttachmentService attachmentService, EventQueryRepository eventQueryRepository,
			RealtimeEventService realtimeEventService, CaseClassifierService caseClassifierService, 
			UploadedFileRepository uploadedFileRepository, StatusLogService statusLogService) {
		this.eventRepository = eventRepository;
		this.moduleCodeResolver = moduleCodeResolver;
		this.eventIdGeneratorService = eventIdGeneratorService;
		this.attachmentService = attachmentService;
		this.eventQueryRepository = eventQueryRepository;
		this.realtimeEventService = realtimeEventService;
		this.caseClassifierService = caseClassifierService;
		this.uploadedFileRepository = uploadedFileRepository;
		this.statusLogService = statusLogService;
	}

	@Transactional
	public Event processNewEvent(String rawEmlContent) {
		try {
			Event saved = transferMailToEvent(rawEmlContent);
			caseClassifierService.classifyAndAddEventToCases(saved);
			EventDTO dto = new EventDTO(saved);
			realtimeEventService.publish(EventType.EVENT_CREATED, "EVENT", saved.getEventId(), dto);
			return saved;
		} catch (InvalidEventPayloadException e) {
			// 無效資料先不儲存
			log.error("Event 處理失敗，原始資料：{}", rawEmlContent, e);
			return null;
		}
	}

	public List<EventDTO> getEventsByFilters(List<EventStatus> statusArray, String subject, String moduleCode,
			String sender, String content, String startDay, String endDay) {
		return eventQueryRepository.getEventsByFilters(statusArray, subject, moduleCode, sender, content, startDay,
				endDay);
	}

	@Transactional(readOnly = true)
	public Optional<EventDTO> getEventDTO(String eventId) {
		return eventQueryRepository.findByEventId(eventId).map(this::attachSopFiles).map(this::attachStatusLog);
	}

	@Transactional
	public void changeEventStatus(String eventId, EventStatus status) {
		Event event = eventRepository.findByEventId(eventId)
				.orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
		event.setStatus(status);

		EventDTO dto = new EventDTO(event);
		realtimeEventService.publish(EventType.EVENT_UPDATED, "EVENT", dto.getId(), dto);
	}

	public Event transferMailToEvent(String input) throws InvalidEventPayloadException {
		MimeMessage mm;
		try {
			Properties props = System.getProperties();
			Session session = Session.getDefaultInstance(props, null);
			mm = new MimeMessage(session, new ByteArrayInputStream(input.getBytes()));
		} catch (Exception e) {
			throw new InvalidEventPayloadException("eml 解析失敗: " + e.getMessage(), e);
		}

		JSONObject data;
		try {
			data = extractJsonBody(mm);
		} catch (Exception e) {
			throw new InvalidEventPayloadException("mail 本文中找不到有效 JSON 內容");
		}

		List<AttachmentInfoDTO> attachments;
		try {
			attachments = attachmentService.extractAttachments(mm);
		} catch (Exception e) {
			throw new InvalidEventPayloadException("attachment 解析失敗");
		}

		// ---- Mail 層級 ----
		String subject;
		List<String> senderList = new ArrayList<>();
		List<String> receiverList = new ArrayList<>();
		OffsetDateTime receivedAt;
		try {
			subject = mm.getSubject();

			if (mm.getFrom() != null) {
				for (Address address : mm.getFrom()) {
					senderList.add(address.toString());
				}
			}

			Address[] recipients = mm.getRecipients(RecipientType.TO);
			if (recipients != null) {
				for (Address address : recipients) {
					receiverList.add(address.toString());
				}
			}

			Date mailDate = mm.getSentDate();
			receivedAt = (mailDate != null) ? mailDate.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime()
					: OffsetDateTime.now(); // fallback：Date header 缺失時，用消費當下時間
		} catch (Exception e) {
			throw new InvalidEventPayloadException("mail header 解析失敗: " + e.getMessage(), e);
		}

		// ---- 內容層級 ----
		String alertCode = data.optString("alertCode", "");
		if (alertCode.isBlank()) {
			throw new InvalidEventPayloadException("JSON 內容缺少 alertCode");
		}

		JSONObject source = data.optJSONObject("source");
		String environment = (source != null) ? source.optString("environment", "") : "";
		if (environment.isBlank()) {
			throw new InvalidEventPayloadException("environment 為必要欄位，不可為 null: alertCode=" + alertCode);
		}
		String sourceHost = source.optString("host", null);
		String sourceIp = source.optString("ip", null);

		String severity = data.optString("severity", null);
		String title = data.optString("title", null);
		String messageContent = data.optString("message", null);

		if (!data.has("occurredAt")) {
			throw new InvalidEventPayloadException("JSON 內容缺少 occurredAt: alertCode=" + alertCode);
		}
		OffsetDateTime occurredAt;
		try {
			occurredAt = Instant.ofEpochMilli(data.getLong("occurredAt")).atOffset(ZoneOffset.UTC);
		} catch (Exception e) {
			throw new InvalidEventPayloadException("occurredAt 格式錯誤: " + data.getString("occurredAt"), e);
		}

		String details = null;
		if (data.has("details")) {
			details = data.get("details").toString();
		}

		// ---- moduleCode 推導 + eventId 產生 ----
		String moduleCode = moduleCodeResolver.resolve(alertCode);
		String eventId = eventIdGeneratorService.generate(moduleCode, environment, LocalDate.now());

		// ---- 組成 Event ----
		Event event = new Event();
		event.setEventId(eventId);

		event.setSubject(subject);
		event.setSender(senderList.toString());
		event.setReceiver(receiverList.toString());
		event.setReceivedAt(receivedAt);

		event.setAlertCode(alertCode);
		event.setModuleCode(moduleCode);
		event.setEnvironment(environment);
		event.setSourceHost(sourceHost);
		event.setSourceIp(sourceIp);
		event.setSeverity(severity);
		event.setTitle(title);
		event.setMessageContent(messageContent);
		event.setOccurredAt(occurredAt);
		event.setDetails(details);

		event.setRawJsonPayload(data.toString());
		event.setRawEmlContent(input); // 若不需要保留完整 eml，可拿掉這行

		event.setHasAttachment(attachments.isEmpty() ? "N" : "Y");

		event = eventRepository.save(event);
		attachmentService.storeAttachments(event, attachments);

		statusLogService.log(StatusLogTargetType.EVENT, event.getId(), EventStatus.UNREAD.name(),
				null, null, ChangeSource.SYSTEM, null);
		
		return eventRepository.save(event);
	}

	// ---- helpers ----

//	private JSONObject extractJsonBody(String rawEmlContent) throws Exception {
//		Properties props = System.getProperties();
//		Session session = Session.getDefaultInstance(props, null);
//		MimeMessage mm = new MimeMessage(session, new ByteArrayInputStream(rawEmlContent.getBytes()));
//		return extractJsonBody(mm);
//	}

	/**
	 * 沿用舊有 transferMailToMsg 的 multipart 拆解邏輯：只處理到兩層（mp -> mp2），未做遞迴。
	 */
	private JSONObject extractJsonBody(MimeMessage mm) throws Exception {
		Multipart mp = (Multipart) mm.getContent();
		int bodynum = mp.getCount();
		JSONObject data = new JSONObject();

		for (int partCount = 0; partCount < bodynum; partCount++) {
			MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(partCount);
			if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
				continue; // 附件（略）
			}
			Object content = part.getContent();
			String contentType = part.getContentType();
			if (content instanceof Multipart) {
				Multipart mp2 = (Multipart) content;
				for (int i = 0; i < mp2.getCount(); i++) {
					MimeBodyPart part2 = (MimeBodyPart) mp2.getBodyPart(i);
					String part2Content = part2.getContent().toString();
					String part2ContentType = part2.getContentType();
					if (part2ContentType.contains("plain") && isValidJson(part2Content)) {
						data = new JSONObject(part2Content);
					}
				}
			} else if (content instanceof String && contentType.contains("plain") && isValidJson(content.toString())) {
				data = new JSONObject(content.toString());
			}
		}
		return data;
	}

	private boolean isValidJson(String input) {
		try {
			new JSONObject(input);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private EventDTO attachSopFiles(EventDTO dto) {
		String alertCode = dto.getAlertCode();
		if (alertCode != null && !alertCode.isBlank()) {
			dto.setSopFileList(uploadedFileRepository.findByAlertCodeOrderByTimestampDesc(alertCode));
		}
		return dto;
	}
	
	private EventDTO attachStatusLog(EventDTO dto) {
		dto.setLogList(getStatusHistory(dto.getEventId(),"ASC"));
		return dto;
	}
	
	@Transactional(readOnly = true)
	public List<StatusLogDTO> getStatusHistory(String eventId, String order) {
		Event event = eventRepository.findByEventId(eventId)
				.orElseThrow(() -> new NoSuchElementException("Event not found: " + eventId));
		return statusLogService.getHistory(StatusLogTargetType.EVENT, event.getId(), order);
	}
}