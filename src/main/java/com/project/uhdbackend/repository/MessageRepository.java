//package com.project.uhdbackend.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import com.project.uhdbackend.entity.Message;
//import com.project.uhdbackend.enums.MessageStatus;
//
//public interface MessageRepository extends JpaRepository<Message, Long> {
//
////	@Query("SELECT m FROM Message m JOIN m.groups g " + "WHERE g.groupId = :groupId "
////			+ "AND LOWER(m.subject) LIKE LOWER(CONCAT('%', :subject, '%'))")
////	List<Message> findMessagesByGroupIdAndSubjectContaining(@Param("groupId") Long groupId,
////			@Param("subject") String subject);
//
//	@Query("SELECT m FROM Message m " + "WHERE LOWER(m.subject) LIKE LOWER(CONCAT('%', :subject, '%'))")
//	List<Message> findMessagesBySubjectContaining(@Param("subject") String subject);
//
//	@Modifying
//	@Query("UPDATE Message m " + "SET m.status = :status " + "WHERE m.id IN ("
//			+ "  SELECT cm.messageId FROM CaseMessage cm WHERE cm.caseId = :caseId" + ")")
//	int updateStatusByCaseId(@Param("caseId") Long caseId, @Param("status") MessageStatus status);
//}
