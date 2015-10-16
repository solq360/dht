package org.solq.dht.db.redis.model;

public interface IRedisElementAction {
    public void exec();
    public String getId();
    public boolean isRetry();
}
