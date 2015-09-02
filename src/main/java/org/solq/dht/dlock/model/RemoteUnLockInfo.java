package org.solq.dht.dlock.model;

public class RemoteUnLockInfo {

	private DId id;
	private String token;

	public static RemoteUnLockInfo of(DId id, String token) {
		RemoteUnLockInfo result = new RemoteUnLockInfo();
		result.id = id;
		result.token = token;
		return result;
	}

	// getter

	public String getToken() {
		return token;
	}

	public DId getId() {
		return id;
	}

}
