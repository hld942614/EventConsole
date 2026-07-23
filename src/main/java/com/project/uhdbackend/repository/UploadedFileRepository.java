package com.project.uhdbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhdbackend.entity.UploadedFile;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
	List<UploadedFile> findByAlertCodeOrderByTimestampDesc(String alertCode);

	Optional<UploadedFile> getFileByAlertCodeAndFileName(String alertCode, String fileName);
}
