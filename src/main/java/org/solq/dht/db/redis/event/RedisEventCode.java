package org.solq.dht.db.redis.event;

/**
 * redis事件编码
 * 
 * @author solq
 */
public class RedisEventCode {

	private Class<? extends IRedisEvent> type;

	public String body;

	public static RedisEventCode of(Class<? extends IRedisEvent> type, String body) {
		RedisEventCode result = new RedisEventCode();
		result.type = type;
		result.body = body;
		return result;
	}
	// getter

	public Class<? extends IRedisEvent> getType() {
		return type;
	}

	public String getBody() {
		return body;
	}

	void setType(Class<? extends IRedisEvent> type) {
		this.type = type;
	}

	void setBody(String body) {
		this.body = body;
	}

}
