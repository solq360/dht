package org.solq.dht.core.protocol.krpc;

import java.util.Map;

import org.solq.dht.core.protocol.bencode.BEncodeUtils;

/**
 * Message 模板
 * 
 * @author solq
 */
public abstract class CommonMessage implements IRequest, IResponse {

	public  byte[] toMessage(Map<String, Object> body) {
		return BEncodeUtils.bencode(body);
 	}
	
	public String toMessageToString(Map<String, Object> body) {
		return BEncodeUtils.bencodeToString(body);
	}
}
