package com.project.uhdbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhdbackend.entity.CmdbApplicationAsset;

public interface CmdbApplicationAssetRepository extends JpaRepository<CmdbApplicationAsset, Long> {
	List<CmdbApplicationAsset> findByAssetId(String assetId);
}
