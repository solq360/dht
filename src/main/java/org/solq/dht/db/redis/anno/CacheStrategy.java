package org.solq.dht.db.redis.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 缓存策略
 * 
 * @author solq
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheStrategy {

	/***
	 * 有限数据长度 少于1代表无限制 默认2000条
	 */
	int lenth() default 2000;

	/**
	 * 有效时间 少于1代表无限制 默认有效30分钟
	 */
	long expires() default 1000 * 60 * 30;
}
