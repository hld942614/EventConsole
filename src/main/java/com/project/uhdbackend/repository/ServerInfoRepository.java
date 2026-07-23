//package com.project.uhdbackend.repository;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.EntityGraph;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.project.uhdbackend.entity.ServerInfo;
//
//public interface ServerInfoRepository extends JpaRepository<ServerInfo, Long> {
//	Optional<ServerInfo> findByServerNameAndRackAndModel(String serverName, String rack, String model);
//
//	@EntityGraph(attributePaths = { "hardwareSpec", "networkInfo", "maintenanceRecord", "mtaGroup" })
//	List<ServerInfo> findAll();
//}
