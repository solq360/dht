package org.solq.dht.core.dht.impl;

import java.math.BigInteger;
import java.util.Comparator;

import org.solq.dht.core.dht.IKNode;

/***
 * KNode 实现
 * 
 * @author solq
 */
public class KNode implements IKNode, Comparator<KNode> {

	private Key nid;
	private String host;
	private int port;

	public static KNode of(Key nid, String host, int port) {
		KNode result = new KNode();
		result.host = host;
		result.port = port;
		result.nid = nid;
		return result;
	}

	@Override
	public int hashCode() {
		return nid.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KNode other = (KNode) obj;
		if (nid == null) {
			if (other.nid != null)
				return false;
		} else if (!nid.equals(other.nid))
			return false;
		return true;
	}

	// getter

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public Key getNid() {
		return nid;
	}

	@Override
	public int compare(KNode o1, KNode o2) {
		BigInteger b1 = o1.nid.toInt();
		BigInteger b2 = o2.nid.toInt();

		b1 = b1.xor(b1);
		b2 = b2.xor(b1);

		return b1.abs().compareTo(b2.abs());
	}

}
