package com.project.uhd.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhd.entity.StatusLog;
import com.project.uhd.enums.StatusLogTargetType;

public interface StatusLogRepository extends JpaRepository<StatusLog, Long> {
	List<StatusLog> findByEntityTypeAndEntityId(StatusLogTargetType entityType, Long entityId, Sort sort);
}