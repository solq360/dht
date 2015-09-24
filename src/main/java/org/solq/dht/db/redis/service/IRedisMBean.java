package org.solq.dht.db.redis.service;

import javax.management.MXBean;

@MXBean
public interface IRedisMBean {

	/** 获取redis 数据库总使用容量 */
	public long getDbUseSize();
}
