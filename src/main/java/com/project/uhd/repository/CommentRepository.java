package com.project.uhd.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.uhd.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query("select c from Comment c\r\n" + "			where c.commentId in (\r\n"
			+ "			    select distinct c2.commentId\r\n" + "			    from Comment c2 join c2.events e\r\n"
			+ "			    where e.eventId in :eventIds\r\n" + "			)")
	List<Comment> findAllDistinctByEventIds(@Param("eventIds") Collection<String> eventIds, Sort sort);

	@Query("select c from Comment c\r\n"
			+ "			where c.commentId in (\r\n"
			+ "			    select distinct c2.commentId\r\n"
			+ "			    from Comment c2 join c2.cases cs\r\n"
			+ "			    where cs.id = :caseId\r\n"
			+ "			)")
	List<Comment> findAllByCaseId_ManyToMany(@Param("caseId") Long caseId, Sort sort);
}
