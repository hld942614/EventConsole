package com.project.uhd.repository;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.project.uhd.entity.CmdbAssetSeqNo;

public interface CmdbAssetSeqNoRepository extends JpaRepository<CmdbAssetSeqNo, String> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select s from CmdbAssetSeqNo s where s.assetTypeCode = :code")
	Optional<CmdbAssetSeqNo> lockByAssetTypeCode(String code);
}
