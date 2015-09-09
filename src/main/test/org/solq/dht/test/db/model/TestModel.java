package org.solq.dht.test.db.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TestModel {

	@Id
	private String id;

	// getter

	public String getId() {
		return id;
	}

	public static TestModel of(String id) {
		TestModel result = new TestModel();
		result.id = id;
		return result;
	}

}
