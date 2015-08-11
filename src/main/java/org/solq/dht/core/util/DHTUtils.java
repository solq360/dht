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

	public static String bytesToHex1(byte[] b) {
		String result = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				result = result + "0" + stmp;
			else
				result = result + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return result.toUpperCase();
	}

	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte[] hexToBytes(String s) {
		int len = s.length();
		if (len % 2 == 1) {
			throw new IllegalArgumentException("HexString uneven length:'" + s + "'");
		}
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
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
