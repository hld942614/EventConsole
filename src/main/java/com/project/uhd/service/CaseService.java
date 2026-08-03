package com.project.uhd.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhd.dto.CaseCreateRequest;
import com.project.uhd.dto.CaseDTO;
import com.project.uhd.dto.CaseUpdateRequest;
import com.project.uhd.dto.EventDTO;
import com.project.uhd.entity.Case;
import com.project.uhd.entity.Event;
import com.project.uhd.enums.CaseStatus;
import com.project.uhd.realtime.event.EventType;
import com.project.uhd.realtime.service.RealtimeEventService;
import com.project.uhd.repository.CaseEventRepository;
import com.project.uhd.repository.CaseRepository;
import com.project.uhd.repository.EventQueryRepository;
import com.project.uhd.util.CommentStatus;

@Service
public class CaseService {

	private final CaseRepository caseRepository;
	private final RealtimeEventService realtimeEventService;
	private EntityManager entityManager;
	private final EventQueryRepository eventQueryRepository;
	private final CaseEventRepository caseEventRepository;
	private final EventStatusService eventStatusService;

	public CaseService(CaseRepository caseRepository, RealtimeEventService realtimeEventService,
			EntityManager entityManager, EventQueryRepository eventQueryRepository,
			CaseEventRepository caseEventRepository, EventStatusService eventStatusService) {
		this.caseRepository = caseRepository;
		this.realtimeEventService = realtimeEventService;
		this.entityManager = entityManager;
		this.eventQueryRepository = eventQueryRepository;
		this.caseEventRepository = caseEventRepository;
		this.eventStatusService = eventStatusService;
	}

	@Transactional
	public CaseDTO addCase(CaseCreateRequest request) {
		Case newCase = new Case();
		newCase.setName(request.getName());
		newCase.setDescription(request.getDescription());
		newCase.setLogic(request.getLogic());
		newCase.setConditions(request.getConditions());
		newCase.setCreator(request.getCreator());
		newCase.setRuleEnabled(request.getRuleEnabled() != null ? request.getRuleEnabled() : false);

		Case saved = caseRepository.save(newCase);
		entityManager.flush();
		entityManager.refresh(saved);

		CaseDTO dto = new CaseDTO(saved);
		realtimeEventService.publish(EventType.CASE_CREATED, "CASE", dto.getId(), dto);
		return dto;
	}

	@Transactional
	public void deleteCase(Long caseId) {
		Case existingCase = caseRepository.findById(caseId)
				.orElseThrow(() -> new NoSuchElementException("Case not found"));

		// 在刪除前先記下受影響的 Event，刪除後逐一檢查是否還掛在其他 Case
		Set<Event> affectedEvents = new HashSet<>(existingCase.getEvents());

		caseRepository.delete(existingCase);

		for (Event event : affectedEvents) {
			boolean stillHasCase = caseEventRepository.findByEventId(event.getId()).stream()
					.anyMatch(ce -> !ce.getCaseId().equals(caseId));
			eventStatusService.unclassifyFromCase(event, stillHasCase);
		}

		CaseDTO dto = new CaseDTO(existingCase);
		realtimeEventService.publish(EventType.CASE_DELETED, "CASE", dto.getId(), dto);
	}

	/**
	 * 留言時同步指定的狀態轉換。 targetStatus 為 null 或 PROCESSING：沿用原本 ensureProcessingOnComment
	 * 的自動判斷邏輯 （只有 OPEN 會被推進到 PROCESSING）。 targetStatus 為 RESOLVED / CLOSED：走
	 * resolveCase/closeCase，含 cascade 更新底下 Event 狀態。
	 */
	@Transactional
	public void applyCommentStatus(Case target, CaseStatus targetStatus, String actor) {
		if (targetStatus == null || targetStatus == CaseStatus.PROCESSING) {
			ensureProcessingOnComment(target);
			return;
		}
		switch (targetStatus) {
		case RESOLVED:
			resolveCase(target.getId(), actor);
			break;
		case CLOSED:
			closeCase(target.getId(), actor);
			break;
		default:
			throw new IllegalArgumentException("留言無法將 Case 轉換為狀態: " + targetStatus);
		}
	}

	public List<Case> getAllCases() {
		return caseRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
	}

	public Optional<Case> findById(Long id) {
		return caseRepository.findById(id);
	}

//	@Transactional
//	public Case addMessageToCase(Long caseId, Long messageId) {
//		Case res = caseRepository.findById(caseId).orElseThrow(() -> new NoSuchElementException("Case not found"));
//
//		Message message = messageRepository.findById(messageId)
//				.orElseThrow(() -> new NoSuchElementException("Message not found"));
//
//		res.getMessages().add(message);
//		message.getCases().add(res);
//
//		MessageDTO dto = new MessageDTO(message);
//		realtimeEventService.publish(EventType.MESSAGE_CLASSIFIED, "CASE", res.getId(), dto);
//
//		return caseRepository.save(res);
//	}
//
//	@Transactional
//	public Case removeMessageFromCase(Long caseId, Long messageId) {
//		Case res = caseRepository.findById(caseId).orElseThrow(() -> new NoSuchElementException("Group not found"));
//
//		Message message = messageRepository.findById(messageId)
//				.orElseThrow(() -> new NoSuchElementException("Message not found"));
//
//		res.getMessages().remove(message);
//		message.getCases().remove(res);
//
//		MessageDTO dto = new MessageDTO(message);
//		realtimeEventService.publish(EventType.MESSAGE_REMOVED, "CASE", res.getId(), dto);
//		return caseRepository.save(res);
//	}
//
//	@Transactional(readOnly = true)
//	public Set<Message> getMessagesByCaseId(Long caseId) {
//		Case res = caseRepository.findById(caseId).orElseThrow(() -> new NoSuchElementException("Group not found"));
//		return res.getMessages();
//	}

	@Transactional(readOnly = true)
	public Optional<CaseDTO> getCaseDtoById(Long caseId) {
		return caseRepository.findById(caseId).map(caze -> {
			CaseDTO dto = new CaseDTO(caze);
			List<EventDTO> events = eventQueryRepository.findEventsByCaseId(caseId);
			dto.setEvents(new LinkedHashSet<>(events));
			return dto;
		});
	}

//	@Transactional(readOnly = true)
//	public Set<Case> getCasesByMessageId(Long messageId) {
//		Message message = messageRepository.findById(messageId)
//				.orElseThrow(() -> new NoSuchElementException("Message not found"));
//		return message.getCases();
//	}

//	@Transactional
//	public Case addMessagesToCase(Long caseId, List<Long> messageIds) {
//		Case res = caseRepository.findById(caseId).orElseThrow(() -> new NoSuchElementException("Group not found"));
//
//		List<Message> messages = messageRepository.findAllById(messageIds);
//
//		res.getMessages().addAll(messages);
//		for (Message m : messages) {
//			m.getCases().add(res);
//		}
//
//		realtimeEventService.publish(EventType.MESSAGE_CLASSIFIED, "CASE", res.getId(), messages);
//
//		return caseRepository.save(res);
//	}

	@Transactional
	public CaseDTO updateCase(CaseUpdateRequest request) {
		Case existingCase = caseRepository.findById(request.getId())
				.orElseThrow(() -> new NoSuchElementException("Case not found"));

		existingCase.setName(request.getName());
		existingCase.setDescription(request.getDescription());
		existingCase.setRuleEnabled(request.getRuleEnabled());
		existingCase.setLogic(request.getLogic());
		existingCase.setConditions(request.getConditions());
		existingCase.setCreator(request.getCreator());

		Case saved = caseRepository.save(existingCase);

		CaseDTO dto = new CaseDTO(saved);
		realtimeEventService.publish(EventType.CASE_UPDATED, "CASE", dto.getId(), dto);

		return dto;
	}

//	@Transactional
//	public void updateCaseStatus(Long caseId, MessageStatus status) {
//		Case c = caseRepository.findById(caseId)
//				.orElseThrow(() -> new IllegalArgumentException("Case not found: " + caseId));
//		c.setStatus(status);
//
//		messageRepository.updateStatusByCaseId(caseId, status);
//
//		CaseDTO dto = new CaseDTO(c);
//		realtimeEventService.publish(EventType.CASE_UPDATED, "CASE", dto.getId(), dto);
//	}
//
//	@Transactional
//	public void classifyMsgToCase(Case input) {
//		List<Message> msgs = messageService.getAllMessages();
//		Predicate<Message> predicate = classifierService.buildPredicate(input);
//		for (Message msg : msgs) {
//			if (predicate.test(msg)) {
//				input.addMessage(msg);
//			}
//		}
//	}

	/**
	 * Case 被留言時呼叫。只有 OPEN 才會晉升 PROCESSING； 已經是 PROCESSING/RESOLVED/CLOSED
	 * 則不動作、不重複推播。
	 */
	@Transactional
	public void ensureProcessingOnComment(Case target) {
		if (target.getStatus() != CaseStatus.OPEN) {
			return;
		}
		target.setStatus(CaseStatus.PROCESSING);
		Case saved = caseRepository.save(target);
		CaseDTO dto = new CaseDTO(saved);
		realtimeEventService.publish(EventType.CASE_PROCESSING, "CASE", dto.getId(), dto);
	}

	/**
	 * Case 解決：OPEN/PROCESSING -> RESOLVED，並 cascade 底下所有還在 CLASSIFIED 的 Event ->
	 * RESOLVED
	 */
	@Transactional
	public CaseDTO resolveCase(Long caseId, String actor) {
		Case existingCase = caseRepository.findById(caseId)
				.orElseThrow(() -> new NoSuchElementException("Case not found"));

		if (!CaseStatus.RESOLVED.isStrictUpgradeFrom(existingCase.getStatus())) {
			throw new IllegalStateException("Case 目前狀態為 " + existingCase.getStatus() + "，無法再解決: caseId=" + caseId);
		}

		existingCase.setStatus(CaseStatus.RESOLVED);
		existingCase.setProcessingDetailStatus(CommentStatus.RESOLVED);
		existingCase.setResolvedBy(actor);
		existingCase.setResolvedAt(LocalDateTime.now());

		for (Event event : existingCase.getEvents()) {
			eventStatusService.cascadeResolveFromCase(event, actor);
		}

		Case saved = caseRepository.save(existingCase);
		CaseDTO dto = new CaseDTO(saved);
		realtimeEventService.publish(EventType.CASE_RESOLVED, "CASE", dto.getId(), dto);
		return dto;
	}

	/**
	 * Case 結案：OPEN/PROCESSING/RESOLVED -> CLOSED，並 cascade 底下所有 CLASSIFIED/RESOLVED
	 * 的 Event -> CLOSED
	 */
	@Transactional
	public CaseDTO closeCase(Long caseId, String actor) {
		Case existingCase = caseRepository.findById(caseId)
				.orElseThrow(() -> new NoSuchElementException("Case not found"));

		if (!CaseStatus.CLOSED.isStrictUpgradeFrom(existingCase.getStatus())) {
			throw new IllegalStateException("Case 目前狀態為 " + existingCase.getStatus() + "，無法再結案: caseId=" + caseId);
		}

		existingCase.setStatus(CaseStatus.CLOSED);
		existingCase.setProcessingDetailStatus(CommentStatus.CLOSED);
		existingCase.setClosedBy(actor);
		existingCase.setClosedAt(LocalDateTime.now());

		for (Event event : existingCase.getEvents()) {
			eventStatusService.cascadeCloseFromCase(event, actor);
		}

		Case saved = caseRepository.save(existingCase);
		CaseDTO dto = new CaseDTO(saved);
		realtimeEventService.publish(EventType.CASE_CLOSED, "CASE", dto.getId(), dto);
		return dto;
	}
}
