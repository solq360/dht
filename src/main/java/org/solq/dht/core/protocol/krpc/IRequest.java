package org.solq.dht.core.protocol.krpc;

/**
 * krpc Request 接口
 * 
 * @author solq
 */
public interface IRequest extends IMessage {
	/**
	 * 转换成发送消息
	 * */
	public String toRequestMessage();
}
