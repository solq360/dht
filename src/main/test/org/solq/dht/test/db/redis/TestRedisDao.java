package org.solq.dht.test.db.redis;

import org.solq.dht.db.redis.anno.StoreStrategy;
import org.solq.dht.db.redis.service.RedisDao;
import org.solq.dht.db.redis.service.RedisDataSourceManager;
import org.solq.dht.test.db.redis.model.Item;

@StoreStrategy(dataSource = "test")
public class TestRedisDao extends RedisDao<Item> {

    public static TestRedisDao of(RedisDataSourceManager redisDataSourceManager) {
	TestRedisDao result = new TestRedisDao();
	result.setRedisDataSourceManager(redisDataSourceManager);
 	return result;
    }

}
