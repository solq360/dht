package org.solq.dht.core.protocol.bencode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/***
 * @author solq
 */
public abstract class BDecodeUtils {

	static BObject parseNext(InputStream is) throws IOException {
		Object value = null;
		Class<?> clz;
		is.mark(0);
		char c = (char) is.read();
		if ((byte) c == -1 || c == BProtocol.TYPE_END) {
			return null;
		}
		if (c == BProtocol.TYPE_INT) {
 
			clz = Integer.class; 
			value = Integer.valueOf(BProtocol.readDataUntilToken(is, BProtocol.TYPE_END));

		} else if (c == BProtocol.TYPE_ARRAY) {
			clz = Array.class;
			List<BObject> _value = new LinkedList<>();
			BObject object = null;
			while ((object = parseNext(is)) != null) {
				_value.add(object);
			}
			value = _value;
		} else if (c == BProtocol.TYPE_DICTIONARY) {
			clz = Map.class;
			Map<String, Object> _value = new HashMap<>();
			while (true) {
				int keylen = BProtocol.readLength(is, BProtocol.TYPE_STRING_SPLITTER,BProtocol.TYPE_END);
				if (keylen <= 0) {
					break;
				}
				byte[] keyBytes = new byte[keylen];
				is.read(keyBytes);
				String key = new String(keyBytes, Charset.forName(BProtocol.CHAR_SET));
				
				BObject bValue = parseNext(is);
				_value.put(key, bValue);
			}
			value = _value;

		} else if (Character.isDigit(c)) {
			// 还原上一次读的 char
			is.reset();
			clz = String.class;
			int length = BProtocol.readLength(is, BProtocol.TYPE_STRING_SPLITTER,BProtocol.TYPE_END);
			byte[] valueBtyes = new byte[length];
			is.read(valueBtyes);
			value = new String(valueBtyes, Charset.forName(BProtocol.CHAR_SET)); 			
		} else {
			throw new IllegalArgumentException("bdecode 解码类型错误 : ");
		}

		return BObject.of(value, clz);
	}

	public static Object bdecode(InputStream is) {
		List<BObject> objects = new LinkedList<>();
		BObject object = null;
		try {
			while ((object = parseNext(is)) != null) {
				objects.add(object);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("bdecode 解码类型错误 : ", e);
		}
		return objects;
	}

	public static Object bdecode(byte[] bytes) {
		return bdecode(new ByteArrayInputStream(bytes));
	}

	public static Object bdecode(String msg) {
		return bdecode(msg.getBytes());
	}

}