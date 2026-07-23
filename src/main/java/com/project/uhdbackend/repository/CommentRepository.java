package com.project.uhdbackend.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhdbackend.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Modifying
	@Transactional
	@Query(value = "UPDATE MUHD_COMMENT SET comment_content = :content WHERE comment_id = :id", nativeQuery = true)
	void updateCommentText(@Param("content") String content, @Param("id") Long id);

//	@Query("select c from Comment c\r\n" + "			where c.commentId in (\r\n"
//			+ "			    select distinct c2.commentId\r\n" + "			    from Comment c2 join c2.messages m\r\n"
//			+ "			    where m.messageId in :messageIds\r\n" + "			)")
//	List<Comment> findAllDistinctByMessageIds(@Param("messageIds") Collection<Long> messageIds, Sort sort);

	// 加在既有 findAllDistinctByMessageIds 旁邊

	@Query("select c from Comment c\r\n" + "			where c.commentId in (\r\n"
			+ "			    select distinct c2.commentId\r\n" + "			    from Comment c2 join c2.events e\r\n"
			+ "			    where e.eventId in :eventIds\r\n" + "			)")
	List<Comment> findAllDistinctByEventIds(@Param("eventIds") Collection<String> eventIds, Sort sort);

	@Query("SELECT DISTINCT c\r\n" + "           FROM Comment c\r\n" + "           JOIN c.cases cs\r\n"
			+ "           WHERE cs.id = :caseId")
	List<Comment> findAllByCaseId_ManyToMany(@Param("caseId") Long caseId, Sort sort);
}
