package org.solq.dht.db.redis.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.connection.RedisConnectionFactory;

/***
 * @author solq
 */
public class RedisDataSourceManager {

    private Map<String, RedisConnectionFactory> redisConnectionFactorys = new HashMap<String, RedisConnectionFactory>();

    public RedisConnectionFactory getRedisConnection(String dataSource) {
	return redisConnectionFactorys.get(dataSource);
    }

    public synchronized void registerRedisConnection(String dataSource, RedisConnectionFactory connectionFactory) {
	redisConnectionFactorys.put(dataSource, connectionFactory);
    }

}
