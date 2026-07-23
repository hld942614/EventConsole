package com.project.uhdbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhdbackend.entity.UploadedFile;
import com.project.uhdbackend.repository.UploadedFileRepository;

@Service
public class UploadedFileService {
	@Autowired
	private UploadedFileRepository repository;

	public UploadedFile getFileById(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
	
	public List<UploadedFile> getFilesByAlertCode(String alertCode) {
		return repository.findByAlertCodeOrderByTimestampDesc(alertCode);
	} 
	
	public UploadedFile getFileByAlertCodeAndFileName(String alertCode,String fileName) {
		return repository.getFileByAlertCodeAndFileName(alertCode,fileName).get();
	}

	public void save(UploadedFile file) {
		repository.save(file);
	}
	
	@Transactional
    public void updateFileDescritpion(Long id, String description) {
        UploadedFile file = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("File not found: " + id));
        file.setDescription(description);
    }
}
