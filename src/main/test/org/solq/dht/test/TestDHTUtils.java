package org.solq.dht.test;

import org.junit.Assert;
import org.junit.Test;
import org.solq.dht.core.protocol.bencode.BDecodeUtils;
import org.solq.dht.core.protocol.krpc.impl.FindNodeMessage;
import org.solq.dht.core.util.DHTUtils;

public class TestDHTUtils {

	// FORMAT C TYPE PYTHON TYPE STANDARD SIZE NOTES
	// x pad byte no value
	// c char string of length 1 1
	// b signed char integer 1 (3)
	// B unsigned char integer 1 (3)
	// ? _Bool bool 1 (1)
	// h short integer 2 (3)
	// H unsigned short integer 2 (3)
	// i int integer 4 (3)
	// I unsigned int integer 4 (3)
	// l long integer 4 (3)
	// L unsigned long integer 4 (3)
	// q long long integer 8 (2), (3)
	// Q unsigned long long integer 8 (2), (3)
	// f float float 4 (4)
	// d double float 8 (4)
	// s char[] string
	// p char[] string
	// P void * integer (5), (3)

	// CHARACTER BYTE ORDER SIZE ALIGNMENT
	// @ native native native
	// = native standard none
	// < little-endian standard none
	// > big-endian standard none
	// ! network (= big-endian) standard none

	@Test
	public void testBytesToLong() {
		byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, -7 };
		long r = DHTUtils.bytesToLong(bytes);
		String r2 = DHTUtils.bytesToHex1(bytes);

		System.out.println(r);
		System.out.println(r2);

		// return pack("!" + "20sIH" * len(nodes), *n)
		// nodes = unpack("!" + "20sIH" * nrnodes, nodes)
	}

	@Test
	public void testBytesToHex() {
		byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, -7 };

		System.out.println(DHTUtils.bytesToHex(bytes));
		System.out.println(DHTUtils.bytesToHex1(bytes));
	}

	@Test
	public void testHexToBytes() {
		byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, -7 };
		String hex = "01020304050607F9";
		Assert.assertArrayEquals(DHTUtils.hexToBytes(hex), bytes);
	}

	@Test
	public void testbencode() {
		byte[] requestMessage = FindNodeMessage.ofRequest().toRequestMessage();
		System.out.println(new String(requestMessage));
	}
	
	@Test
	public void testbdecode() {
		byte[] requestMessage = FindNodeMessage.ofRequest().toRequestMessage();
		Object responseMessage= BDecodeUtils.bdecode(requestMessage);
		System.out.println(requestMessage);
	}

}
