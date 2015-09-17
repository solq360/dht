package org.solq.dht.db.redis;

public interface TxlCallBack<T> {
	public T exec(T entity);
}
