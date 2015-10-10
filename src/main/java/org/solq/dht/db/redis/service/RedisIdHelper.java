package org.solq.dht.db.redis.service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * redis id 生成帮助器
 * 
 * @author solq
 */
public abstract class RedisIdHelper {

	/***
	 * 每天ID
	 * */
	public static String toId(Class<?> clz, String id, Date date) {
		return clz.getName() + "_" + id + "_" + new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	
	/***
	 * 搜索每天ID
	 * */
	public static String search(Class<?> clz, String id, Date date){
		return clz.getName() + "_" + id + "_" + new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	
	/***
	 * 唯一ID
	 * */
	public static String toId(Class<?> clz, String id) {
		return clz.getName() + "_" + id;
	}
	
	/***
	 * 搜索唯一ID
	 * */
	public static String search(Class<?> clz, String id){
		return clz.getName() + "_" + id ;
	}

}
