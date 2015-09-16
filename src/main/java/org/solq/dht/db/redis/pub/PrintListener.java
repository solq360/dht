package org.solq.dht.db.redis.pub;

import java.util.Date;

//public class PrintListener extends JedisPubSub {
//
//	@Override
//	public void onMessage(String channel, String message) {
//		String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
//		System.out.println("message receive:" + message + ",channel:" + channel + "..." + time);
//
//		if (message.equalsIgnoreCase("quit")) {
//			this.unsubscribe(channel);
//		}
//	}
//
//	public void pub(String channel, String message) {
//		// jedis.publish(channel, message);
//	}
//
//	public void sub(JedisPubSub listener, String channel) {
//		// jedis.subscribe(listener, channel);
//		// 此处将会阻塞，在client代码级别为JedisPubSub在处理消息时，将会“独占”链接
//		// 并且采取了while循环的方式，侦听订阅的消息
//		//
//	}
//}