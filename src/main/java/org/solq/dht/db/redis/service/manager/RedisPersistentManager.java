package org.solq.dht.db.redis.service.manager;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solq.dht.db.redis.service.RedisDao;
import org.springframework.stereotype.Component;

/***
 * 持久化管理 
 * @author solq
 */
@Component
public class RedisPersistentManager {

    private static Logger logger = LoggerFactory.getLogger(RedisPersistentManager.class);
    private static Timer timer;

    private final static int PERIOD = 1000 * 60 * 15;

    static {
	timer = new Timer(true);
	timer.scheduleAtFixedRate(new TimerTask() {

	    @Override
	    public void run() {
		handlePersistent();
	    }
	}, 1000 * 10, PERIOD);
    }

    private static Map<String, RedisDao<?>> daos = new ConcurrentHashMap<>();

    static void handlePersistent() {
	logger.debug("handlePersistent");
	for (RedisDao<?> dao : daos.values()) {
	    try {
		dao.handlePersistent();
	    } catch (Exception e) {
		logger.error("handlePersistent error : {}", e);
	    }
	}
    }

    public static void register(String owner, RedisDao<?> dao) {
	daos.put(owner, dao);
    }

    public static void unRegister(String clz) {
	daos.remove(clz);
    }
    
    @PreDestroy
    void preDestroy(){
	//考虑到连接可能会失败，不做等侍完成
	handlePersistent();
    }
}
