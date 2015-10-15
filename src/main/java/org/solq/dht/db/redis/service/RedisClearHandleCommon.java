package org.solq.dht.db.redis.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solq.dht.db.redis.model.IRedisClearHandle;
import org.solq.dht.db.redis.model.IRedisEntity;

/***
 * 清理数据模板
 * 
 * @author solq
 */
public abstract class RedisClearHandleCommon<T extends IRedisEntity> implements IRedisClearHandle {
    private static Logger logger = LoggerFactory.getLogger(RedisClearHandleCommon.class);

    @Override
    public void exec() {
	RedisDao<T> dao = getDao();
	List<T> datas = dao.query(getQueryAll(dao.getEntityClass()));
	for (T entity : datas) {
	    if (valid(entity)) {
		if (logger.isWarnEnabled()) {
		    logger.warn("clear data : {}", entity.toId());
		}
		dao.remove(entity.toId());
	    }
	}
    }

    public abstract int getValidDay();

    public abstract boolean valid(T entity);

    public abstract RedisDao<T> getDao();

    public abstract String getQueryAll(Class<?> clz);
}
