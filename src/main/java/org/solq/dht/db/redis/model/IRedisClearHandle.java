package org.solq.dht.db.redis.model;

public interface IRedisClearHandle {

    public void exec();
    
    public int getValidDay();
}
