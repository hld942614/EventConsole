package com.project.uhd.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.project.uhd.exception.InvalidEventPayloadException;

/**
 * 產生格式如 MEVT-{moduleCode}-{environment}-{yyyyMMdd}-{6碼流水號} 的 eventId。
 *
 * 流水號規則：同一天 + 同模組 + 同環境，從 1 開始重新編號。 因為組合是動態的，無法用單一 Oracle SEQUENCE，改用計數器表 +
 * SELECT ... FOR UPDATE 行鎖。
 *
 * 正常情況（該 SEQ_KEY 當天已經有其他事件產生過）： FOR UPDATE 會鎖住既有那一列，其他 transaction 會被 block
 * 到前一筆 commit 為止，不會撞號， 且此鎖是 DB 層級，多實例/多節點部署下依然安全。
 *
 * 邊界情況（該 SEQ_KEY 當天「第一次」出現）： FOR UPDATE 鎖不到「還不存在」的列，兩個 transaction 可能同時判斷
 * current == null， 都嘗試 INSERT 同一個 SEQ_KEY（PRIMARY KEY），其中一個會拋出 unique
 * constraint 違反例外。 這裡用「REQUIRES_NEW + retry」處理：第一次失敗代表對方已經 commit 了該列， 重跑一次就會改走
 * UPDATE 分支，正常取號，不會產生重複 ID，只是多一次重試。
 */
@Service
public class EventIdGeneratorService {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE; // yyyyMMdd
	private static final int MAX_RETRY = 3;

	private final TransactionTemplate requiresNewTransactionTemplate;

	@PersistenceContext
	private EntityManager em;

	public EventIdGeneratorService(PlatformTransactionManager transactionManager) {
		this.requiresNewTransactionTemplate = new TransactionTemplate(transactionManager);
		this.requiresNewTransactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	}

	public String generate(String moduleCode, String environment, LocalDate date) {
		if (environment == null || environment.isBlank()) {
			throw new InvalidEventPayloadException("environment 為必要欄位，不可為 null");
		}
		if (moduleCode == null || moduleCode.isBlank()) {
			throw new InvalidEventPayloadException("moduleCode 判斷失敗，無法產生 eventId");
		}

		String datePart = date.format(DATE_FORMAT);
		String seqKey = moduleCode + "-" + environment + "-" + datePart;

		long next = nextValueWithRetry(seqKey);

		return String.format("MEVT-%s-%s-%s-%06d", moduleCode, environment, datePart, next);
	}

	private long nextValueWithRetry(String seqKey) {
		DataIntegrityViolationException lastError = null;
		for (int attempt = 1; attempt <= MAX_RETRY; attempt++) {
			try {
				return requiresNewTransactionTemplate.execute(status -> nextValue(seqKey));
			} catch (DataIntegrityViolationException e) {
				// 代表另一個 transaction 剛好搶先 insert 了同一個 seqKey（當天第一筆的邊界情況）。
				// 對方已經 commit，重試一次會改走 UPDATE 分支，正常取號。
				lastError = e;
			}
		}
		throw new IllegalStateException("eventId 取號重試 " + MAX_RETRY + " 次仍失敗: seqKey=" + seqKey, lastError);
	}

	private long nextValue(String seqKey) {
		Long current;
		try {
			current = ((Number) em
					.createNativeQuery("SELECT CURRENT_VALUE FROM MUHD_EVENT_SEQUENCE WHERE SEQ_KEY = :key FOR UPDATE")
					.setParameter("key", seqKey).getSingleResult()).longValue();
		} catch (NoResultException e) {
			current = null;
		}

		long next;
		if (current == null) {
			next = 1;
			em.createNativeQuery("INSERT INTO MUHD_EVENT_SEQUENCE (SEQ_KEY, CURRENT_VALUE) VALUES (:key, :val)")
					.setParameter("key", seqKey).setParameter("val", next).executeUpdate();
		} else {
			next = current + 1;
			em.createNativeQuery("UPDATE MUHD_EVENT_SEQUENCE SET CURRENT_VALUE = :val WHERE SEQ_KEY = :key")
					.setParameter("val", next).setParameter("key", seqKey).executeUpdate();
		}
		return next;
	}
}