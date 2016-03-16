package org.solq.dht.test.other;

//DEMO中使用的 消息全假定是一条交易  
public class TradeTransaction {
    private String id;// 交易ID
    private double price;// 交易金额

    private boolean end;
    public TradeTransaction() {
    }

    public TradeTransaction(String id, double price, boolean end) {
 	this.id = id;
	this.price = price;
	this.end=end;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public double getPrice() {
	return price;
    }

    public void setPrice(double price) {
	this.price = price;
    }
}
