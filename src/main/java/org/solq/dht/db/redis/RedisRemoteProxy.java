package org.solq.dht.db.redis;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.solq.dht.db.redis.model.RedisLock;
import org.solq.dht.dlock.IRemoteProxy;
import org.solq.dht.dlock.model.DId;
import org.solq.dht.dlock.model.DLockHoldInfo;
import org.solq.dht.dlock.model.DLockResult;
import org.solq.dht.dlock.model.RemoteLockInfo;
import org.solq.dht.dlock.model.RemoteUnLockInfo;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@SuppressWarnings("unchecked")
public class RedisRemoteProxy implements IRemoteProxy {

	private final static AtomicLong al = new AtomicLong();
	private ConcurrentHashMap<DId, DLockHoldInfo> locks = new ConcurrentHashMap<>();

	private RedisDao<RedisLock> redisDao;

	public void afterPropertiesSet() {
		JedisConnectionFactory cf = new JedisConnectionFactory();
		cf.setHostName("120.25.105.27");
		cf.setPort(6379);
 		redisDao = RedisDao.of(RedisLock.class, cf);
 		
	}

	public DLockResult<DLockHoldInfo> lock(RemoteLockInfo info) {
		DId id = info.getId();
		DLockHoldInfo hold = locks.get(id);
		if (hold != null) {
			return DLockResult.ERROR(DLockResult.APPLY_LOCK_USED);
		}
		String toket = String.valueOf(al.getAndIncrement());
		

		
		
		hold = DLockHoldInfo.of(toket);
		locks.put(id, hold);
		return DLockResult.SUCCEED(hold);
	}

	public DLockResult<DLockHoldInfo> unlock(RemoteUnLockInfo info) {
		DId id = info.getId();
		DLockHoldInfo hold = locks.get(id);
		if (hold == null) {
			return DLockResult.ERROR(DLockResult.NOT_FIND_LOCK);
		}
		if (!info.getToken().equals(hold.getToket())) {
			return DLockResult.ERROR(DLockResult.LOCK_SEQUENCE_DISAFFINITY);
		}
		locks.remove(id);
		return DLockResult.SUCCEED(hold);
	}

}