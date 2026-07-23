package com.project.uhdbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhdbackend.entity.ConditionEntity;


public interface ConditionRepository extends JpaRepository<ConditionEntity, Long> {
    List<ConditionEntity> findByGroupId(String groupId);
    void deleteByGroupId(String groupId);
}