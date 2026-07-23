//package com.project.uhdbackend.service;
//
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import com.project.uhdbackend.entity.ServerInfo;
//import com.project.uhdbackend.repository.ServerInfoRepository;
//
//@Service
//public class ServerInfoService {
//
//	private ServerInfoRepository repository;
//
//	public ServerInfoService(ServerInfoRepository repository) {
//		this.repository = repository;
//	}
//
//	public List<ServerInfo> getAllInfos() {
//		return repository.findAll();
//	}
//
//	public void save(ServerInfo serverInfo) {
//		repository.save(serverInfo);
//	}
//	
//	public ServerInfo findById(Long serverId) {
//		return repository.findById(serverId).get();
//	}
//
//	public ServerInfo getByServerName(String serverName, String rack, String model) {
//		return repository.findByServerNameAndRackAndModel(serverName, rack, model).orElseThrow(
//				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ServerInfo not found: " + serverName));
//	}
//}
