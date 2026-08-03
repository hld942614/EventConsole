package com.project.uhd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhd.entity.CmdbAssetHardware;

public interface CmdbAssetHardwareRepository extends JpaRepository<CmdbAssetHardware, Long> {
	Optional<CmdbAssetHardware> findByAssetId(String assetId);
}
