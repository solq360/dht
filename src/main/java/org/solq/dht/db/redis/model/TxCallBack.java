package org.solq.dht.db.redis.model;

/**
 * 事务逻辑回调
 * 
 * @author solq
 */
public interface TxCallBack<T> {
	public T exec(T entity);
}
