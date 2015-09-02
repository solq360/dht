package org.solq.dht.dlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.solq.dht.dlock.model.DId;
import org.solq.dht.dlock.model.DLockHoldInfo;
import org.solq.dht.dlock.model.DLockResult;
import org.solq.dht.dlock.model.RemoteLockInfo;
import org.solq.dht.dlock.model.RemoteUnLockInfo;

/**
 * 分布式锁
 * 
 * @author solq
 */
public class DistributedLock implements IDistributedLock, Lock, java.io.Serializable {

	private static final long serialVersionUID = 5749540652661711272L;
	/** 远程锁代理 **/
	private IRemoteProxy remoteProxy;
	/** 申请记录 **/
	private ThreadLocal<DLockHoldInfo> applyInfo = new ThreadLocal<>();
	/** 申请ID **/
	private ThreadLocal<DId> idInfo = new ThreadLocal<>();

	////////////////////////

	private int retry = 5;

	private int timeOut = 5;

	private TimeUnit timeUnit = TimeUnit.SECONDS;

	@Override
	public void lock(DId id) {
		idInfo.set(id);
		tryLock();
	}

	@Override
	public void lock() {
		tryLock();
	}

	@Override
	public boolean tryLock() {
		try {
			return tryLock(timeOut, timeUnit);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void unlock() {
		try {
			DLockResult<DLockHoldInfo> response = remoteProxy.unlock(RemoteUnLockInfo.of(getDid(), null));
			if (!response.verifly()) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			applyInfo.remove();
			idInfo.remove();
		}
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {

		if (idInfo.get() == null) {
			throw new RuntimeException("id is null");
		}

		long startTime = System.nanoTime();
		int r = this.retry;
		while (r > 0) {
			try {
				DLockResult<DLockHoldInfo> response = remoteProxy.lock(RemoteLockInfo.of(getDid()));
				DLockHoldInfo info = response.getResult();
				applyInfo.set(info);
				if (response.verifly()) {
					return true;
				}
				Thread.sleep(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (time > 0) {
				long endTime = System.nanoTime();
				if (endTime - startTime >= unit.toMillis(time)) {
					throw new RuntimeException("time out");
				}
			}
			r--;
		}
		throw new RuntimeException("retry fail");
 	}

	private DId getDid() {
		return idInfo.get();
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {

	}

	@Override
	public Condition newCondition() {
		return null;
	}

	// getter
	@Override
	public IRemoteProxy getRemoteProxy() {
		return this.remoteProxy;
	}

	@Override
	public DId getId() {
		return this.idInfo.get();
	}
}
