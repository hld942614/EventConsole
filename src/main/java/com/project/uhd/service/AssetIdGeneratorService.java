package com.project.uhd.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.project.uhd.enums.AssetType;

/**
 * ASSET_ID 產生器，例如 CI-SRV-00000001。
 *
 * 概念與 UHD Console MEVT 序號生成一致：用 SELECT ... FOR UPDATE 鎖住 CMDB_ASSET_SEQ_NO
 * 對應那一列並遞增；若該資產類型第一次使用（列還不存在）， 交由 CmdbAssetSeqNoTxOps 在獨立的 REQUIRES_NEW 交易裡嘗試
 * insert， insert 失敗（代表同時間有另一個 thread 搶先建立）就重試一次， 藉此處理「第一筆序號」情境下的併發 race
 * condition。
 *
 * 這個類別本身不加 @Transactional：實際加鎖/寫入都委派給 CmdbAssetSeqNoTxOps，
 * 確保每次重試都是乾淨的一次獨立交易，而不是被外層匯入的長交易鎖住。
 */
@Service
public class AssetIdGeneratorService {

	private static final int MAX_RETRY = 3;

	private final CmdbAssetSeqNoTxOps seqNoTxOps;

	public AssetIdGeneratorService(CmdbAssetSeqNoTxOps seqNoTxOps) {
		this.seqNoTxOps = seqNoTxOps;
	}

	/** 產生一個新的 ASSET_ID，例如 CI-SRV-00000001 */
	public String generateAssetId(AssetType assetType) {
		String code = assetType.seqCode();
		long nextSeq = nextSequenceWithRetry(code);
		return String.format("%s-%08d", assetType.assetIdPrefix(), nextSeq);
	}

	private long nextSequenceWithRetry(String code) {
		RuntimeException lastError = null;
		for (int attempt = 0; attempt < MAX_RETRY; attempt++) {
			try {
				return seqNoTxOps.lockAndIncrement(code);
			} catch (DataIntegrityViolationException e) {
				// 另一個 thread 同時間也在幫同一個 assetType 建立第一筆序號列，重試即可
				lastError = e;
			}
		}
		throw new IllegalStateException("無法產生 ASSET_ID 序號（assetTypeCode=" + code + "），重試 " + MAX_RETRY + " 次後仍失敗",
				lastError);
	}
}
