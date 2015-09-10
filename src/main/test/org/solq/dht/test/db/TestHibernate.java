package org.solq.dht.test.db;

import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.junit.Test;
import org.solq.dht.db.DBDriver;
import org.solq.dht.db.DbUtils;
import org.solq.dht.db.JdbcConfig;
import org.solq.dht.test.db.model.TestModel;
import org.springframework.orm.hibernate3.HibernateTemplate;

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
		TestModel entity = hibernateTemplate.load(TestModel.class, "234");
		System.out.println(entity == null);

	}

	@Test
	public void tplH2Op() throws Exception {

		JdbcConfig config = JdbcConfig.of(DBDriver.H2, H2Demo.sourceURL3, H2Demo.user, H2Demo.password, scanPackages);
		SessionFactory sessionFactory = DbUtils.buildSessionFactory(config);
		HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
		hibernateTemplate.afterPropertiesSet();
		Map<String, ClassMetadata> classMetadataMap = sessionFactory.getAllClassMetadata();
		System.out.println(classMetadataMap.size());
		TestModel entity = hibernateTemplate.load(TestModel.class, "234");
		System.out.println(entity == null);

	}

}
