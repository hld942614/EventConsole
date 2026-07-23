//package com.project.uhdbackend.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import com.project.uhdbackend.entity.HardwareSpec;
//import com.project.uhdbackend.repository.HardwareSpecRepository;
//
//@Service
//public class HardwareSpecService {
//	@Autowired
//	private HardwareSpecRepository repository;
//	
//	public List<HardwareSpec> getAllHardwareSpec(){
//		return repository.findAll();
//	}
//	
//	public void save(HardwareSpec hardwareSpec) {
//		repository.save(hardwareSpec);
//	}
//	
//	public HardwareSpec getHardwareSpecByServerId(Long serverId) {
//		return repository.findByServer_Id(serverId).orElseThrow(
//				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "HardwareSpec not found: " + serverId));
//	}
//}
