package com.project.uhdbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhdbackend.entity.CmdbAssetHardware;

public interface CmdbAssetHardwareRepository extends JpaRepository<CmdbAssetHardware, Long> {
	Optional<CmdbAssetHardware> findByAssetId(String assetId);
}
