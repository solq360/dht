package org.solq.dht.db.redis.model;

import org.solq.dht.db.redis.IRedisEntity;

public class RedisLock implements IRedisEntity {

	private String id;

	public String getId() {
		return id;
	}

	@Override
	public String toId() {
		return id;
	}

}
