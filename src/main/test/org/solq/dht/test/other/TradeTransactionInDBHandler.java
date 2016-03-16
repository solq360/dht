package org.solq.dht.test.other;

import java.util.concurrent.atomic.AtomicInteger;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public class TradeTransactionInDBHandler implements EventHandler<TradeTransaction>, WorkHandler<TradeTransaction> {

    private AtomicInteger ai = new AtomicInteger();
    private long start;

    @Override
    public void onEvent(TradeTransaction event, long sequence, boolean endOfBatch) throws Exception {
	this.onEvent(event);
    }

    @Override
    public void onEvent(TradeTransaction event) throws Exception {
	int value = ai.incrementAndGet();
	if (value == 1) {
	    start = System.currentTimeMillis();
	}
	if (event.isEnd()) {
	    System.out.println(System.currentTimeMillis() - start);
	}
    }
}
