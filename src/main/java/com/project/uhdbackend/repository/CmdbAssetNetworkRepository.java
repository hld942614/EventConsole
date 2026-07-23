package com.project.uhdbackend.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhdbackend.entity.CmdbAssetNetwork;

public interface CmdbAssetNetworkRepository extends JpaRepository<CmdbAssetNetwork, Long> {
	List<CmdbAssetNetwork> findByAssetId(String assetId);

	List<CmdbAssetNetwork> findByAssetIdIn(Collection<String> assetIds);
}
