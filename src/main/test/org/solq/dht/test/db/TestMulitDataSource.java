package org.solq.dht.test.db;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.metadata.ClassMetadata;
import org.junit.Test;
import org.solq.dht.db.HibernateTemplate;
import org.solq.dht.db.config.MulitDataSource;
import org.solq.dht.db.model.DbMetadata;

public class TestMulitDataSource {

	@Test
	public void test() throws Exception {
		Map<String, String> configs = new HashMap<>();
		configs.put("test1", "test1");
		MulitDataSource mulitDataSource = MulitDataSource.of("bdb.properties", configs);
		mulitDataSource.initBuild();
		Map<String, DbMetadata> dbMetadatad = mulitDataSource.getDbMetadatad();
		DbMetadata dbMetadata = dbMetadatad.get("test1");

		HibernateTemplate hibernateTemplate = dbMetadata.getHibernateTemplate();

		Map<String, ClassMetadata> classMetadataMap = hibernateTemplate.getSessionFactory().getAllClassMetadata();
		System.out.println("classMetadata size : " + classMetadataMap.size());
	}
}
