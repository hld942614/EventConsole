//package com.project.uhdbackend.repository;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.project.uhdbackend.entity.HardwareSpec;
//
//public interface HardwareSpecRepository extends JpaRepository<HardwareSpec, Long>{
//	Optional<HardwareSpec> findByServer_Id(Long serverIdPk);
//	void deleteByServerId(Long serverId);
//	List<HardwareSpec> findByServerIdIn(Collection<Long> serverIds);
//}
