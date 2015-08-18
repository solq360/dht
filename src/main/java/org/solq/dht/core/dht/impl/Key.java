package org.solq.dht.core.dht.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;

public class Key {
	public final transient static int ID_LENGTH = 160;
	public final transient static int LENGTH = ID_LENGTH / 8;
	// private BitSet val;
	private byte[] keyBytes;

	public Key(String data) {
		this(data.getBytes());
	}

	public Key(byte[] keyBytes) {
		if (keyBytes.length != LENGTH) {
			throw new IllegalArgumentException("Specified Data need to be " + (ID_LENGTH / 8) + " characters long.");
		}
		this.keyBytes = keyBytes;
	}

	public Key() {
		keyBytes = new byte[LENGTH];
		new Random().nextBytes(keyBytes);
	}
	///////////////////////////// 领域方法/////////////////////////////////

	public int getDistance(Key to) {
		/**
		 * Compute the xor of this and to Get the index i of the first set bit
		 * of the xor returned NodeId The distance between them is ID_LENGTH - i
		 */
		return ID_LENGTH - this.xor(to).getFirstSetBitIndex();
	}

	/**
	 * Counts the number of leading 0's in this NodeId
	 *
	 * @return Integer The number of leading 0's
	 */
	public int getFirstSetBitIndex() {
		int prefixLength = 0;

		for (byte b : this.keyBytes) {
			if (b == 0) {
				prefixLength += 8;
			} else {
				/* If the byte is not 0, we need to count how many MSBs are 0 */
				int count = 0;
				for (int i = 7; i >= 0; i--) {
					boolean a = (b & (1 << i)) == 0;
					if (a) {
						count++;
					} else {
						break; // Reset the count if we encounter a non-zero
								// number
					}
				}

				/* Add the count of MSB 0s to the prefix length */
				prefixLength += count;

				/* Break here since we've now covered the MSB 0s */
				break;
			}
		}
		return prefixLength;
	}

	/**
	 * Generates a NodeId that is some distance away from this NodeId
	 *
	 * @param distance
	 *            in number of bits
	 *
	 * @return NodeId The newly generated NodeId
	 */
	public Key generateNodeIdByDistance(int distance) {
		byte[] result = new byte[LENGTH];

		/*
		 * Since distance = ID_LENGTH - prefixLength, we need to fill that
		 * amount with 0's
		 */
		int numByteZeroes = (ID_LENGTH - distance) / 8;
		int numBitZeroes = 8 - (distance % 8);

		/* Filling byte zeroes */
		for (int i = 0; i < numByteZeroes; i++) {
			result[i] = 0;
		}

		/* Filling bit zeroes */
		BitSet bits = new BitSet(8);
		bits.set(0, 8);

		for (int i = 0; i < numBitZeroes; i++) {
			/* Shift 1 zero into the start of the value */
			bits.clear(i);
		}
		bits.flip(0, 8); // Flip the bits since they're in reverse order
		result[numByteZeroes] = (byte) bits.toByteArray()[0];

		/* Set the remaining bytes to Maximum value */
		for (int i = numByteZeroes + 1; i < result.length; i++) {
			result[i] = Byte.MAX_VALUE;
		}

		return this.xor(new Key(result));
	}

	public BigInteger toInt() {
		return new BigInteger(1, keyBytes);
	}

	public String toHex() {
		/* Returns the hex format of this NodeId */
		BigInteger bi = toInt();
		return String.format("%0" + (this.keyBytes.length << 1) + "X", bi);
	}

	public Key xor(Key nid) {
		byte[] result = new byte[LENGTH];
		byte[] nidBytes = nid.keyBytes;

		for (int i = 0; i < LENGTH; i++) {
			result[i] = (byte) (this.keyBytes[i] ^ nidBytes[i]);
		}

		Key resNid = new Key(result);
		return resNid;
	}

	public void toStream(DataOutputStream out) {
		try {
			out.write(this.keyBytes);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void fromStream(DataInputStream in) {
		try {
			byte[] input = new byte[LENGTH];
			in.readFully(input);
			this.keyBytes = input;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Key) {
			Key nid = (Key) o;
			return this.hashCode() == nid.hashCode();
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 83 * hash + Arrays.hashCode(this.keyBytes);
		return hash;
	}

	// get
	public byte[] getKeyBytes() {
		return keyBytes;
	}

}
