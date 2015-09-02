package org.solq.dht.test.db;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.solq.dht.db.DBDriver;
import org.solq.dht.db.DbUtils;
import org.solq.dht.db.JdbcConfig;

//http://www.uml.org.cn/sjjm/201403141.asp
public class TestDB {

	@Test
	public void test() throws Exception {
		//jdbc:h2:nio:target/db/test;AUTO_SERVER=TRUE
		// jdbc:h2:target/db/test
		//file,tcp,
		JdbcConfig config = JdbcConfig.of(DBDriver.H2, "jdbc:h2:file:./target/testH2;AUTO_SERVER=TRUE", "a", "a");
		SessionFactory sessionFactory = DbUtils.buildSessionFactory(config);

		Session session = sessionFactory.openSession();
		Query query = session.createSQLQuery("select * from TestModel");
		//Criteria criteria = session.createCriteria(Player.class, "tttt_player");
		System.out.println(query.list().size());
		session.close();

	}
}
