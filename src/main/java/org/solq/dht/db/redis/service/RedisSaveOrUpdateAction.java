package org.solq.dht.db.redis.service;

import org.solq.dht.db.redis.model.IRedisElementAction;
import org.solq.dht.db.redis.model.IRedisEntity;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RedisSaveOrUpdateAction<T extends IRedisEntity> implements IRedisElementAction {

    private RedisDao<T> dao;

    private T entity;

    private boolean retry;

    public static <T extends IRedisEntity> RedisSaveOrUpdateAction<T> of(RedisDao<T> dao, T entity, boolean retry) {
	RedisSaveOrUpdateAction<T> result = new RedisSaveOrUpdateAction<T>();
	result.dao = dao;
	result.entity = entity;
	result.retry = retry;
	return result;
    }

    @Override
    public void exec() {
	dao.saveOrUpdateSync(entity);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((entity == null) ? 0 : entity.hashCode());
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
	RedisSaveOrUpdateAction other = (RedisSaveOrUpdateAction) obj;
	if (entity == null) {
	    if (other.entity != null)
		return false;
	} else if (!entity.toId().equals(other.entity.toId()))
	    return false;
	return true;
    }

    @Override
    public String getId() {
	return entity.toId();
    }

    public RedisDao<T> getDao() {
	return dao;
    }

    public T getEntity() {
	return entity;
    }

    public boolean isRetry() {
	return retry;
    }

}
