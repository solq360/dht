package org.solq.dht.db.redis.event;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

public class RedisEventManager {

	private RedisMessageListenerContainer redisMessageListenerContainer;

	private Map<String, IRedisMessageListener> listeners = new HashMap<>();

	public void register(IRedisMessageListener... listeners) {
		for (IRedisMessageListener listener : listeners) {
			String channel = listener.getChannel();
			this.listeners.put(channel, listener);
			ChannelTopic channelTopic = new ChannelTopic(channel);
			redisMessageListenerContainer.addMessageListener(listener, channelTopic);
		}
	}

	public void unRegister(String channel) {
		IRedisMessageListener listener = listeners.remove(channel);
		if (listener == null) {
			return;
		}
		redisMessageListenerContainer.removeMessageListener(listener);
	}

	public static RedisEventManager of(RedisMessageListenerContainer redisMessageListenerContainer,
			IRedisMessageListener... listeners) {
		RedisEventManager result = new RedisEventManager();
		result.redisMessageListenerContainer = redisMessageListenerContainer;
		result.register(listeners);
		
		redisMessageListenerContainer.start();
 		return result;
	}
}
