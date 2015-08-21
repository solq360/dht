package org.solq.dht.core.protocol.bencode;

import java.io.IOException;
import java.io.InputStream;
/***
 * bencode 协议常量
 * @author solq
 * */
public abstract class BProtocol {

	public static final char TYPE_INT = 'i';
	public static final char TYPE_ARRAY = 'l';
	public static final char TYPE_DICTIONARY = 'd';
	public static final char TYPE_STRING_SPLITTER = ':';
	public static final char TYPE_END = 'e';
	public static final String CHAR_SET = "UTF-8";

	public static String readDataUntilToken(InputStream is, char... tokens) {
		StringBuilder sb = new StringBuilder();
		char ch;
		while (true) {
			try {
				ch = (char) is.read();

				if (ch == -1) {
					break;
				}
				for (char c : tokens) {
					if (c == ch) {
						return sb.toString();
					}
				}
				sb.append(ch);

			} catch (IOException e) {
				throw new IllegalStateException("Unexpected stream end, token ");
			}

		}
		return sb.toString();
	}

	public static int readLength(InputStream is, char... tokens) {
		int length = 0;
		char read;
		try {
			while (true) {
				read = (char) is.read();
				if (read == -1) {
					break;
				}
				for (char c : tokens) {
					if (c == read) {
						return length;
					}
				}
				length = (length * 10) + Character.getNumericValue(read);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Unexpected stream end, token ", e);
		}
		return length;
	}
}
