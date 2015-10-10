package org.solq.dht.db.redis.model;

public interface CursorCallBack<T> {
	public void exec(T entity);
}
