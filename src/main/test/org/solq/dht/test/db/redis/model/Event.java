package org.solq.dht.test.db.redis.model;

import org.solq.dht.db.redis.event.RedisEventCommon;

public class Event extends RedisEventCommon {
	public Event() {

	}

	public Event(String name, String from) {
		super(name, from);
	}
}
