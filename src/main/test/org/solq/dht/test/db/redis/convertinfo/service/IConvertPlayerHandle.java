package org.solq.dht.test.db.redis.convertinfo.service;

import org.solq.dht.test.db.redis.convertinfo.model.ConvertPlayerType;

/**
 * 玩家信息转换器接口
 * 
 * @author solq
 */
public interface IConvertPlayerHandle {

    /**
     * 转换处理
     * */
    public <T> T convert(long playerId);
    /**
     * 处理类型
     * */
    public ConvertPlayerType getType();
}
