package org.solq.dht.db.redis.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 仓库策略
 * 
 * @author solq
 */
@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface StoreStrategy {

	/** 数据来源 */
	String dataSource();
 
}
