package org.solq.dht.test;

import org.junit.Test;
import org.solq.dht.core.util.DHTUtils;

public class TestDHTUtils {

	@Test
	public void testBytesToLong() {
		byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, -7 };
		long r = DHTUtils.bytesToLong(bytes);
		String r2 = DHTUtils.byte2HexStr(bytes);

		System.out.println(r);
		System.out.println(r2);
 	}
}
