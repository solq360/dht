package org.solq.dht.test.db.redis.convertinfo.service;

import java.util.List;

import org.solq.dht.test.db.redis.convertinfo.model.ConvertRankType;

/**
 * 排行榜玩家信息转换器接口
 * 
 * @author solq
 */
public interface IConvertRankHandle {

    /**
     * 获取相应玩家集合
     */
    public List<Long> getRank(int page, int limit);

    /**
     * 获取全部数据
     */
    public List<Long> getAll();

    /**
     * 获取总数量
     */
    public int getSize();

    /**
     * 处理类型
     */
    public ConvertRankType getType();
}
