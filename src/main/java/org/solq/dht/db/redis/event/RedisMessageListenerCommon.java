package org.solq.dht.db.redis.event;

import org.solq.dht.db.redis.service.ser.Jackson3JsonRedisSerializer;
import org.springframework.data.redis.connection.Message;

public abstract class RedisMessageListenerCommon implements IRedisMessageListener {

	@Override
	public void onMessage(Message message, byte[] pattern) {
		RedisEventCode event = Jackson3JsonRedisSerializer.json2Object(message.getBody(), RedisEventCode.class);		
		onEvent(Jackson3JsonRedisSerializer.json2Object(event.getBody(), event.getType()));
	}
}
