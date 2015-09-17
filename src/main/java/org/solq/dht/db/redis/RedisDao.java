package org.solq.dht.db.redis;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis 基础dao http://shift-alt-ctrl.iteye.com/category/277626 备份问题
 * http://my.oschina.net/freegeek/blog/324410
 * 
 * @author solq
 */
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
			// 执行期获取java 泛型类型,class 必须要有硬文件存在
			entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		}

		redis = new RedisTemplate<String, T>();
		redis.setConnectionFactory(cf);
		redis.setKeySerializer(StringSerializer.INSTANCE);
		redis.setValueSerializer(new Jackson2JsonRedisSerializer<T>((Class<T>) entityClass));
		redis.afterPropertiesSet();
		ops = redis.opsForValue();
	}

	public static <T extends IRedisEntity> RedisDao<T> of(Class<T> entityClass, JedisConnectionFactory cf) {
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

	// @Override
	// public List<T> sort(SortQuery<String> query) {
	// List<T> list = redis.sort(query);
	// return list;
	// }

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
	public void rename(String oldKey, String newKey) {
		redis.rename(oldKey, newKey);
	}

	@Override
	public DataType type(String key) {
		return redis.type(key);
	}

	// http://jimgreat.iteye.com/blog/1596058
	// http://www.tuicool.com/articles/I32IFz
	@Override
	public T tx(String key, TxlCallBack<T> callBack) {

		String lockKey = "_clock_" + key;
		if (lock(lockKey)) {
			T entity = null;
			entity = findOne(key);
			// 开启事务是不能进行任何操作
			try {
				entity = callBack.exec(entity);
			} finally {
				if (entity != null) {
					saveOrUpdate(entity);
				}
				unLock(lockKey);
			}
			return entity;
		}

		throw new RuntimeException("time out");
		// return redis.execute(scb);
	}



	@SuppressWarnings("unchecked")
	private boolean lock(String lockKey) {
		// 处理次数
		int times = 30;
		// 下次请求等侍时间
		final int sleepTime = 50;
		// 有限时间
		final long expireMsecs = 1000 * 30;
		boolean locked = false;
		final RedisConnection redisConnection = redis.getConnectionFactory().getConnection();
		final RedisSerializer<String> rs = (RedisSerializer<String>) redis.getKeySerializer();
		final byte[] key = rs.serialize(lockKey);
		while (times >= 0) {
			// 锁到期时间
			long expires = System.currentTimeMillis() + expireMsecs + 1;
			byte[] value = rs.serialize(String.valueOf(expires));
			// 首次设置成功
			if (redisConnection.setNX(key, value)) {
				return true;
			}

			byte[] data = redisConnection.get(key);
			if (data == null) {
				times--;
				continue;
			}
			// 超时处理
			String currentValueStr = rs.deserialize(data);
			if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
				data = redisConnection.getSet(key, value);
				if (data == null) {
					times--;
					continue;
				}
				String oldValueStr = rs.deserialize(data);
				// 申请成功
				if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
					locked = true;
					return true;
				}
			}

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			times--;
		}
		return locked;
	}
	private void unLock(String lockKey) {
		redis.delete(lockKey);
	}
}
