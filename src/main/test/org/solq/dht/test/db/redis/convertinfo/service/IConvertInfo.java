package org.solq.dht.test.db.redis.convertinfo.service;

import java.util.Collection;
import java.util.Map;

import org.solq.dht.test.db.redis.convertinfo.model.ConvertPlayerType;
import org.solq.dht.test.db.redis.convertinfo.model.ConvertRankType;
import org.solq.dht.test.db.redis.convertinfo.model.RankPage;

/**
 * 信息转换器接口
 * 
 * @author solq
 */
public interface IConvertInfo {

    /**
     * 转换玩家信息
     */
    public <T> T convertPlayerInfo(long playerId, ConvertPlayerType type);

    /**
     * 转换玩家信息
     */
    public Map<ConvertPlayerType, Object> convertPlayerInfo(long playerId, Collection<ConvertPlayerType> convertTypes);

    /**
     * 转换多个玩家信息
     */
    public Map<Long, Map<ConvertPlayerType, Object>> convertMultiPlayerInfo(Collection<Long> playerIds, Collection<ConvertPlayerType> convertTypes);

    /**
     * 转换多个玩家信息-排行榜
     * 
     * @param type
     *            排行榜类型
     * @param page
     *            第几页 从0开始
     * @param limit
     *            每页长度
     * @param convertTypes
     *            需要信息类型
     */
    public RankPage rankPlayerInfo(ConvertRankType type, int page, int limit, Collection<ConvertPlayerType> convertTypes);
}
