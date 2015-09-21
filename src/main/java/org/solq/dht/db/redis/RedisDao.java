package org.solq.dht.db.redis;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.solq.dht.db.redis.anno.LockStrategy;
import org.solq.dht.db.redis.model.IRedisEntity;
import org.solq.dht.db.redis.model.LockCallBack;
import org.solq.dht.db.redis.model.TxCallBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis 基础dao http://shift-alt-ctrl.iteye.com/category/277626 <br>
 * 备份问题 http://my.oschina.net/freegeek/blog/324410
 * 
 * @author solq
 */
public class RedisDao<T extends IRedisEntity> implements IRedisDao<String, T>, IRedisMBean {

	@Autowired
	protected JedisConnectionFactory cf;
	@Autowired
	protected RedisTemplate<String, ?> redis;
	protected RedisSerializer<T> valueRedisSerializer;
	protected Class<T> entityClass;

	protected LockStrategy lockStrategy;

	static enum StringSerializer implements RedisSerializer<String> {
		INSTANCE;

		@Override
		public byte[] serialize(String s) {
			return (null != s ? s.getBytes() : new byte[0]);
		}

		@Override
		public String deserialize(byte[] bytes) {
			if (bytes == null || bytes.length == 0) {
				return null;
			}
			return new String(bytes);
		}
	}

	static enum LongSerializer implements RedisSerializer<Long> {
		INSTANCE;

		@Override
		public byte[] serialize(Long s) {
			return (null != s ? s.toString().getBytes() : new byte[0]);
		}

		@Override
		public Long deserialize(byte[] bytes) {
			if (bytes == null || bytes.length == 0) {
				return null;
			}
			return Long.parseLong(new String(bytes));
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void afterPropertiesSet() {
		if (entityClass == null) {
			// 执行期获取java 泛型类型,class 必须要有硬文件存在
			entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		}
		lockStrategy = entityClass.getAnnotation(LockStrategy.class);
		valueRedisSerializer = new Jackson2JsonRedisSerializer<T>((Class<T>) entityClass);

		if (redis == null) {
			redis = new RedisTemplate<String, Object>();
			redis.setConnectionFactory(cf);
			redis.setKeySerializer(StringSerializer.INSTANCE);
			redis.setValueSerializer(valueRedisSerializer);
			redis.afterPropertiesSet();
		}
	}

	public static <T extends IRedisEntity> RedisDao<T> of(Class<T> entityClass, JedisConnectionFactory cf) {
		RedisDao<T> result = new RedisDao<T>();
		result.entityClass = entityClass;
		result.cf = cf;
		result.afterPropertiesSet();
		return result;
	}

	public static <T extends IRedisEntity> RedisDao<T> of(Class<T> entityClass, JedisConnectionFactory cf,
			RedisTemplate<String, ?> redis) {
		RedisDao<T> result = new RedisDao<T>();
		result.entityClass = entityClass;
		result.cf = cf;
		result.redis = redis;
		result.afterPropertiesSet();
		return result;
	}

	@Override
	public T findOne(String key) {
		return redis.execute(new RedisCallback<T>() {
			@Override
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] rawKey = keySerializer(key);
				byte[] result = connection.get(rawKey);
				return valueDeserialize(result);
			}
		}, true);
	}

	@Override
	public void saveOrUpdate(T entity) {
		redis.execute(new RedisCallback<T>() {
			@Override
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] rawKey = keySerializer(entity.toId());
				connection.set(rawKey, valueSerializer(entity));
				return null;
			}
		}, true);
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
	public void remove(String... keys) {
		for (String key : keys) {
			redis.delete(key);
		}
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

	@Override
	public long getDbUseSize() {
		return redis.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.dbSize();
			}
		});
	}

	@Override
	public void lock(String key, LockCallBack callBack) {
		String lockKey = "_clock_" + key;
		if (lock(lockKey)) {
			try {
				callBack.exec(key);
			} finally {
				unLock(lockKey);
			}
		}
		throw new RuntimeException("time out");
	}

	// http://jimgreat.iteye.com/blog/1596058
	// http://www.tuicool.com/articles/I32IFz
	@Override
	public T tx(String key, TxCallBack<T> callBack) {

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
	}

	private boolean lock(String lockKey) {
		// 处理次数
		int times = 25;
		// 下次请求等侍时间
		long sleepTime = 250;
		// 最大请求等侍时间
		final long maxWait = 2 * 1000;
		// 有效时间
		long expires = 1000 * 10;

		if (lockStrategy != null) {
			times = lockStrategy.times();
			sleepTime = lockStrategy.sleepTime();
			expires = lockStrategy.expires();
		}
		expires = expires / 1000;
		final RedisConnection redisConnection = redis.getConnectionFactory().getConnection();
		final byte[] key = keySerializer(lockKey);

		int i = 0;
		while (times-- >= 0) {
			// 首次设置成功
			if (redisConnection.setNX(key, new byte[] { 0 })) {
				// 使用系统自带管理
				redisConnection.expire(key, expires);
				return true;
			}
			long t = Math.min(maxWait, sleepTime * i);
			_await(t);
			i++;
		}

		return false;
	}

	private void _await(long sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void unLock(String lockKey) {
		redis.delete(lockKey);
	}

	@Override
	public void destroy() {
		cf.destroy();
	}

	////////////////////// 内部方法//////////////////////////

	private byte[] keySerializer(String key) {
		return StringSerializer.INSTANCE.serialize(key);
	}

	private byte[] valueSerializer(T entity) {
		return this.valueRedisSerializer.serialize(entity);
	}

	private T valueDeserialize(byte[] bytes) {
		return this.valueRedisSerializer.deserialize(bytes);
	}
}
