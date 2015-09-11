package org.solq.dht.test.db;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.metadata.ClassMetadata;
import org.junit.Test;
import org.solq.dht.db.config.MulitDataSource;
import org.solq.dht.db.model.DbMetadata;
import org.solq.dht.test.db.model.TestModel;

public class TestMulitDataSource {

	@Test
	public void test() throws Exception {
		Map<String, String> configs = new HashMap<>();
		configs.put("test1", "test1");
		MulitDataSource mulitDataSource = MulitDataSource.of("bdb.properties", configs);
		mulitDataSource.initBuild();
		Map<String, DbMetadata> dbMetadatad = mulitDataSource.getDbMetadatad();
		DbMetadata dbMetadata = dbMetadatad.get("test1");

		org.springframework.orm.hibernate3.HibernateTemplate hibernateTemplate = dbMetadata.getHibernateTemplate()
				.getHibernateTemplate();

		Map<String, ClassMetadata> classMetadataMap = hibernateTemplate.getSessionFactory().getAllClassMetadata();
		System.out.println("classMetadata size : " + classMetadataMap.size());
		System.out.println("check : " + hibernateTemplate.get(TestModel.class, "aaaa"));

		hibernateTemplate.saveOrUpdate("test_01", TestModel.of("test01"));
		System.out.println("check : " + hibernateTemplate.get(TestModel.class, "test01"));

	}

	@Test
	public void test2() throws Exception {
		Map<String, String> configs = new HashMap<>();
		configs.put("test2", "test2");
		MulitDataSource mulitDataSource = MulitDataSource.of("bdb.properties", configs);
		mulitDataSource.initBuild();
		Map<String, DbMetadata> dbMetadatad = mulitDataSource.getDbMetadatad();
		DbMetadata dbMetadata = dbMetadatad.get("test2");

		org.springframework.orm.hibernate3.HibernateTemplate hibernateTemplate = dbMetadata.getHibernateTemplate()
				.getHibernateTemplate();

		Map<String, ClassMetadata> classMetadataMap = hibernateTemplate.getSessionFactory().getAllClassMetadata();
		System.out.println("classMetadata size : " + classMetadataMap.size());
		System.out.println("check : " + hibernateTemplate.get(TestModel.class, "aaaa"));

		hibernateTemplate.saveOrUpdate("test_01", TestModel.of("test01"));
		System.out.println("check : " + hibernateTemplate.get(TestModel.class, "test01"));

	}

}
