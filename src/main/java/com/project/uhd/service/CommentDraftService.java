package com.project.uhd.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhd.authentication.CustomUserDetails;
import com.project.uhd.entity.CommentDraft;
import com.project.uhd.repository.CommentDraftRepository;

@Service
public class CommentDraftService {

	private final CommentDraftRepository draftRepository;

	public CommentDraftService(CommentDraftRepository draftRepository) {
		this.draftRepository = draftRepository;
	}

	@Transactional
	public void saveEventDraft(Long eventPk, String content, CustomUserDetails currentUser) {
		if (content == null || content.isBlank()) {
			// 內容清空視為捨棄草稿，直接刪除，避免留下空白垃圾列
			draftRepository.deleteByOwnerIdAndEventPk(currentUser.getId(), eventPk);
			return;
		}
		CommentDraft draft = draftRepository.findByOwnerIdAndEventPk(currentUser.getId(), eventPk)
				.orElseGet(() -> {
					CommentDraft d = new CommentDraft();
					d.setOwnerId(currentUser.getId());
					d.setEventPk(eventPk);
					return d;
				});
		draft.setDraftContent(content);
		draftRepository.save(draft);
	}

	@Transactional
	public void saveCaseDraft(Long caseId, String content, CustomUserDetails currentUser) {
		if (content == null || content.isBlank()) {
			draftRepository.deleteByOwnerIdAndCaseId(currentUser.getId(), caseId);
			return;
		}
		CommentDraft draft = draftRepository.findByOwnerIdAndCaseId(currentUser.getId(), caseId)
				.orElseGet(() -> {
					CommentDraft d = new CommentDraft();
					d.setOwnerId(currentUser.getId());
					d.setCaseId(caseId);
					return d;
				});
		draft.setDraftContent(content);
		draftRepository.save(draft);
	}

	@Transactional(readOnly = true)
	public Optional<CommentDraft> getEventDraft(Long eventPk, CustomUserDetails currentUser) {
		return draftRepository.findByOwnerIdAndEventPk(currentUser.getId(), eventPk);
	}

	@Transactional(readOnly = true)
	public Optional<CommentDraft> getCaseDraft(Long caseId, CustomUserDetails currentUser) {
		return draftRepository.findByOwnerIdAndCaseId(currentUser.getId(), caseId);
	}

	@Transactional
	public void deleteEventDraft(Long eventPk, CustomUserDetails currentUser) {
		draftRepository.deleteByOwnerIdAndEventPk(currentUser.getId(), eventPk);
	}

	@Transactional
	public void deleteCaseDraft(Long caseId, CustomUserDetails currentUser) {
		draftRepository.deleteByOwnerIdAndCaseId(currentUser.getId(), caseId);
	}
}
