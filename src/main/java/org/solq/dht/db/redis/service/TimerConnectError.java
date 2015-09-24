package org.solq.dht.db.redis.service;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TimerConnectError {

	private static Logger logger = LoggerFactory.getLogger(TimerConnectError.class);
	private static Timer timer;

	private final static int PERIOD = 1000 * 60 * 15;

	static {
		timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				handleConnectError();
			}
		}, 1000 * 10, PERIOD);
	}

	private static Map<String, RedisDao<?>> daos = new ConcurrentHashMap<>();

	protected static void handleConnectError() {
		logger.debug("handleConnectError");
 		for (RedisDao<?> dao : daos.values()) {
			dao.handleConnectError();
		}
	}

	public static void register(String owner, RedisDao<?> dao) {
		daos.put(owner, dao);
	}

	public static void unRegister(String clz) {
		daos.remove(clz);
	}
}
