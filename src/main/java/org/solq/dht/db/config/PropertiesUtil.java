package org.solq.dht.db.config;

import java.util.Properties;

public abstract class PropertiesUtil {

	public static String[] getArrayValue(Properties properties, String name, boolean istry) {
		String result = properties.getProperty(name);
		if (istry && (result == null || result == "")) {
			throw new RuntimeException("未找到 : " + name);
		}
		if (result == null || result == "") {
			return null;
		}
		return result.split(",");
	}

	public static String getValue(Properties properties, String name, boolean istry) {
		String result = properties.getProperty(name);
		if (istry && (result == null || result == "")) {
			throw new RuntimeException("未找到 : " + name);
		}
		if (result == null || result == "") {
			return null;
		}
		return result;
	}

	public static Integer getIntValue(Properties properties, String name, boolean istry) {
		String result = properties.getProperty(name);
		if (istry && (result == null || result == "")) {
			throw new RuntimeException("未找到 : " + name);
		}
		if (result == null || result == "") {
			return null;
		}
		return Integer.valueOf(result);
	}

	public static Boolean getBooleanValue(Properties properties, String name, boolean istry) {
		String result = properties.getProperty(name);
		if (istry && (result == null || result == "")) {
			throw new RuntimeException("未找到 : " + name);
		}
		if (result == null || result == "") {
			return null;
		}
		return Boolean.valueOf(result);
	}
}
