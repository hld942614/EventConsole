package com.project.uhd.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhd.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

	boolean existsByEventId(String eventId);

	Optional<Event> findByEventId(String eventId);

	List<Event> findAllByEventIdIn(Collection<String> eventIds);

	List<Event> findAll();

	@Modifying
	@Transactional
	@Query("""
			    update Event e
			       set e.hasAttachment='Y'
			     where e.eventId=:eventId
			""")
	void updateHasAttachment(@Param("eventId") String eventId);
}