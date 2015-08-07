package org.solq.dht.core.protocol.krpc.impl;

import java.util.HashMap;
import java.util.Map;

import org.solq.dht.core.protocol.krpc.CommonMessage;
import org.solq.dht.core.protocol.krpc.KRPCProtocol;

/***
 * ping 消息
 * @author solq
 */
public class PingMessage extends CommonMessage {

	private String senderId;

	@Override
	public String toRequestMessage() {
		// message = {
		// "y": "q",
		// "q": "ping",
		// "a": {
		// "id": sender_id
		// }
		// }
		Map<String, Object> message = new HashMap<>(3);
		message.put(KRPCProtocol.HEARD_Y, KRPCProtocol.HEARD_REQUEST);
		message.put(KRPCProtocol.HEARD_Q, KRPCProtocol.REQUEST_PING);
		Map<String, Object> body = new HashMap<>(1);
		message.put(KRPCProtocol.HEARD_A, body);
		body.put(KRPCProtocol.HEARD_ID, senderId);

		return toMessage(body);
	}

	@Override
	public String toResponseMessage() {
		// message = {
		// "y": "r",
		// "r": {
		// "id": sender_id
		// }
		// }
		Map<String, Object> message = new HashMap<>(2);
		message.put(KRPCProtocol.HEARD_Y, KRPCProtocol.HEARD_RESPONSE);
		Map<String, Object> body = new HashMap<>(3);
		message.put(KRPCProtocol.HEARD_R, body);
		body.put(KRPCProtocol.HEARD_ID, senderId);

		return toMessage(body);
	}

}
