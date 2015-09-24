package org.solq.dht.db.redis.service.ser;

import org.springframework.data.redis.serializer.RedisSerializer;

public enum LongSerializer implements RedisSerializer<Long> {
	INSTANCE;

	@Override
	public byte[] serialize(Long s) {
		return (null != s ? s.toString().getBytes() : new byte[0]);
	}

	@Override
	public Long deserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return Long.parseLong(new String(bytes));
	}
}