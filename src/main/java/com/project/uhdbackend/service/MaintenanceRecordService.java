//package com.project.uhdbackend.service;
//
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import com.project.uhdbackend.entity.MaintenanceRecord;
//import com.project.uhdbackend.repository.MaintenanceRecordRepository;
//
//@Service
//public class MaintenanceRecordService {
//	
//	private MaintenanceRecordRepository  repository;
//	public MaintenanceRecordService(MaintenanceRecordRepository  repository) {
//		this.repository = repository;
//	}
//	
//	public List<MaintenanceRecord> getAllMaintenanceRecords(){
//		return repository.findAll();
//	}
//	
//	public void save(MaintenanceRecord maintenanceRecord) {
//		repository.save(maintenanceRecord);
//	}
//	
//	public MaintenanceRecord getMaintenanceRecordByServerId(Long serverId) {
//		return repository.findByServer_Id(serverId).orElseThrow(
//				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MaintenanceRecord not found: " + serverId));
//	}
//}
