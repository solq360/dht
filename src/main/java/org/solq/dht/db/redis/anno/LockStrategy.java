package org.solq.dht.db.redis.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 锁策略
 * 
 * @author solq
 */
@Target({ElementType.PACKAGE })
@Retention(RetentionPolicy.RUNTIME)
public @interface LockStrategy {

	/** 有效时间 */
	long expires();
	 
	/** 下次请求时间 */
	long sleepTime(); 

	/** 重试处理次数 */
	int times() default 25;
}
