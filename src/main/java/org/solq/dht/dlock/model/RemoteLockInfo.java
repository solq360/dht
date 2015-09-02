package org.solq.dht.dlock.model;

public class RemoteLockInfo {

	private DId id;

	public static RemoteLockInfo of(DId id) {
		RemoteLockInfo result = new RemoteLockInfo();
		result.id = id;
		return result;
	}
	// getter

	public DId getId() {
		return id;
	}

}
