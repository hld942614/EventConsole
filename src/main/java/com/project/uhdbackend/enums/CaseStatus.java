package com.project.uhdbackend.enums;

public enum CaseStatus {
	OPEN(0), PROCESSING(1), RESOLVED(2), CLOSED(3);

	private final int rank;

	CaseStatus(int rank) {
		this.rank = rank;
	}

	public boolean isStrictUpgradeFrom(CaseStatus current) {
		return this.rank > current.rank;
	}

	/** 是否為尚未終結、仍可接收新事件/繼續處理的狀態 */
	public boolean isActive() {
		return this == OPEN || this == PROCESSING;
	}
}
