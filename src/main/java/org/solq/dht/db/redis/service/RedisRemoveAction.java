package org.solq.dht.db.redis.service;

import org.solq.dht.db.redis.model.IRedisElementAction;
import org.solq.dht.db.redis.model.IRedisEntity;

@SuppressWarnings({ "rawtypes" })
public class RedisRemoveAction<T extends IRedisEntity> implements IRedisElementAction {

    private RedisDao<T> dao;

    private String key;

    private boolean retry;

    public static <T extends IRedisEntity> RedisRemoveAction<T> of(RedisDao<T> dao, String key, boolean retry) {
	RedisRemoveAction<T> result = new RedisRemoveAction<T>();
	result.dao = dao;
	result.key = key;
	result.retry = retry;
	return result;
    }


    @Override
    public void exec() {
	dao.removeSync(key);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((key == null) ? 0 : key.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	RedisRemoveAction other = (RedisRemoveAction) obj;
	if (key == null) {
	    if (other.key != null)
		return false;
	} else if (!key.equals(other.key))
	    return false;
	return true;
    }

    @Override
    public String getId() {
	return key;
    }

    @Override
    public boolean isRetry() {
	return retry;
    }

}
