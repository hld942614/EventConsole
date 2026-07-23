//package com.project.uhdbackend.repository;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.project.uhdbackend.entity.MtaGroup;
//import com.project.uhdbackend.entity.NetworkInfo;
//
//public interface NetworkInfoRepository extends JpaRepository<NetworkInfo, Long>{
//	Optional<NetworkInfo> findByServer_Id(Long serverIdPk);
//	void deleteByServerId(Long serverId);
//	List<MtaGroup> findByServerIdIn(Collection<Long> serverIds);
//}
