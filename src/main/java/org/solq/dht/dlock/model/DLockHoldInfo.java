package org.solq.dht.dlock.model;

public class DLockHoldInfo {
	private String toket;

	public static DLockHoldInfo of(String toket) {
		DLockHoldInfo result = new DLockHoldInfo();
		result.toket = toket;
		return result;
	}
	// getter

	public String getToket() {
		return toket;
	}

}
