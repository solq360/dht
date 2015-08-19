package org.solq.dht.core.protocol.bencode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.solq.dht.core.protocol.bencode.codec.BEncodeUtils;

/***
 * @author solq
 */
public abstract class BDecodeUtils {

	static BObject parseNext(InputStream is) throws IOException {
		Object value = null;
		Class<?> clz;
		char c = (char) is.read();
		if (c == BEncodeUtils.INT) {
			clz = Integer.class;
			value = Integer.valueOf(BProtocol.readDataUntilToken(is, BEncodeUtils.END));
		} else if (c == BEncodeUtils.ARRAY) {
			clz = Array.class;
			List<BObject> _value = new LinkedList<>();
			BObject object = null;
			while ((object = parseNext(is)) != null) {
				_value.add(object);
			}
			value = _value;
		} else if (c == BEncodeUtils.DICTIONARY) {
			clz = Map.class;
			Map<String, Object> _value = new HashMap<>();
			while (true) {
				BObject bKey = parseNext(is);
				BObject bValue = parseNext(is);
				if (bKey == null) {
					break;
				}
				if (!bKey.getClz().equals(String.class)) {
					throw new IllegalArgumentException("bdecode 解码MAP类型KEY 不是String : " + bKey.getClz());
				}
				_value.put(bKey.getValue().toString(), bValue);
			}
			value = _value;

		} else if (Character.isDigit(c)) {
			clz = String.class;

			String restLength = BProtocol.readDataUntilToken(is, BEncodeUtils.STRING_SPLITTER);
			int len = Integer.valueOf(c + restLength);
			char[] chars = new char[len];
			int b;
			for (int i = 0; i < len; i++) {
				b = is.read();
				if (b == -1) {
					throw new IllegalStateException("Unexpected stream end, last chars " + len);
				} else {
					chars[i] = (char) b;
				}
			}
			value = new String(chars);
		} else if (c == BEncodeUtils.END) {
			return null;
		} else if ((byte) c == -1) {
			return null;
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