package org.solq.dht.test.db.redis;

import org.junit.Test;
import org.solq.dht.db.redis.service.JedisConnectionFactory;
import org.solq.dht.db.redis.service.manager.RedisDataSourceManager;
import org.solq.dht.test.db.redis.model.Item;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import redis.clients.jedis.JedisPoolConfig;

public class TestRedisPersistent {
    
    public RedisConnectionFactory connect() {
	JedisPoolConfig poolConfig = new JedisPoolConfig();
	JedisConnectionFactory cf = new JedisConnectionFactory();
	// cf.setPort(6379);
	cf.setHostName("192.168.50.159");
	cf.setPort(7001);

	cf.setUsePool(true);
	cf.setTimeout(1000 * 60 * 5);
	cf.setConnectionTimeout(1000 * 60 * 2);
	cf.setPoolConfig(poolConfig);
	cf.afterPropertiesSet();
	return cf;
    }
    @Test
    public void test() throws InterruptedException {
	RedisConnectionFactory cf = connect();
	RedisDataSourceManager redisDataSourceManager=new RedisDataSourceManager();
	redisDataSourceManager.registerRedisConnection("test", cf);
	TestRedisDao redis = TestRedisDao.of(redisDataSourceManager);
	redis.afterPropertiesSet();
	redis.saveOrUpdate(Item.of("a", 5));
	Thread.sleep(1000*60*60);
    }

}
