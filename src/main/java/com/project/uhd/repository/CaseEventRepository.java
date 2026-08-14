//package com.project.uhd.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.project.uhd.entity.CaseEvent;
//import com.project.uhd.entity.CaseEventId;
//
//public interface CaseEventRepository extends JpaRepository<CaseEvent, CaseEventId> {
//	List<CaseEvent> findByCaseId(Long caseId);
//
//	List<CaseEvent> findByEventId(Long eventId);
//}