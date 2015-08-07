package org.solq.dht.core.util;

public abstract class DHTUtils {

	public static byte[] randomNodeId() {
		return CryptUtils.sha1(entropy(20));
	}

	public static String entropy(int len) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append((char) betweenInt(0, 255, true));
		}
		return sb.toString();
	}
 
	public static int betweenInt(int min, int max, boolean include) {
		// 修正边界值
		if (include) {
			max++;
		}
		return (int) (min + Math.random() * (max - min));
	}

}
