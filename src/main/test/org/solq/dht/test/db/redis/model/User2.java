package org.solq.dht.test.db.redis.model;

import java.io.Serializable;

import org.solq.dht.db.redis.IRedisEntity;

public class User2 implements Serializable, IRedisEntity {

	private static final long serialVersionUID = -1267719235225203410L;

	private String uid;

	private int age;

	public static User2 of(String uid, int age) {
		User2 result = new User2();
		result.uid = uid;
		result.age = age;
		return result;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toId() {
		return uid;
	}

}