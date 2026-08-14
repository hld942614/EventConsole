package com.project.uhd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhd.entity.CommentDraft;

public interface CommentDraftRepository extends JpaRepository<CommentDraft, Long> {

	Optional<CommentDraft> findByOwnerIdAndCaseId(String ownerId, Long caseId);

	Optional<CommentDraft> findByOwnerIdAndEventPk(String ownerId, Long eventPk);

	void deleteByOwnerIdAndCaseId(String ownerId, Long caseId);

	void deleteByOwnerIdAndEventPk(String ownerId, Long eventPk);
}
