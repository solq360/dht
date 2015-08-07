package org.solq.dht.core.dht;

import java.util.List;

/**
 * 路由表 {@link http://www.tuicool.com/articles/iAbiue
 * http://blog.sina.com.cn/s/blog_5384aaf00100a88k.html}
 * 
 * @author solq
 */
public interface IKTable {
	// search ：比较重要的方法，主要使用它来定位当前infohash所在的桶的位置。会被其他各种代理方法调用到。
	// findNodes ：找到路由表中与传入的infohash最近的k个节点
	// getPeer ：找到待查资源是否有peer（即是否有人在下载，也就是是否有人announce过）
	// announcePeer ：通知该资源正在被下载
	public IKBucket search(int infoHash);

	public List<IKNode> getPeers(int infoHash);

	public List<IKNode> findNodes(int infoHash);

	public void announcePeer();
}
