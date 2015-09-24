package org.solq.dht.db.redis.event;

import org.springframework.data.redis.connection.MessageListener;

public interface IRedisMessageListener extends MessageListener {

	public String getChannel();

	public void onEvent(IRedisEvent event);
}
