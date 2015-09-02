package org.solq.dht.dlock;

import org.solq.dht.dlock.model.DId;

public interface IDistributedLock {

	public IRemoteProxy getRemoteProxy();

	public DId getId(); 
	
	public void lock(DId id);
}
