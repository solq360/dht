package org.solq.dht.test.db.shards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.shards.ShardId;
import org.hibernate.shards.ShardedConfiguration;
import org.hibernate.shards.cfg.ConfigurationToShardConfigurationAdapter;
import org.hibernate.shards.cfg.ShardConfiguration;
import org.hibernate.shards.loadbalance.RoundRobinShardLoadBalancer;
import org.hibernate.shards.strategy.ShardStrategy;
import org.hibernate.shards.strategy.ShardStrategyFactory;
import org.hibernate.shards.strategy.ShardStrategyImpl;
import org.hibernate.shards.strategy.access.SequentialShardAccessStrategy;
import org.hibernate.shards.strategy.access.ShardAccessStrategy;
import org.hibernate.shards.strategy.resolution.AllShardsShardResolutionStrategy;
import org.hibernate.shards.strategy.resolution.ShardResolutionStrategy;
import org.hibernate.shards.strategy.selection.RoundRobinShardSelectionStrategy;
import org.hibernate.shards.strategy.selection.ShardSelectionStrategy;
import org.junit.Test;
import org.solq.dht.db.HibernateTemplate;
import org.solq.dht.test.db.shards.model.ContactEntity;

//http://www.cnblogs.com/RicCC/archive/2010/04/14/hibernate-shards-3-architecture.html
//http://blog.csdn.net/bluishglc/article/details/7970268

//NamingStrategy 解决方案
//http://www.360doc.com/content/10/0901/17/1542811_50437228.shtml

//guzz 解决方案 
//https://github.com/liukaixuan/guzz
//http://www.iteye.com/topic/787836




//mybatis 解决方案 
//http://blog.csdn.net/zhulin40/article/details/38705105

public class TestShards {

	@Test
	public void test1() {
		String loginId = "RicCC@cnblogs.com";
		String password = "123";

		SessionFactory factory = null;
		Session session = null;
		Transaction transaction = null;
		List contacts = null;
		Iterator it = null;
		try {
			factory = new Configuration().configure("db1.xml").buildSessionFactory();
			session = factory.openSession();
			transaction = session.beginTransaction();

			System.out.println("===Create Contacts===");
			ContactEntity c = new ContactEntity("01111111", "RicCC@cnblogs.com", "123", "Richie", "RicCC@cnblogs.com");
			session.save(c);
			c = new ContactEntity("91111111", "a@cnblogs.com", "123", "AAA", "a@cnblogs.com");
			session.save(c);
			c = new ContactEntity("81111111", "b@cnblogs.com", "123", "BBB", "b@cnblogs.com");
			session.save(c);
			c = new ContactEntity("31111111", "c@cnblogs.com", "123", "CCC", "c@cnblogs.com");
			session.save(c);
			session.flush();

			System.out.println("\n===Login Test===");
			contacts = session.createQuery("from ContactEntity where LoginId=:loginId").setString("loginId", loginId)
					.list();
			if (contacts.isEmpty())
				System.out.println("contact " + loginId + " not found!");
			else {
				c = (ContactEntity) contacts.get(0);
				if (c.getPassword() == password)
					System.out.println("user " + loginId + " login successful");
				else
					System.out.println("password is incorrect, login failed!");
			}

			System.out.println("\n===Delete Contacts===");
			contacts = session.createQuery("from ContactEntity").list();
			it = contacts.iterator();
			while (it.hasNext()) {
				session.delete(it.next());
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
			if (factory != null)
				factory.close();
		}
	}

	@Test
	public void test2() {
		String loginId = "RicCC@cnblogs.com";
		String password = "123";
		SessionFactory factory = null;
		String mainConfig = "db1.xml";
		String entityConfig = "entity.xml";
		List<String> dbConfig = new ArrayList<>();
		dbConfig.add("db2.xml");
		try {
			factory = createSessionFactory(mainConfig, entityConfig, dbConfig);
			ShardsTestCreate(factory);
			ShardsTestLogin(factory, loginId, password);
			ShardsTestDelete(factory);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (factory != null)
				factory.close();
		}
	}

	@Test
	public void test3() {
		SessionFactory factory = null;
		String mainConfig = "db1.xml";
		String entityConfig = "entity.xml";
		List<String> dbConfig = new ArrayList<>();
		dbConfig.add("db2.xml");
		try {
			factory = createSessionFactory(mainConfig, entityConfig, dbConfig);
			HibernateTemplate hibernateTemplate = new HibernateTemplate(factory);
			hibernateTemplate.saveOrUpdate(
					new ContactEntity("01111111", "RicCC@cnblogs.com", "123", "Richie", "RicCC@cnblogs.com"));

			ContactEntity entity = hibernateTemplate.get(ContactEntity.class, "01111111");
			if (entity != null) {
				System.out.println(entity.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (factory != null)
				factory.close();
		}
	}

	private static void ShardsTestCreate(SessionFactory factory) {
		Session session = null;
		Transaction transaction = null;
		System.out.println("===Create Contacts===");
		try {
			session = factory.openSession();
			transaction = session.beginTransaction();
			session.save(new ContactEntity("01111111", "RicCC@cnblogs.com", "123", "Richie", "RicCC@cnblogs.com"));
			session.save(new ContactEntity("91111111", "a@cnblogs.com", "123", "AAA", "a@cnblogs.com"));
			session.save(new ContactEntity("81111111", "b@cnblogs.com", "123", "BBB", "b@cnblogs.com"));
			session.save(new ContactEntity("31111111", "c@cnblogs.com", "123", "CCC", "c@cnblogs.com"));
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}
	}

	private static void ShardsTestLogin(SessionFactory factory, String loginId, String password) {
		Session session = null;
		ContactEntity c = null;
		System.out.println("\n===Login Test===");
		try {
			session = factory.openSession();
			List contacts = session.createQuery("from ContactEntity where LoginId=:loginId")
					.setString("loginId", loginId).list();
			if (contacts.isEmpty())
				System.out.println("Contact \"" + loginId + "\" not found!");
			else {
				c = (ContactEntity) contacts.get(0);
				if (c.getPassword().equals(password))
					System.out.println("Contact \"" + loginId + "\" login successful");
				else
					System.out.println(
							"Password is incorrect (should be: " + c.getPassword() + ", but is: " + password + ")");
			}
			System.out.println("\n===Get Contact by Id===");
			c = (ContactEntity) session.get(ContactEntity.class, "81111111");
			System.out.println(c.toString());
			c = (ContactEntity) session.get(ContactEntity.class, "31111111");
			System.out.println(c.toString());
		} catch (Exception e) {
 			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}
	}

	private static void ShardsTestDelete(SessionFactory factory) {
		Session session = null;
		Transaction transaction = null;
		System.out.println("\n===Delete Contacts===");
		try {
			session = factory.openSession();
			transaction = session.beginTransaction();
			List contacts = session.createQuery("from ContactEntity").list();
			Iterator it = contacts.iterator();
			while (it.hasNext()) {
				session.delete(it.next());
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
 			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}
	}

	/////////////////////////////////////////// 官方例子////////////////////////////////////////////////////
	// public SessionFactory createSessionFactory() {
	// Configuration config = new Configuration();
	// config.configure("weather.hibernate.cfg.xml");
	// config.addResource("weather.hbm.xml");
	// return config.buildSessionFactory();
	// }

	public static SessionFactory createSessionFactory(String mainConfig, String mainEntityConfig,
			List<String> dbConfig) {
		Configuration prototypeConfig = new Configuration().configure(mainConfig);
		// prototypeConfig.addResource(mainEntityConfig);
		List<ShardConfiguration> shardConfigs = new ArrayList<ShardConfiguration>();
		for (String config : dbConfig) {
			shardConfigs.add(buildShardConfig(config));
		}

		ShardStrategyFactory shardStrategyFactory = buildShardStrategyFactory();

		ShardedConfiguration shardedConfig = new ShardedConfiguration(prototypeConfig, shardConfigs,
				shardStrategyFactory);
		return shardedConfig.buildShardedSessionFactory();
	}

	static ShardStrategyFactory buildShardStrategyFactory() {
		ShardStrategyFactory shardStrategyFactory = new ShardStrategyFactory() {
			public ShardStrategy newShardStrategy(List<ShardId> shardIds) {
				RoundRobinShardLoadBalancer loadBalancer = new RoundRobinShardLoadBalancer(shardIds);
				// ShardSelectionStrategy pss = new
				// RoundRobinShardSelectionStrategy(loadBalancer);
				ShardResolutionStrategy prs = new AllShardsShardResolutionStrategy(shardIds);
				ShardAccessStrategy pas = new SequentialShardAccessStrategy();

				ShardSelectionStrategy pss = new MyShardSelectionStrategy(shardIds);
				// ShardResolutionStrategy prs = new
				// MyShardResolutionStrategy(shardIds);
				return new ShardStrategyImpl(pss, prs, pas);
			}
		};
		return shardStrategyFactory;
	}

	static ShardConfiguration buildShardConfig(String configFile) {
		Configuration config = new Configuration().configure(configFile);
		return new ConfigurationToShardConfigurationAdapter(config);
	}

}
