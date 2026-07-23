//package com.project.uhdbackend.service;
//
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import com.project.uhdbackend.entity.NetworkInfo;
//import com.project.uhdbackend.repository.NetworkInfoRepository;
//
//@Service
//public class NetworkInfoService {
//	private NetworkInfoRepository repository;
//	public NetworkInfoService(NetworkInfoRepository repository) {
//		this.repository = repository;
//	}
//	
//	public List<NetworkInfo> getAllInfos(){
//		return repository.findAll();
//	}
//	
//	public void save(NetworkInfo networkInfo) {
//		repository.save(networkInfo);
//	}
//	
//	public NetworkInfo getNetworkInfoByServerId(Long serverId) {
//		return repository.findByServer_Id(serverId).orElseThrow(
//				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NetworkInfo not found: " + serverId));
//	}
//
//}
