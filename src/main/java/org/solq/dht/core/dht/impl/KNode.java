package org.solq.dht.core.dht.impl;

import org.solq.dht.core.dht.IKNode;

/***
 * KNode 实现
 * 
 * @author solq
 */
public class KNode implements IKNode {

	private String nid;
	private String host;
	private int port;
	
	
	// getter

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getNid() {
		return nid;
	}

}
