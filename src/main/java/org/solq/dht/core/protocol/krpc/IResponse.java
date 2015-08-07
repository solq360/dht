package org.solq.dht.core.protocol.krpc;

/**
 * krpc Response 接口
 * 
 * @author solq
 */
public interface IResponse extends IMessage {
	/**
	 * 转换成影响消息
	 * */
	public String toResponseMessage();
}
