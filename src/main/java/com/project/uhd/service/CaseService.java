package com.project.uhd.service;

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
import com.project.uhd.dto.StatusLogDTO;
import com.project.uhd.entity.Case;
import com.project.uhd.entity.Event;
import com.project.uhd.enums.CaseStatus;
import com.project.uhd.enums.ChangeSource;
import com.project.uhd.enums.StatusLogTargetType;
import com.project.uhd.realtime.event.EventType;
import com.project.uhd.realtime.service.RealtimeEventService;
import com.project.uhd.repository.CaseRepository;
import com.project.uhd.repository.EventQueryRepository;

@Service
public class CaseService {

	private final CaseRepository caseRepository;
	private final RealtimeEventService realtimeEventService;
	private EntityManager entityManager;
	private final EventQueryRepository eventQueryRepository;
	private final EventStatusService eventStatusService;
	private final StatusLogService statusLogService;

	public CaseService(CaseRepository caseRepository, RealtimeEventService realtimeEventService,
			EntityManager entityManager, EventQueryRepository eventQueryRepository,
			EventStatusService eventStatusService, StatusLogService statusLogService) {
		this.caseRepository = caseRepository;
		this.realtimeEventService = realtimeEventService;
		this.entityManager = entityManager;
		this.eventQueryRepository = eventQueryRepository;
		this.eventStatusService = eventStatusService;
		this.statusLogService = statusLogService;
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

		statusLogService.log(StatusLogTargetType.CASE, saved.getId(), CaseStatus.OPEN.name(),
				currentUser.getChineseName(), currentUser.getId(), ChangeSource.USER, null);

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

	@Transactional
	public void applyCommentStatus(Case target, CaseStatus targetStatus, CustomUserDetails currentUser,
			Long relatedCommentId) {
		if (targetStatus == CaseStatus.RESOLVED) {
			resolveCase(target.getId(), currentUser, ChangeSource.USER, relatedCommentId);
			return;
		}
		if (targetStatus == CaseStatus.CLOSED) {
			closeCase(target.getId(), currentUser, ChangeSource.USER, relatedCommentId);
			return;
		}

		CaseStatus effectiveTarget = (targetStatus == null) ? CaseStatus.PROCESSING : targetStatus;
		if (!effectiveTarget.isProcessingPhase()) {
			throw new IllegalArgumentException("留言無法將 Case 轉換為狀態: " + targetStatus);
		}

		if (target.getStatus() == CaseStatus.OPEN || target.getStatus().isProcessingPhase()) {
			setStatusWithLog(target, effectiveTarget, currentUser, ChangeSource.USER, relatedCommentId);
			Case saved = caseRepository.save(target);
			CaseDTO dto = new CaseDTO(saved);
			realtimeEventService.publish(EventType.CASE_PROCESSING, "CASE", dto.getId(), dto);
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
		return resolveCase(caseId, currentUser, ChangeSource.USER, null);
	}

	@Transactional
	public CaseDTO closeCase(Long caseId, CustomUserDetails currentUser) {
		return closeCase(caseId, currentUser, ChangeSource.USER, null);
	}
	
	@Transactional
	public CaseDTO resolveCase(Long caseId, CustomUserDetails currentUser, ChangeSource source, Long relatedCommentId) {
		Case existingCase = caseRepository.findById(caseId)
				.orElseThrow(() -> new NoSuchElementException("Case not found"));

		if (!CaseStatus.RESOLVED.isStrictUpgradeFrom(existingCase.getStatus())) {
			throw new IllegalStateException("Case 目前狀態為 " + existingCase.getStatus() + "，無法再解決: caseId=" + caseId);
		}

		setStatusWithLog(existingCase, CaseStatus.RESOLVED, currentUser, source, relatedCommentId);

		for (Event event : existingCase.getEvents()) {
			eventStatusService.cascadeResolveFromCase(event, currentUser);
		}

		Case saved = caseRepository.save(existingCase);
		CaseDTO dto = new CaseDTO(saved);
		realtimeEventService.publish(EventType.CASE_RESOLVED, "CASE", dto.getId(), dto);
		return dto;
	}

	@Transactional
	public CaseDTO closeCase(Long caseId, CustomUserDetails currentUser, ChangeSource source, Long relatedCommentId) {
		Case existingCase = caseRepository.findById(caseId)
				.orElseThrow(() -> new NoSuchElementException("Case not found"));

		if (!CaseStatus.CLOSED.isStrictUpgradeFrom(existingCase.getStatus())) {
			throw new IllegalStateException("Case 目前狀態為 " + existingCase.getStatus() + "，無法再結案: caseId=" + caseId);
		}

		setStatusWithLog(existingCase, CaseStatus.CLOSED, currentUser, source, relatedCommentId);

		for (Event event : existingCase.getEvents()) {
			eventStatusService.cascadeCloseFromCase(event, currentUser);
		}

		Case saved = caseRepository.save(existingCase);
		CaseDTO dto = new CaseDTO(saved);
		realtimeEventService.publish(EventType.CASE_CLOSED, "CASE", dto.getId(), dto);
		return dto;
	}

	@Transactional(readOnly = true)
	public List<StatusLogDTO> getStatusHistory(Long caseId, String order) {
		if (!caseRepository.existsById(caseId)) {
			throw new NoSuchElementException("Case not found: " + caseId);
		}
		return statusLogService.getHistory(StatusLogTargetType.CASE, caseId, order);
	}

	private void setStatusWithLog(Case target, CaseStatus newStatus, CustomUserDetails currentUser,
			ChangeSource source, Long relatedCommentId) {
		target.setStatus(newStatus);
		statusLogService.log(StatusLogTargetType.CASE, target.getId(), newStatus.name(),
				currentUser != null ? currentUser.getChineseName() : null,
				currentUser != null ? currentUser.getId() : null, source, relatedCommentId);
	}
}
