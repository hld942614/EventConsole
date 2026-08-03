package com.project.uhd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhd.entity.Case;

public interface CaseRepository extends JpaRepository<Case, Long> {
//	@Query("SELECT g FROM Group g JOIN g.messages m WHERE m = :message")
//	List<Case> findByMessagesContaining(@Param("message") Message message);
}
