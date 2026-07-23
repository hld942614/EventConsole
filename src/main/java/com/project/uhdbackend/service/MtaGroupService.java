//package com.project.uhdbackend.service;
//
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import com.project.uhdbackend.entity.MtaGroup;
//import com.project.uhdbackend.repository.MtaGroupRepository;
//
//@Service
//public class MtaGroupService {
//	private MtaGroupRepository repository;
//	public MtaGroupService(MtaGroupRepository repository) {
//		this.repository = repository;
//	}
//	
//	public List<MtaGroup> getAllMtaGroups(){
//		return repository.findAll();
//	}
//	
//	public void save(MtaGroup mtaGroup) {
//		repository.save(mtaGroup);
//	}
//	
//	public MtaGroup getMtaGroupByServerId(Long serverId) {
//		return repository.findByServer_Id(serverId).orElseThrow(
//				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MtaGroup not found: " + serverId));
//	}
//}
