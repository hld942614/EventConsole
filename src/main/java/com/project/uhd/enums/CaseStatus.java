package com.project.uhd.enums;

public enum CaseStatus {
	OPEN(0),

	PROCESSING(1),
	TRANSFERRED_TO_PIC(1),
	INVESTIGATING(1),
	WAITING_VENDOR(1),
	FIXING(1),
	VERIFYING(1),

	RESOLVED(2),
	CLOSED(3);

	private final int rank;

	CaseStatus(int rank) {
		this.rank = rank;
	}

	public boolean isStrictUpgradeFrom(CaseStatus current) {
		return this.rank > current.rank;
	}

	public boolean isProcessingPhase() {
		return this.rank == 1;
	}

	public boolean isActive() {
		return this == OPEN || isProcessingPhase();
	}
}
