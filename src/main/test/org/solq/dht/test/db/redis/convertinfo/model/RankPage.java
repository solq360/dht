package org.solq.dht.test.db.redis.convertinfo.model;

import java.util.List;
import java.util.Map;

/**
 * 排行榜模型
 * @author solq
 * */
public class RankPage {
    
    /** 查询排行榜类型**/
    private ConvertRankType type;
    /** 当前页数 **/
    private int curr;
    /** 每页数量 **/
    private int limit;
    /** 总数据量 **/
    private int size;

    /** 查询数据 **/
    private List<Map<Long, Map<ConvertPlayerType, Object>>> data;

    public static RankPage of(ConvertRankType type, List<Map<Long, Map<ConvertPlayerType, Object>>> data, int curr, int limit, int size) {
	RankPage result = new RankPage();
	result.type= type;
	result.curr= curr;
	result.limit= limit;
	result.size= size;
	result.data= data; 	
	return result;
    }
    // getter
    public int getCurr() {
	return curr;
    }

    public int getLimit() {
	return limit;
    }

    public int getSize() {
	return size;
    }

    public List<Map<Long, Map<ConvertPlayerType, Object>>> getData() {
	return data;
    }
    public ConvertRankType getType() {
        return type;
    }



}
