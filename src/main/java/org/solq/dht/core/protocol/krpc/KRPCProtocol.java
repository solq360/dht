package org.solq.dht.core.protocol.krpc;

/**
 * KRPC 协议常量
 * 
 * @author solq
 */
public interface KRPCProtocol {

	//////////////////////// 请求头key//////////////////////////////
	public final static String HEARD_REQUEST = "q";
	public final static String HEARD_RESPONSE = "r";

	public final static String HEARD_Y = "y";
	public final static String HEARD_R = "r";
	public final static String HEARD_Q = "q";
	public final static String HEARD_A = "a";
	public final static String HEARD_ID = "id";
	public final static String HEARD_T = "t";
	public final static String HEARD_TARGET = "target";
	public final static String HEARD_TRANS_ID = "trans_id";
	public final static String HEARD_NODES = "nodes";
	public final static String HEARD_INFO_HASH = "info_hash";

	//////////////////////// 请求类型//////////////////////////////

	public final static String REQUEST_PING = "ping";
	public final static String REQUEST_FIND_NODE = "find_node";
	public final static String REQUEST_GET_PEERS = "get_peers";
	public final static String REQUEST_ANNOUNCE_PEER = "announce_peer";
}
