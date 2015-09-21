package org.solq.dht.db.redis.model;

/**
 * 锁逻辑回调
 * 
 * @author solq
 */
public interface LockCallBack {
	/**
	 * @parkey
	 */
	public void exec(String key);
}
