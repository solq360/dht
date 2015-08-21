package org.solq.dht.core.protocol.bencode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * @author solq
 */
public abstract class BEncodeUtils {

	public static String bencodeToString(Object o) {
		byte[] bytes = bencode(o);
		return new String(bytes, Charset.forName(BProtocol.CHAR_SET));
	}

	public static byte[] bencode(Object o) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bencode(o, baos);
			return baos.toByteArray();
		} catch (IOException ioe) {
			throw new InternalError(ioe.toString());
		}
	}

	static void bencode(String s, OutputStream out) throws IOException {
		byte[] bs = s.getBytes(BProtocol.CHAR_SET);
		bencode(bs, out);
	}

	static void bencode(Number n, OutputStream out) throws IOException {
		out.write(BProtocol.TYPE_INT);
		out.write(n.toString().getBytes(BProtocol.CHAR_SET));
		out.write(BProtocol.TYPE_END);
	}

	static void bencode(List<Object> list, OutputStream out) throws IOException {
		out.write(BProtocol.TYPE_ARRAY);
		for (Object object : list) {
			bencode(object, out);
		}
		out.write(BProtocol.TYPE_END);
	}

	static void bencode(Map<String, Object> m, OutputStream out) throws IOException {
		out.write(BProtocol.TYPE_DICTIONARY);
		Set<String> s = m.keySet();
		List<String> l = new ArrayList<String>(s);
		Collections.sort(l);
		Object value;
		for (String key : l) {
			value = m.get(key);
			bencode(key, out);
			bencode(value, out);
		}
		out.write(BProtocol.TYPE_END);
	}

	/***
	 * 协议格式 : [类型字符][内容长度][分割符 : ][内容][结束符 e]
	 */
	static void bencode(byte[] bs, OutputStream out) throws IOException {
		out.write(Integer.toString(bs.length).getBytes(BProtocol.CHAR_SET));
		out.write(BProtocol.TYPE_STRING_SPLITTER);
		out.write(bs);
	}

	@SuppressWarnings("unchecked")
	static void bencode(Object o, OutputStream out) throws IOException, IllegalArgumentException {
		//System.out.println(new String( ((ByteArrayOutputStream)out).toByteArray()));
		if (o instanceof String) {
			bencode((String) o, out);
		} else if (o instanceof byte[]) {
			bencode((byte[]) o, out);
		} else if (o instanceof Number) {
			bencode((Number) o, out);
		} else if (o instanceof List) {
			bencode((List<Object>) o, out);
		} else if (o instanceof Map) {
			bencode((Map<String, Object>) o, out);
		} else {
			throw new IllegalArgumentException("bencode 解码类型错误 : " + o.getClass());
		}
	}

}