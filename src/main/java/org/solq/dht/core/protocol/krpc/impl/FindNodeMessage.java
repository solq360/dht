package org.solq.dht.core.protocol.krpc.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.solq.dht.core.protocol.krpc.CommonMessage;
import org.solq.dht.core.protocol.krpc.KRPCProtocol;
import org.solq.dht.core.util.DHTUtils;

/***
 * FindNode 消息
 * 
 * @author solq
 */
public class FindNodeMessage extends CommonMessage {

	private byte[] target;
	private byte[] senderId;
	private List<Object> nodes;

	public static FindNodeMessage ofRequest() {
		FindNodeMessage result = new FindNodeMessage();
		result.target = DHTUtils.randomNodeId();
		result.senderId = DHTUtils.randomNodeId();
		return result;
	}

	@Override
	public byte[] toRequestMessage() {
		// message = {
		// "y": "q",
		// "q": "find_node",
		// "a": {
		// "id": sender_id,
		// "target": target_id
		// }
		// }

		Map<String, Object> message = new HashMap<>(3);
		message.put(KRPCProtocol.HEARD_Y, KRPCProtocol.HEARD_REQUEST);
		message.put(KRPCProtocol.HEARD_Q, KRPCProtocol.REQUEST_FIND_NODE);
		Map<String, Object> body = new HashMap<>(1);
		message.put(KRPCProtocol.HEARD_A, body);
		body.put(KRPCProtocol.HEARD_ID, senderId);
		body.put(KRPCProtocol.HEARD_TARGET, target);

		return toMessage(body);
	}

	@Override
	public byte[] toResponseMessage() {
		// message = {
		// "y": "r",
		// "r": {
		// "id": sender_id,
		// "nodes": found_nodes
		// }
		// }
		Map<String, Object> message = new HashMap<>(2);
		message.put(KRPCProtocol.HEARD_Y, KRPCProtocol.HEARD_RESPONSE);
		Map<String, Object> body = new HashMap<>(2);
		message.put(KRPCProtocol.HEARD_R, body);
		body.put(KRPCProtocol.HEARD_ID, senderId);
		body.put(KRPCProtocol.HEARD_NODES, nodes);

		return toMessage(body);
	}

}
