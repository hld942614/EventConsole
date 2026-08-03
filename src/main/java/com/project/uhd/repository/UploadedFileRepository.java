package com.project.uhd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhd.entity.UploadedFile;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
	List<UploadedFile> findByAlertCodeOrderByTimestampDesc(String alertCode);

	Optional<UploadedFile> getFileByAlertCodeAndFileName(String alertCode, String fileName);
}
