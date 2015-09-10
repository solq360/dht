package org.solq.dht.test.db;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationBinder;
import org.hibernate.metadata.ClassMetadata;
import org.junit.Test;
import org.solq.dht.db.DbUtils;
import org.solq.dht.db.HibernateTemplate;
import org.solq.dht.db.MyInterceptor;
import org.solq.dht.db.config.DBDriver;
import org.solq.dht.db.config.JdbcConfig;
import org.solq.dht.test.db.model.TestModel;
 
//http://www.uml.org.cn/sjjm/201403141.asp
public class TestHibernate {

	public final static String[] scanPackages = { "org.solq.dht.test.db.model" };

	@Test
	public void sessionOp() throws Exception {

		JdbcConfig config = JdbcConfig.of(DBDriver.H2, H2Demo.sourceURL3, H2Demo.user, H2Demo.password, scanPackages);
		SessionFactory sessionFactory = DbUtils.buildSessionFactory(config);
		Session session = sessionFactory.openSession();
		Query query = session.createSQLQuery("select * from test");
		// Criteria criteria = session.createCriteria(Player.class,
		// "tttt_player");
		System.out.println(query.list().size());
		session.close();

	}

	@Test
	public void tplMysqlOp() throws Exception {

		JdbcConfig config = JdbcConfig.of(DBDriver.MYSQL_INNODB,
				"jdbc:mysql://127.0.0.1:8585/test_h?useUnicode=true&characterEncoding=utf-8", "root", "kwgkwg",
				scanPackages);
		SessionFactory sessionFactory = DbUtils.buildSessionFactory(config);
		HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
		hibernateTemplate.afterPropertiesSet();
		Map<String, ClassMetadata> classMetadataMap = sessionFactory.getAllClassMetadata();
		System.out.println(classMetadataMap.size());
		hibernateTemplate.saveOrUpdate(TestModel.of("5555"));
		TestModel entity = hibernateTemplate.get(TestModel.class, "5555");
 		if (entity != null) {
			System.out.println(entity.getId());
		}

	}

	@Test
	public void tplH2Op() throws Exception {

		JdbcConfig config = JdbcConfig.of(DBDriver.H2, H2Demo.sourceURL3, H2Demo.user, H2Demo.password, scanPackages);
		SessionFactory sessionFactory = DbUtils.buildSessionFactory(config);

		MyInterceptor interceptor = new MyInterceptor();// 我们的拦截器
		interceptor.setTargetTableName("testmodel");// 要拦截的目标表名
		interceptor.setTempTableName("testmodel_01"); // 要替换的子表名

		HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
		// hibernateTemplate.setEntityInterceptor(interceptor);
		hibernateTemplate.afterPropertiesSet();
		Map<String, ClassMetadata> classMetadataMap = sessionFactory.getAllClassMetadata();
		System.out.println("classMetadata size : " + classMetadataMap.size());
		String testId= "aaaaaa1";
		hibernateTemplate.saveOrUpdate(TestModel.of(testId));		
		
	 
		Session session = hibernateTemplate.getSessionFactory().openSession();
 		//不能直接用session 操作
		//session.saveOrUpdate(TestModel.of(testId)); 
		
		TestModel entity = hibernateTemplate.get(TestModel.class,testId);
 		if (entity != null) {
			System.out.println(entity.getId());
		}
		try {
			String hql = "From TestModel where id =?";
			Query query = session.createQuery(hql);
			query.setParameter(0, testId);
			List list = query.list();
			System.out.println("interceptor query : " + list.size());
		} finally {
			session.close();
 		}
		// AnnotationBinder.bindDefaults(mappings);
	}

	
	
	/////////////////////////////////http://www.iteye.com/topic/359230
}
