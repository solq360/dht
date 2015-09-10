package org.solq.dht.db.config;

import java.util.Properties;

public abstract class PropertiesUtil {
 
	public static String[] getArrayValue(Properties properties, String name, boolean istry) {
		String result = properties.getProperty(name);
		if (istry && result == null) {
			throw new RuntimeException("未找到 : " + name);
		}
		return result.split(",");
	}

	public static String getValue(Properties properties, String name, boolean istry) {
		String result = properties.getProperty(name);
		if (istry && result == null) {
			throw new RuntimeException("未找到 : " + name);
		}
		return result;
	}
	
	public static int getIntValue(Properties properties, String name, boolean istry) {
		String result = properties.getProperty(name);
		if (istry && result == null) {
			throw new RuntimeException("未找到 : " + name);
		}
		return Integer.valueOf(result);
	}
	
	public static boolean getBooleanValue(Properties properties, String name, boolean istry) {
		String result = properties.getProperty(name);
		if (istry && result == null) {
			throw new RuntimeException("未找到 : " + name);
		}
		return Boolean.valueOf(result);
	}
}
