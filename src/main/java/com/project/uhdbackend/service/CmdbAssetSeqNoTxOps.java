package com.project.uhdbackend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhdbackend.entity.CmdbAssetSeqNo;
import com.project.uhdbackend.repository.CmdbAssetSeqNoRepository;

/**
 * 獨立成一個 bean，是為了讓 REQUIRES_NEW 確實透過 Spring AOP proxy 生效。
 * 如果把這個方法跟呼叫端（AssetIdGeneratorService 的重試迴圈）放在同一個類別， 用 this.xxx(...) 呼叫會是
 * self-invocation，@Transactional 不會真的開新交易， 鎖定/重試機制就會失效——所以刻意拆成獨立 bean，讓呼叫一定是跨
 * bean 呼叫。
 */
@Service
public class CmdbAssetSeqNoTxOps {

	private final CmdbAssetSeqNoRepository seqNoRepository;

	public CmdbAssetSeqNoTxOps(CmdbAssetSeqNoRepository seqNoRepository) {
		this.seqNoRepository = seqNoRepository;
	}

	/**
	 * 鎖住（或視需要先建立）指定 assetTypeCode 的序號列並遞增，回傳遞增後的序號。 用獨立交易執行，鎖定時間只涵蓋這個方法本身，執行完立刻
	 * commit 釋放鎖， 不會被外層（例如整批匯入）的長交易拖著一起鎖住。
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public long lockAndIncrement(String assetTypeCode) {
		Optional<CmdbAssetSeqNo> existing = seqNoRepository.lockByAssetTypeCode(assetTypeCode);
		CmdbAssetSeqNo row = existing
				.orElseGet(() -> seqNoRepository.saveAndFlush(new CmdbAssetSeqNo(assetTypeCode, 0L)));
		row.setCurrentSeq(row.getCurrentSeq() + 1);
		seqNoRepository.save(row);
		return row.getCurrentSeq();
	}
}
