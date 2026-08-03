package com.project.uhd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhd.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

	List<Attachment> findByEventId(String eventId);

}
