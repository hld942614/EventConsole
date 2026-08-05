package com.project.uhd.service;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhd.authentication.CustomUserDetails;
import com.project.uhd.dto.CaseCreateRequest;
import com.project.uhd.dto.CaseDTO;
import com.project.uhd.dto.CaseUpdateRequest;
import com.project.uhd.dto.EventDTO;
import com.project.uhd.entity.Case;
import com.project.uhd.entity.Event;
import com.project.uhd.enums.CaseStatus;
import com.project.uhd.realtime.event.EventType;
import com.project.uhd.realtime.service.RealtimeEventService;
import com.project.uhd.repository.CaseRepository;
import com.project.uhd.repository.EventQueryRepository;
import com.project.uhd.util.CommentStatus;

@Service
public class CaseService {

	private final CaseRepository caseRepository;
	private final RealtimeEventService realtimeEventService;
	private EntityManager entityManager;
	private final EventQueryRepository eventQueryRepository;
	private final EventStatusService eventStatusService;

	public CaseService(CaseRepository caseRepository, RealtimeEventService realtimeEventService,
			EntityManager entityManager, EventQueryRepository eventQueryRepository,
			EventStatusService eventStatusService) {
		this.caseRepository = caseRepository;
		this.realtimeEventService = realtimeEventService;
		this.entityManager = entityManager;
		this.eventQueryRepository = eventQueryRepository;
		this.eventStatusService = eventStatusService;
	}

	@Transactional
	public CaseDTO addCase(CaseCreateRequest request, CustomUserDetails currentUser) {
		Case newCase = new Case();
		newCase.setName(request.getName());
		newCase.setDescription(request.getDescription());
		newCase.setLogic(request.getLogic());
		newCase.setConditions(request.getConditions());
		newCase.setCreator(currentUser.getChineseName());
		newCase.setCreatorId(currentUser.getId());
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

		if (!existingCase.getEvents().isEmpty()) {
			throw new IllegalStateException("Case 底下仍有關聯的 Event，請先移除所有 Event 後再刪除: caseId=" + caseId);
		}

		CaseDTO dto = new CaseDTO(existingCase);

		caseRepository.delete(existingCase); // comments 會透過 cascade 自動刪除

		realtimeEventService.publish(EventType.CASE_DELETED, "CASE", dto.getId(), dto);
	}

	/**
	 * 留言時同步指定的狀態轉換。 targetStatus 為 null 或 PROCESSING：沿用原本 ensureProcessingOnComment
	 * 的自動判斷邏輯 （只有 OPEN 會被推進到 PROCESSING）。 targetStatus 為 RESOLVED / CLOSED：走
	 * resolveCase/closeCase，含 cascade 更新底下 Event 狀態。
	 */
	@Transactional
	public void applyCommentStatus(Case target, CaseStatus targetStatus, CustomUserDetails currentUser) {
		if (targetStatus == null || targetStatus == CaseStatus.PROCESSING) {
			ensureProcessingOnComment(target);
			return;
		}
		switch (targetStatus) {
		case RESOLVED:
			resolveCase(target.getId(), currentUser);
			break;
		case CLOSED:
			closeCase(target.getId(), currentUser);
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

	@Transactional(readOnly = true)
	public Optional<CaseDTO> getCaseDtoById(Long caseId) {
		return caseRepository.findById(caseId).map(caze -> {
			CaseDTO dto = new CaseDTO(caze);
			List<EventDTO> events = eventQueryRepository.findEventsByCaseId(caseId);
			dto.setEvents(new LinkedHashSet<>(events));
			return dto;
		});
	}

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

	@Transactional
	public CaseDTO resolveCase(Long caseId, CustomUserDetails currentUser) {
		Case existingCase = caseRepository.findById(caseId)
				.orElseThrow(() -> new NoSuchElementException("Case not found"));

		if (!CaseStatus.RESOLVED.isStrictUpgradeFrom(existingCase.getStatus())) {
			throw new IllegalStateException("Case 目前狀態為 " + existingCase.getStatus() + "，無法再解決: caseId=" + caseId);
		}

		existingCase.setStatus(CaseStatus.RESOLVED);
		existingCase.setProcessingDetailStatus(CommentStatus.RESOLVED);
		existingCase.setResolvedBy(currentUser.getChineseName());
		existingCase.setResolvedById(currentUser.getId());
		existingCase.setResolvedAt(LocalDateTime.now());

		for (Event event : existingCase.getEvents()) {
			eventStatusService.cascadeResolveFromCase(event, currentUser);
		}

		Case saved = caseRepository.save(existingCase);
		CaseDTO dto = new CaseDTO(saved);
		realtimeEventService.publish(EventType.CASE_RESOLVED, "CASE", dto.getId(), dto);
		return dto;
	}

	@Transactional
	public CaseDTO closeCase(Long caseId, CustomUserDetails currentUser) {
		Case existingCase = caseRepository.findById(caseId)
				.orElseThrow(() -> new NoSuchElementException("Case not found"));

		if (!CaseStatus.CLOSED.isStrictUpgradeFrom(existingCase.getStatus())) {
			throw new IllegalStateException("Case 目前狀態為 " + existingCase.getStatus() + "，無法再結案: caseId=" + caseId);
		}

		existingCase.setStatus(CaseStatus.CLOSED);
		existingCase.setProcessingDetailStatus(CommentStatus.CLOSED);
		existingCase.setClosedBy(currentUser.getChineseName());
		existingCase.setClosedById(currentUser.getId());
		existingCase.setClosedAt(LocalDateTime.now());

		for (Event event : existingCase.getEvents()) {
			eventStatusService.cascadeCloseFromCase(event, currentUser);
		}

		Case saved = caseRepository.save(existingCase);
		CaseDTO dto = new CaseDTO(saved);
		realtimeEventService.publish(EventType.CASE_CLOSED, "CASE", dto.getId(), dto);
		return dto;
	}
}
