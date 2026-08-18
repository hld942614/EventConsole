package com.project.uhd.enums;

public enum EventStatus {
	UNREAD(0),
	ACKNOWLEDGED(1),
	CLASSIFIED(1),

	PROCESSING(2),
	TRANSFERRED_TO_PIC(2),
	INVESTIGATING(2),
	WAITING_VENDOR(2),
	FIXING(2),
	VERIFYING(2),

	RESOLVED(3),
	CLOSED(4),
	INVALID(-1);

	private final int rank;

	EventStatus(int rank) {
		this.rank = rank;
	}

	public boolean isStrictUpgradeFrom(EventStatus current) {
		if (this.rank < 0 || current.rank < 0) {
			return false;
		}
		return this.rank > current.rank;
	}

	public boolean isProcessingPhase() {
		return this.rank == 2;
	}
}