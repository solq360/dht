package org.solq.dht.core.protocol.krpc.impl;

import java.util.HashMap;
import java.util.Map;

import org.solq.dht.core.protocol.krpc.CommonMessage;
import org.solq.dht.core.protocol.krpc.KRPCProtocol;

/***
 * GetPeers 消息
 * @author solq
 */
public class GetPeersMessage extends CommonMessage {

 	private Object senderId;
	private Object info_hash;
	private Object nodes;

	@Override
	public byte[] toRequestMessage() {
//		 message = {
//		            "y": "q",
//		            "q": "get_peers",
//		            "a": { 
//		                "id": sender_id,
//		                "info_hash": info_hash
//		            }
//		        }

		Map<String, Object> message = new HashMap<>(3);
		message.put(KRPCProtocol.HEARD_Y, KRPCProtocol.HEARD_REQUEST);
		message.put(KRPCProtocol.HEARD_Q, KRPCProtocol.REQUEST_GET_PEERS);
		Map<String, Object> body = new HashMap<>(1);
		message.put(KRPCProtocol.HEARD_A, body);
		body.put(KRPCProtocol.HEARD_ID, senderId);
		body.put(KRPCProtocol.HEARD_INFO_HASH, info_hash);

		return toMessage(message);
	}

	@Override
	public byte[] toResponseMessage() {
//		 message = {
//		 "y": "r",
//		 "r": {
//		 "id": sender_id,
//		 "nodes": found_nodes
//		 }
//		 }
		Map<String, Object> message = new HashMap<>(2);
		message.put(KRPCProtocol.HEARD_Y, KRPCProtocol.HEARD_RESPONSE);
		Map<String, Object> body = new HashMap<>(2);
		message.put(KRPCProtocol.HEARD_R, body);
		body.put(KRPCProtocol.HEARD_ID, senderId);
		body.put(KRPCProtocol.HEARD_NODES, nodes);

		return toMessage(message);
	}

}
