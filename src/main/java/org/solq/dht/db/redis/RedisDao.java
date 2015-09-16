package org.solq.dht.db.redis;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis 基础dao http://shift-alt-ctrl.iteye.com/category/277626 备份问题
 * http://my.oschina.net/freegeek/blog/324410
 * 
 * @author solq
 * */
public class RedisDao<T extends IRedisEntity> implements IRedisDao<String, T> {

	@Autowired
	protected JedisConnectionFactory cf;
	protected Class<T> entityClass;
	protected ValueOperations<String, T> ops;
	protected RedisTemplate<String, T> redis;

	static enum StringSerializer implements RedisSerializer<String> {
		INSTANCE;

		@Override
		public byte[] serialize(String s) {
			return (null != s ? s.getBytes() : new byte[0]);
		}

		@Override
		public String deserialize(byte[] bytes) {
			if (bytes.length > 0) {
				return new String(bytes);
			} else {
				return null;
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void afterPropertiesSet() {
		if (entityClass == null) {
			//执行期获取java 泛型类型,class 必须要有硬文件存在
			entityClass = (Class<T>) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
		}

		redis = new RedisTemplate<String, T>();
		redis.setConnectionFactory(cf);
		redis.setKeySerializer(StringSerializer.INSTANCE);
		redis.setValueSerializer(new Jackson2JsonRedisSerializer<T>(
				(Class<T>) entityClass));
		redis.afterPropertiesSet();
		ops = redis.opsForValue();
	}

	public static <T extends IRedisEntity> RedisDao<T> of(Class<T> entityClass,
			JedisConnectionFactory cf) {
		RedisDao<T> result = new RedisDao<T>();
		result.cf = cf;
		result.entityClass = entityClass;
		result.afterPropertiesSet();
		return result;
	}

	@Override
	public T findOne(String key) {
		return ops.get(key);
	}

	@Override
	public List<T> sort(SortQuery<String> query) {
		List<T> list = redis.sort(query);
		return list;
	}

	@Override
	public void saveOrUpdate(T entity) {
		ops.set(entity.toId(), entity);
	}

	@Override
	public boolean exists(String key) {
		return redis.hasKey(key);
	}

	@Override
	public void expire(String key, long timeout, TimeUnit unit) {
		redis.expire(key, timeout, unit);
	}

	@Override
	public Set<String> keys(String pattern) {
		return redis.keys(pattern);
	}

	@Override
	public List<T> query(String pattern) {
		Set<String> ids = redis.keys(pattern);
		List<T> result = new ArrayList<>(ids.size());
		for (String id : ids) {
			result.add(findOne(id));
		}
		return result;
	}

	@Override
	public void remove(String key) {
		redis.delete(key);
	}

	@Override
	public Boolean move(String key, int dbIndex) {
		return redis.move(key, dbIndex);		
	}

	@Override
	public Boolean use(int dbIndex) {
		
		//TODO
 		return null;
	}

}
