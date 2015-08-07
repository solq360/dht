package org.solq.dht.core.dht;

public interface IKBucket {
	// 1. 查找所要添加的节点对应路由表的桶是否已经满，如果未满，添加节点
	// 2. 如果已经满，检查该桶中是否包含爬虫节点自己，如果不包含，抛弃待添加节点
	// 3. 如果该桶中包含本节点，则平均分裂该桶
	//
	// 其他的诸如 locateNode ,
	// replaceNode , updateNode ,
	// removeNode
}
