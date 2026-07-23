//package com.project.uhdbackend.repository;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.project.uhdbackend.entity.MtaGroup;
//
//public interface MtaGroupRepository extends JpaRepository<MtaGroup, Long>{
//	Optional<MtaGroup> findByServer_Id(Long serverIdPk);
//	void deleteByServerId(Long serverId);
//	List<MtaGroup> findByServerIdIn(Collection<Long> serverIds);
//}
