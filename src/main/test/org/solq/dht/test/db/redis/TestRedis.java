package org.solq.dht.test.db.redis;

import java.util.List;

import org.junit.Test;
import org.solq.dht.db.redis.RedisDao;
import org.solq.dht.db.redis.TxlCallBack;
import org.solq.dht.test.db.redis.model.Item;
import org.solq.dht.test.db.redis.model.User;
import org.solq.dht.test.db.redis.model.User2;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestRedis {
	private ObjectMapper objectMapper = new ObjectMapper();

	public JedisConnectionFactory connect() {
		JedisConnectionFactory cf = new JedisConnectionFactory();
		cf.setHostName("120.25.105.27");
		cf.setPort(6379);
		cf.setUsePool(true);		
		cf.setTimeout(1000 * 60);
		cf.afterPropertiesSet();
		return cf;
	}

	@Test
	public void calJsonSerLenth() throws JsonProcessingException {
		User user = User.of("aaaa", 5, 100);
		byte[] bytes = objectMapper.writeValueAsBytes(user);
		System.out.println(objectMapper.writeValueAsString(user));
		System.out.println(bytes.length);
		System.out.println(bytes.length / 1024);
	}

	@Test
	public void calUpByte() throws JsonProcessingException {
		User user = User.of("aaaa", 5, 100);
		byte[] bytes = objectMapper.writeValueAsBytes(user);

		int upSize = 200;
		int time = 120;
		System.out.println("总上传 M: " + bytes.length / 1024 * upSize / 1024);
		System.out.println("每秒上传 k: " + bytes.length / 1024 * upSize / time);
	}

	@Test
	public void testCreate() {
		JedisConnectionFactory cf = connect();
		RedisDao<User> redis = RedisDao.of(User.class, cf);

		long begin = System.currentTimeMillis();
		int count = 100000;
		long time = 0;

		for (int i = 0; i < count; i++) {
			String key = "json/user" + i;
			redis.saveOrUpdate(User.of(key, i, 100));

			if (i % 200 == 0) {
				time = System.currentTimeMillis() - begin;
				System.out.println(Thread.currentThread() + " : " + i + " time :" + time);
			}
		}
		time = System.currentTimeMillis() - begin;
		System.out.println("create time:" + time);
	}

	@Test
	public void testTx() throws InterruptedException {
		int count = 20;
		Thread[] t = new Thread[count];
		for (int i = 0; i < count; i++) {
			t[i] = new Thread(new TaskTx());
		}
		for (int i = 0; i < count; i++) {
			t[i].start();
		}
		for (int i = 0; i < count; i++) {
			t[i].join();
		}
	}

//	@Test
//	public void testSelect1() throws InterruptedException {
//		int count = 1;
//		Thread[] t = new Thread[count];
//		for (int i = 0; i < count; i++) {
//			t[i] = new Thread(new Task1());
//		}
//		for (int i = 0; i < count; i++) {
//			t[i].start();
//		}
//		for (int i = 0; i < count; i++) {
//			t[i].join();
//		}
//	}

	@Test
	public void testSelect() throws InterruptedException {
		int count = 500;
		Thread[] t = new Thread[count];
		for (int i = 0; i < count; i++) {
			t[i] = new Thread(new Task());
		}
		for (int i = 0; i < count; i++) {
			t[i].start();
		}
		for (int i = 0; i < count; i++) {
			t[i].join();
		}
	}

	class TaskTx implements Runnable {

		@Override
		public void run() {
			JedisConnectionFactory cf = connect();
			RedisDao<Item> redis = RedisDao.of(Item.class, cf);

			String key = "itme.test1";
			String key2 = "itme.test2";
			redis.saveOrUpdate(Item.of(key, 5));
			redis.saveOrUpdate(Item.of(key2, 10));
			redis.tx(key, new TxlCallBack<Item>() {
				@Override
				public Item exec(Item entity) {
					if (entity == null) {
						return null;
					}
					entity.addValue();
					return entity;
				}
			});
			Item entity = redis.findOne(key);
			System.out.println(entity.getCount());
		}

	}

	class Task1 implements Runnable {

		@Override
		public void run() {
			JedisConnectionFactory cf = connect();
			RedisDao<User2> redis = RedisDao.of(User2.class, cf);

			String key = "user2.test1";
			redis.saveOrUpdate(User2.of(key, 5));
			User2 user = redis.findOne(key);
			if (user != null) {
				System.out.println(user.getUid());
			}

			long begin = 0;
			long time = 0;
			begin = System.currentTimeMillis();
			SortQuery<String> query = SortQueryBuilder.sort("user2.test").get("user2.test*").build();

			List<User2> list = redis.query("user2.test*");

			System.out.println(list.size());
			time = System.currentTimeMillis() - begin;
			System.out.println("select time:" + time);
		}

	}

	class Task implements Runnable {

		@Override
		public void run() {
			JedisConnectionFactory cf = connect();

			RedisDao<User> redis = RedisDao.of(User.class, cf);
			long begin = 0;
			long time = 0;
			int count = 100000;
			begin = System.currentTimeMillis();
			for (int i = 0; i < count; i++) {
				String key = "json/user" + i;
				User user = redis.findOne(key);
				if (user != null) {
					int a = user.getAge() + 1;
				}
				if (i % 200 == 0) {
					time = System.currentTimeMillis() - begin;
					System.out.println(Thread.currentThread() + " : " + i + " time :" + time);
				}
			}
			time = System.currentTimeMillis() - begin;
			System.out.println("select time:" + time);
		}

	}

}
