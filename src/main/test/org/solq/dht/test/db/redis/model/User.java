package org.solq.dht.test.db.redis.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.solq.dht.db.redis.model.IRedisEntity;

public class User implements Serializable, IRedisEntity {

	private static final long serialVersionUID = -1267719235225203410L;

	private String uid;

	private int age;

	private List<String> a = new LinkedList<>();
	private List<User> b = new LinkedList<>();
	private Map<String, User> c = new HashMap<String, User>();

	public static User of(String key, int age, int count) {
		User user = new User();
		user.uid = key;
		user.age = age;
		for (int i = 0; i < count; i++) {
			user.a.add(UUID.randomUUID().toString());
		}
		for (int i = 0; i < count; i++) {
			user.b.add(User.of2(key, age, 5));
		}
		for (int i = 0; i < count; i++) {
			user.c.put(UUID.randomUUID().toString(), User.of2(key, age, 5));
		}
		return user;
	}

	static User of2(String key, int age, int count) {
		User user = new User();
		user.uid = key;
		user.age = age;
		for (int i = 0; i < count; i++) {
			user.a.add(UUID.randomUUID().toString());
		}
		for (int i = 0; i < count; i++) {
			User sb = new User();
			sb.uid = key;
			sb.age = age;
			user.b.add(sb);
		}
		for (int i = 0; i < count; i++) {
			User sb = new User();
			sb.uid = key;
			sb.age = age;
			user.c.put(UUID.randomUUID().toString(), sb);
		}
		return user;
	}

	// get set

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<String> getA() {
		return a;
	}

	public void setA(List<String> a) {
		this.a = a;
	}

	public List<User> getB() {
		return b;
	}

	public void setB(List<User> b) {
		this.b = b;
	}

	public Map<String, User> getC() {
		return c;
	}

	public void setC(Map<String, User> c) {
		this.c = c;
	}

	@Override
	public String toId() {
		return uid;
	}

}