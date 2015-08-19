package org.solq.dht.core.protocol.bencode;

import java.io.IOException;
import java.io.InputStream;

public abstract class BProtocol {

	public static final char TYPE_INT = 'i';
	public static final char TYPE_ARRAY = 'l';
	public static final char TYPE_DICTIONARY = 'd';
	public static final char TYPE_STRING_SPLITTER = ':';
	public static final char TYPE_END = 'e';
	public static final String CHAR_SET = "UTF-8";

	public static String readDataUntilToken(InputStream is, char token) {
		StringBuilder sb = new StringBuilder();
		char ch;
		while (true) {
			try {
				ch = (char) is.read();
				if (ch == token) {
					break;
				} else if ((byte) ch == -1) {
					throw new IllegalStateException("Unexpected stream end, token " + token);
				} else {
					sb.append(ch);
				}
			} catch (IOException e) {
				throw new IllegalStateException("Unexpected stream end, token " + token);
			}

		}
		return sb.toString();
	}
}
