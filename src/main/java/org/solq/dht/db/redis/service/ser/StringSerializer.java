package org.solq.dht.db.redis.service.ser;

import org.springframework.data.redis.serializer.RedisSerializer;

public enum StringSerializer implements RedisSerializer<String> {
	INSTANCE;

	@Override
	public byte[] serialize(String s) {
		return (null != s ? s.getBytes() : new byte[0]);
	}

	@Override
	public String deserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return new String(bytes);
	}
}
