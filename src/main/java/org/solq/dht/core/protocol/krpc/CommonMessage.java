package org.solq.dht.core.protocol.krpc;

import java.util.Map;

/**
 * Message 模板
 * 
 * @author solq
 */
public abstract class CommonMessage  implements IRequest, IResponse{

	protected String toMessage(Map<String, Object> body){
		return "";
	}
}
