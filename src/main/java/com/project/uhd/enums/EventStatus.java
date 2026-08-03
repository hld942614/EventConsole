package com.project.uhd.enums;

public enum EventStatus {
	UNREAD(0), // 新事件(未讀)
	ACKNOWLEDGED(1), // 已確認(已讀) —— 由「開啟事件」觸發
	CLASSIFIED(1), // 歸類 —— 由「分類進 Case」觸發，與 ACKNOWLEDGED 同層級的平行分支
	PROCESSING(2), // 處理中 —— 已讀 且 (有留言 或 已分類)
	RESOLVED(3), // 已解決
	CLOSED(4), // 已結案
	INVALID(-1); // 沿用既有用途（資料驗證失敗），不參與正常流程

	private final int rank;

	EventStatus(int rank) {
		this.rank = rank;
	}

	/** 嚴格前進：target 的 rank 要大於 current 的 rank。用於 resolve/close 這種單純往後走的轉換。 */
	public boolean isStrictUpgradeFrom(EventStatus current) {
		if (this.rank < 0 || current.rank < 0) {
			return false; // INVALID 不參與
		}
		return this.rank > current.rank;
	}
}