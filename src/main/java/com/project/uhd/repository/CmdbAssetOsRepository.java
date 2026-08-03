package com.project.uhd.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhd.entity.CmdbAssetOs;

public interface CmdbAssetOsRepository extends JpaRepository<CmdbAssetOs, Long> {
	List<CmdbAssetOs> findByAssetId(String assetId);

	Optional<CmdbAssetOs> findByAssetIdAndIsCurrent(String assetId, String isCurrent);

	List<CmdbAssetOs> findByAssetIdInAndIsCurrent(Collection<String> assetIds, String isCurrent);
}
