package org.solq.dht.db.redis.event;

/**
 * redis事件
 * 
 * @author solq
 */
public interface IRedisEvent {
	
	public String getName();
	
	public String getFrom();
}
