package org.solq.dht.test.codec;

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

}
