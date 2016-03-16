package org.solq.dht.test.other;

import com.lmax.disruptor.EventHandler;

public class TradeTransactionJMSNotifyHandler implements EventHandler<TradeTransaction> {  
  
    @Override  
    public void onEvent(TradeTransaction event, long sequence,  
            boolean endOfBatch) throws Exception {  
        //do send jms message  
    }  
}  
  
  
  
