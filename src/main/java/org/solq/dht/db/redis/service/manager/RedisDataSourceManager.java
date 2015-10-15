package org.solq.dht.db.redis.service.manager;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.connection.RedisConnectionFactory;

/***
 * 数据来源管理
 * XML 注入
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
