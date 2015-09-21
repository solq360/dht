package org.solq.dht.test.db.redis.model;

import org.solq.dht.db.redis.model.IRedisEntity;

public class Item implements IRedisEntity {

	private String id;

	private int count;

	public static Item of(String id, int count) {
		Item result = new Item();
		result.id = id;
		result.count = count;
		return result;
	}

	@Override
	public String toId() {
		return id;
	}

	// get set
	public String getId() {
		return id;
	}

	public int getCount() {
		return count;
	}

	void setId(String id) {
		this.id = id;
	}

	void setCount(int count) {
		this.count = count;
	}

	public void addValue() {
		this.count++;
	}

}
