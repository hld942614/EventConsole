package com.project.uhd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ASSET_ID 流水號輔助表。搭配 AssetIdGeneratorService 的 SELECT ... FOR UPDATE +
 * PROPAGATION_REQUIRES_NEW 重試機制使用， 概念與 UHD Console MEVT 序號生成一致。
 */
@Entity
@Table(name = "CMDB_ASSET_SEQ_NO")
public class CmdbAssetSeqNo {

	@Id
	@Column(name = "ASSET_TYPE_CODE", length = 20)
	private String assetTypeCode;

	@Column(name = "CURRENT_SEQ", nullable = false)
	private Long currentSeq = 0L;

	public CmdbAssetSeqNo() {
	}

	public CmdbAssetSeqNo(String assetTypeCode, Long currentSeq) {
		this.assetTypeCode = assetTypeCode;
		this.currentSeq = currentSeq;
	}

	public String getAssetTypeCode() {
		return assetTypeCode;
	}

	public void setAssetTypeCode(String assetTypeCode) {
		this.assetTypeCode = assetTypeCode;
	}

	public Long getCurrentSeq() {
		return currentSeq;
	}

	public void setCurrentSeq(Long currentSeq) {
		this.currentSeq = currentSeq;
	}
}
