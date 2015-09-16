package org.solq.dht.db.redis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.query.SortQuery;

public interface IRedisDao<Key, T extends IRedisEntity> {

	// /////////////////////CUD/////////////////////
	public void saveOrUpdate(T entity);

	public void remove(Key key);

	public boolean exists(Key key);

	public void expire(Key key, long timeOut, TimeUnit unit);
	
	
	public Boolean move(Key key ,int dbIndex);	
	
	public Boolean use(int dbIndex);
	
	

	// //////////////////////搜索////////////////////////////////
	public Set<String> keys(String pattern);

	public List<T> query(String pattern);

	public T findOne(Key key);

	public List<T> sort(SortQuery<Key> query);
}
