package com.project.uhdbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhdbackend.entity.CmdbAsset;

public interface CmdbAssetRepository extends JpaRepository<CmdbAsset, Long> {
	Optional<CmdbAsset> findByAssetId(String assetId);

	List<CmdbAsset> findAll();

	boolean existsByAssetId(String assetId);

	Optional<CmdbAsset> findFirstByServerNameOrderByIdDesc(String serverName);

	List<CmdbAsset> findByParentAssetId(String parentAssetId);
}
