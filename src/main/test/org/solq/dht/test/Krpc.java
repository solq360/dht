package org.solq.dht.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.math3.util.Pair;
import org.solq.dht.core.protocol.bencode.Parser;
import org.solq.dht.core.protocol.bencode.StreamParser;
import org.solq.dht.core.protocol.bencode.codec.BArray;
import org.solq.dht.core.protocol.bencode.codec.BDictionary;
import org.solq.dht.core.protocol.bencode.codec.BInt;
import org.solq.dht.core.protocol.bencode.codec.BString;
import org.solq.dht.core.protocol.bencode.codec.BType;
import org.solq.dht.core.protocol.bencode.codec.TypeFactory;

/**
 * <p>
 * KRPC 协议是由 bencode 编码组成的一个简单的 RPC 结构，他使用 UDP 报文发送。
 * </p>
 * <p>
 * 一个独立的请求包被发出去然后一个独立的包被回复。这个协议没有重发。
 * </p>
 * <p>
 * 它包含 3 种消息：请求，回复和错误。
 * </p>
 * <p>
 * 对DHT协议而言，这里有 4 种请求：ping，find_node，get_peers 和 announce_peer
 * </p>
 * 
 * @author solq
 */
public class Krpc {

	public static class EncodeDemo {
		public static void main(String[] args) {
			BInt bInt = TypeFactory.bInt(-50);
			BString bString = TypeFactory.bString("str");
			BString bString2 = TypeFactory.bString("str2");
			BArray bArray = TypeFactory.bArray(bInt, bString);
			BDictionary bDictionary1 = TypeFactory.bDictionary(new Pair<>(bString, bArray));
			BDictionary bDictionary2 = TypeFactory.bDictionary(new Pair<>(bString, bDictionary1),
					new Pair<>(bString2, bInt));

			System.out.println(bDictionary2.encode());
			System.out.println("---------------------------");

			System.out.println(bDictionary2.toString());
		}
	}

	public static class DecodeDemo {
		public static void main(String[] args) throws IOException {
			Parser<InputStream> parser = new StreamParser();
			List<BType<?>> list = parser
					.parse("i100e7:bencodeld6:stringli-123e6:stringeei-123e6:stringli-123e6:stringee".getBytes());

			list.forEach(System.out::println);
		}
	}
}
