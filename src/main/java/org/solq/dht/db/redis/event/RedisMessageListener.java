package org.solq.dht.db.redis.event;

public class RedisMessageListener extends RedisMessageListenerCommon {
	public final static String NAME = "test:channel";

	@Override
	public String getChannel() {
		return NAME;
	}

	@Override
	public void onEvent(IRedisEvent event) {
		System.out.println(event.getFrom());
	}
}
