package org.solq.dht.core.util;

import java.security.NoSuchAlgorithmException;

public abstract class CryptUtils {

	public static byte[] SHA1(String source){		
		try {
			java.security.MessageDigest alga = java.security.MessageDigest.getInstance("SHA-1");
			alga.update(source.getBytes());
			return alga.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-1", e);
		}
	}
}
