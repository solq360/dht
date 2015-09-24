package org.solq.dht.db.redis.event;

/**
 * redis事件通用
 * 
 * @author solq
 */
public abstract class RedisEventCommon implements IRedisEvent {
	public RedisEventCommon(){}
	public RedisEventCommon(String name, String from) {
		this.name = name;
		this.from = from;
	}

	private String name;
	private String from;

	public String getName() {
		return name;
	}

	public String getFrom() {
		return from;
	}

	void setName(String name) {
		this.name = name;
	}

	void setFrom(String from) {
		this.from = from;
	}
}
