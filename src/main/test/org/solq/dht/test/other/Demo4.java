package org.solq.dht.test.other;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Demo4 {
    private static int LOOP = 10000000;// 模拟一千万次交易的发生

    private static BlockingQueue<TradeTransaction> queue = new LinkedBlockingQueue<>();

    private static volatile boolean run = true;

    public static void main(String[] args) throws InterruptedException {
	long beginTime = System.currentTimeMillis();

	Thread read = new Thread(new Runnable() {

	    @Override
	    public void run() {
		while (run) {
		    TradeTransaction trade;
		    try {
			trade = queue.take();
			trade.setPrice(new Random().nextDouble() * 9999);

		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		    //http://xsh5324.iteye.com/blog/2058925?utm_source=tool.lu
		}
	    }
	});
	read.setDaemon(true);
	read.start();

	for (int i = 0; i < LOOP; i++) {
	    TradeTransaction trade = new TradeTransaction();
	    queue.add(trade);
	    if (trade.isEnd()) {
		System.out.println("xxx");
	    }
	}
	while (queue.isEmpty()) {
	    run = false;
	    Thread.sleep(500);
	}
	System.out.println("总耗时:" + (System.currentTimeMillis() - beginTime));
    }
}