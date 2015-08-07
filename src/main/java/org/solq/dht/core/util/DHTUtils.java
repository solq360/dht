package org.solq.dht.core.util;

/**
 * @author solq
 */
public abstract class DHTUtils {

	public static byte[] randomNodeId() {
		return CryptUtils.SHA1(entropy(20));
	}

	public static long bytesToLong(byte[] bytes) {
		long result = 0;
		for (int i = 0; i < bytes.length; i++) {
			result += bytes[i];
		}
		return result;
	}

	public static String byte2HexStr(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
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
