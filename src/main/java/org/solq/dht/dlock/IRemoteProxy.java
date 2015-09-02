package org.solq.dht.dlock;

import org.solq.dht.dlock.model.DLockHoldInfo;
import org.solq.dht.dlock.model.DLockResult;
import org.solq.dht.dlock.model.RemoteLockInfo;
import org.solq.dht.dlock.model.RemoteUnLockInfo;
 
public interface IRemoteProxy {

	public DLockResult<DLockHoldInfo> lock(RemoteLockInfo info);
 	
 	public DLockResult<DLockHoldInfo> unlock(RemoteUnLockInfo info) ;
}
