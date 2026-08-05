package com.project.uhd.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhd.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findAllByEvent_EventIdIn(Collection<String> eventIds, Sort sort);

	List<Comment> findAllByCaze_Id(Long caseId, Sort sort);
}
