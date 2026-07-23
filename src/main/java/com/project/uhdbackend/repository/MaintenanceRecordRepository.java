//package com.project.uhdbackend.repository;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.project.uhdbackend.entity.MaintenanceRecord;
//
//public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long>{
//	Optional<MaintenanceRecord> findByServer_Id(Long serverIdPk);
//	void deleteByServerId(Long serverId);
//	List<MaintenanceRecord> findByServerIdIn(Collection<Long> serverIds);
//}
