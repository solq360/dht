package org.solq.dht.dlock.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.solq.dht.dlock.IRemoteProxy;
import org.solq.dht.dlock.model.DId;
import org.solq.dht.dlock.model.DLockHoldInfo;
import org.solq.dht.dlock.model.DLockResult;
import org.solq.dht.dlock.model.RemoteLockInfo;
import org.solq.dht.dlock.model.RemoteUnLockInfo;

@SuppressWarnings("unchecked")
public class TestRemoteProxy implements IRemoteProxy {

	private final static AtomicLong al = new AtomicLong();
	private ConcurrentHashMap<DId, DLockHoldInfo> locks = new ConcurrentHashMap<>();

	public DLockResult<DLockHoldInfo> lock(RemoteLockInfo info) {
		DId id = info.getId();
		DLockHoldInfo hold = locks.get(id);
		if (hold != null) {
			return DLockResult.ERROR(DLockResult.APPLY_LOCK_USED);
		}
		String toket = String.valueOf(al.getAndIncrement());
		hold= DLockHoldInfo.of(toket);
		locks.put(id, hold);
		return DLockResult.SUCCEED(hold);
	}

	public DLockResult<DLockHoldInfo> unlock(RemoteUnLockInfo info) {
		DId id = info.getId();
		DLockHoldInfo hold = locks.get(id);
		if (hold == null) {
			return DLockResult.ERROR(DLockResult.NOT_FIND_LOCK);
		}
		if(!info.getToken().equals(hold.getToket() ) ){
			return DLockResult.ERROR(DLockResult.LOCK_SEQUENCE_DISAFFINITY);
		}
		locks.remove(id);
		return DLockResult.SUCCEED(hold);
	}

}
