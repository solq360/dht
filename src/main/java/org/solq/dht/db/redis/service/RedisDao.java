package org.solq.dht.db.redis.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solq.dht.db.redis.anno.CacheStrategy;
import org.solq.dht.db.redis.anno.LockStrategy;
import org.solq.dht.db.redis.anno.StoreStrategy;
import org.solq.dht.db.redis.event.IRedisEvent;
import org.solq.dht.db.redis.event.RedisEventCode;
import org.solq.dht.db.redis.model.CursorCallBack;
import org.solq.dht.db.redis.model.IRedisEntity;
import org.solq.dht.db.redis.model.LockCallBack;
import org.solq.dht.db.redis.model.TxCallBack;
import org.solq.dht.db.redis.service.ser.Jackson3JsonRedisSerializer;
import org.solq.dht.db.redis.service.ser.StringSerializer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.google.common.cache.CacheBuilder;

import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.SafeEncoder;

/**
 * 基础 http://shift-alt-ctrl.iteye.com/category/277626 <br>
 * 备份问题 http://my.oschina.net/freegeek/blog/324410
 * 
 * @author solq
 */
@SuppressWarnings("unchecked")
public class RedisDao<T extends IRedisEntity> implements IRedisDao<String, T>, IRedisMBean, InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(RedisDao.class);

    @Autowired
    protected RedisDataSourceManager redisDataSourceManager;
    protected RedisConnectionFactory cf;

    protected RedisTemplate<String, ?> redis;

    protected Jackson3JsonRedisSerializer<T> valueRedisSerializer;
    protected Class<T> entityClass;

    protected LockStrategy lockStrategy;

    protected CacheStrategy cacheStrategy;

    protected StoreStrategy storeStrategy;

    private ConcurrentMap<String, T> cache = new ConcurrentHashMap<>();
    private Map<String, T> retryElements = new ConcurrentHashMap<>();
    private Set<String> removeElements = new HashSet<>();

    private String owner;

    public void afterPropertiesSet() {
	if (entityClass == null) {
	    // 执行期获取java 泛型类型,class 必须要有硬文件存在
	    entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	lockStrategy = entityClass.getAnnotation(LockStrategy.class);
	cacheStrategy = entityClass.getAnnotation(CacheStrategy.class);
	storeStrategy = getClass().getAnnotation(StoreStrategy.class);

	// dataSource 指定来源
	if (cf == null) {
	    if (redisDataSourceManager == null) {
		throw new RuntimeException("redis redisDataSourceManager is null");
	    }
	    if (storeStrategy == null) {
		throw new RuntimeException("redis storeStrategy is null");
	    }
	    cf = redisDataSourceManager.getRedisConnection(storeStrategy.dataSource());
	}
	if (cf == null) {
	    throw new RuntimeException("RedisConnectionFactory is null");
	}

	// 默认锁策略设置
	if (lockStrategy == null) {
	    lockStrategy = new LockStrategy() {

		@Override
		public Class<? extends Annotation> annotationType() {
		    return LockStrategy.class;
		}

		@Override
		public int times() {
		    return 25;
		}

		@Override
		public long sleepTime() {
		    return 250;
		}

		@Override
		public long expires() {
		    return 1000 * 10;
		}
	    };
	}

	// 缓存设置
	if (cacheStrategy != null) {
	    int length = cacheStrategy.lenth();
	    long expires = cacheStrategy.expires();

	    CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
	    builder.initialCapacity(16);
	    builder.concurrencyLevel(16);
	    // LRU
	    if (length > 0) {
		builder.maximumSize(length);
	    }

	    // TIME OUT
	    if (expires > 0) {
		builder.expireAfterAccess(expires, TimeUnit.MILLISECONDS);
		builder.expireAfterWrite(expires, TimeUnit.MILLISECONDS);
	    }

	    cache = (ConcurrentMap<String, T>) builder.build();
	}

	valueRedisSerializer = new Jackson3JsonRedisSerializer<T>((Class<T>) entityClass);

	// 设置redis tpl
	if (redis == null) {
	    redis = new RedisTemplate<String, Object>();
	    redis.setEnableDefaultSerializer(false);
	    redis.setConnectionFactory(cf);
	    redis.setKeySerializer(StringSerializer.INSTANCE);
	    // redis.setValueSerializer(valueRedisSerializer);
	    redis.afterPropertiesSet();
	}
	// 注册监听
	owner = UUID.randomUUID().toString();
	TimerConnectError.register(owner, this);
    }

    public static <T extends IRedisEntity> RedisDao<T> of(Class<T> entityClass, RedisConnectionFactory cf) {
	RedisDao<T> result = new RedisDao<T>();
	result.entityClass = entityClass;
	result.cf = cf;
	result.afterPropertiesSet();
	return result;
    }

    public static <T extends IRedisEntity> RedisDao<T> of(Class<T> entityClass, RedisConnectionFactory cf, RedisTemplate<String, ?> redis) {
	RedisDao<T> result = new RedisDao<T>();
	result.entityClass = entityClass;
	result.cf = cf;
	result.redis = redis;
	result.afterPropertiesSet();
	return result;
    }

    @Override
    public T findOneForCache(String key) {
	if (lockStrategy == null) {
	    return findOne(key);
	}

	T result = (T) cache.get(key);
	if (result == null) {
	    result = findOne(key);
	    if (result != null) {
		cache.put(key, result);
	    }
	}
	return result;
    }

    @Override
    public T findOne(String key) {
	T result = null;
	try {
	    result = redis.execute(new RedisCallback<T>() {
		@Override
		public T doInRedis(RedisConnection connection) throws DataAccessException {
		    byte[] rawKey = keySerializer(key);
		    byte[] result = connection.get(rawKey);
		    return valueDeserialize(result);
		}
	    }, true);
	} catch (JedisConnectionException e) {
	    logger.error("{}", e);
	}
	return result;
    }

    @Override
    public void saveOrUpdate(T... entitys) {
	for (T entity : entitys) {
	    if (cacheStrategy != null) {
		cache.put(entity.toId(), entity);
	    }

	    // TODO 改为异步保存
	    boolean ok = false;
	    try {
		redis.execute(new RedisCallback<T>() {
		    @Override
		    public T doInRedis(RedisConnection connection) throws DataAccessException {
			byte[] rawKey = keySerializer(entity.toId());
			connection.set(rawKey, valueSerializer(entity));
			return null;
		    }
		}, true);
		ok = true;
	    } catch (JedisConnectionException e) {
		ok = false;
		logger.error("{}", e);
	    } finally {
		// 连接失败处理
		if (!ok) {
		    synchronized (this) {
			retryElements.put(entity.toId(), entity);
		    }
		}
	    }
	}

    }

    void handleConnectError() {
	if (redis.getConnectionFactory().getConnection().isClosed()) {
	    logger.debug("redis isClosed");
	    return;
	}
	Set<String> removeKeys = null;
	Map<String, T> saveEntitys = null;
	synchronized (this) {
	    saveEntitys = new HashMap<>(retryElements);
	    removeKeys = new HashSet<>(removeElements);
	    retryElements.clear();
	}
	remove(removeKeys.toArray(new String[0]));

	for (Entry<String, T> entry : saveEntitys.entrySet()) {
	    saveOrUpdate(entry.getValue());
	}

    }

    @Override
    public Set<String> keys(String pattern) {
	try {
	    return redis.keys(pattern);
	} catch (JedisConnectionException e) {
	    return Collections.emptySet();
	}
    }

    @Override
    public List<T> query(String pattern) {
	try {
	    Set<String> ids = redis.keys(pattern);
	    List<T> result = new ArrayList<>(ids.size());
	    for (String id : ids) {
		result.add(findOne(id));
	    }
	    return result;
	} catch (JedisConnectionException e) {
	    return Collections.EMPTY_LIST;
	}
    }

    @Override
    public void cursor(String pattern, CursorCallBack<T> cb) {
	try {
	    Set<String> ids = redis.keys(pattern);
	    for (String id : ids) {
		cb.exec(findOne(id));
	    }
	} catch (JedisConnectionException e) {

	}
    }

    @Override
    public void remove(String... keys) {
	for (String key : keys) {
	    try {
		redis.delete(key);
		synchronized (this) {
		    cache.remove(key);
		}
	    } catch (JedisConnectionException e) {
		synchronized (this) {
		    removeElements.add(key);
		}
	    }
	}
    }

    @Override
    public <R> R lock(String key, LockCallBack<R> callBack) {
	String lockKey = "_clock_" + key;
	if (lock(lockKey)) {
	    try {
		return callBack.exec(key);
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

    @Override
    public void send(IRedisEvent msg, String... channels) {
	String json = object2Json(msg);
	byte[] bytes = valueSerializer(RedisEventCode.of(msg.getClass(), json));
	for (String channel : channels) {
	    this.redis.convertAndSend(channel, bytes);
	}
    }

    @Override
    public void destroy() {
	TimerConnectError.unRegister(owner);
	handleConnectError();
	if (cf instanceof DisposableBean) {
	    try {
		((DisposableBean) cf).destroy();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    @Override
    public Boolean move(String key, int dbIndex) {
	return redis.move(key, dbIndex);
    }

    @Override
    public boolean rename(String oldKey, String newKey) {
	try {
	    redis.rename(oldKey, newKey);
	    return true;
	} catch (JedisConnectionException e) {
	    return false;
	}
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

    ////////////////////// 内部方法//////////////////////////

    private boolean lock(String lockKey) {
	// 处理次数
	int times = lockStrategy.times();
	// 下次请求等侍时间
	long sleepTime = lockStrategy.sleepTime();
	// 有效时间
	long expires = lockStrategy.expires();
	// 最大请求等侍时间
	final long maxWait = 2 * 1000;

	expires = expires / 1000;
	final RedisConnection redisConnection = redis.getConnectionFactory().getConnection();
	final byte[] key = keySerializer(lockKey);
	int i = 0;
	while (times-- >= 0) {

	    // 首次设置成功
	    // 使用系统自带管理
	    Object ok = redisConnection.execute(Protocol.Command.SET.name(), key, new byte[] { 0 }, SafeEncoder.encode("EX"), Protocol.toByteArray(expires), SafeEncoder.encode("NX"));
	    if (ok != null) {
		return true;
	    }
	    // if (redisConnection.setNX(key, new byte[] { 0 })) {
	    // redisConnection.expire(key, expires);
	    // return true;
	    // }
	    long t = Math.min(maxWait, sleepTime * i);
	    _await(t);
	    i++;
	}

	return false;
    }

    private void unLock(String lockKey) {
	redis.delete(lockKey);
    }

    private void _await(long sleepTime) {
	try {
	    Thread.sleep(sleepTime);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    private byte[] keySerializer(String key) {
	return StringSerializer.INSTANCE.serialize(key);
    }

    private byte[] valueSerializer(Object entity) {
	return this.valueRedisSerializer.serialize(entity);
    }

    private T valueDeserialize(byte[] bytes) {
	return this.valueRedisSerializer.deserialize(bytes);
    }

    private String object2Json(Object entity) {
	return Jackson3JsonRedisSerializer.object2Json(entity);
    }

    // get set

    public void setRedisDataSourceManager(RedisDataSourceManager redisDataSourceManager) {
	this.redisDataSourceManager = redisDataSourceManager;
    }
}
