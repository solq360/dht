package org.solq.dht.test.db.redis.convertinfo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.solq.dht.test.db.redis.convertinfo.model.ConvertPlayerType;
import org.solq.dht.test.db.redis.convertinfo.model.ConvertRankType;
import org.solq.dht.test.db.redis.convertinfo.model.RankPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * 信息转换器实现
 * 
 * @author solq
 */
@Service
public class ConvertInfo implements IConvertInfo, ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ApplicationContext applicationContext;
    private Map<ConvertPlayerType, IConvertPlayerHandle> mapConvertPlayerHandle = new HashMap<ConvertPlayerType, IConvertPlayerHandle>();
    private Map<ConvertRankType, IConvertRankHandle> mapConvertRankHandle = new HashMap<ConvertRankType, IConvertRankHandle>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
	Map<String, IConvertPlayerHandle> beans = applicationContext.getBeansOfType(IConvertPlayerHandle.class);
	for (IConvertPlayerHandle b : beans.values()) {
	    if (mapConvertPlayerHandle.containsKey(b.getType())) {
		throw new RuntimeException("register ConvertPlayerType key Repeat : " + b.getType());
	    }
	    mapConvertPlayerHandle.put(b.getType(), b);
	}

	Map<String, IConvertRankHandle> rankbeans = applicationContext.getBeansOfType(IConvertRankHandle.class);
	for (IConvertRankHandle b : rankbeans.values()) {
	    if (mapConvertRankHandle.containsKey(b.getType())) {
		throw new RuntimeException("register ConvertRankHandle key Repeat : " + b.getType());
	    }
	    mapConvertRankHandle.put(b.getType(), b);
	}
    }

    @Override
    public <T> T convertPlayerInfo(long playerId, ConvertPlayerType type) {
	return getConvertPlayerHandle(type).convert(playerId);
    }

    @Override
    public Map<ConvertPlayerType, Object> convertPlayerInfo(long playerId, Collection<ConvertPlayerType> convertTypes) {
	Map<ConvertPlayerType, Object> result = new HashMap<>(convertTypes.size());
	for (ConvertPlayerType type : convertTypes) {
	    result.put(type, convertPlayerInfo(playerId, type));
	}
	return result;
    }

    @Override
    public Map<Long, Map<ConvertPlayerType, Object>> convertMultiPlayerInfo(Collection<Long> playerIds, Collection<ConvertPlayerType> convertTypes) {
	Map<Long, Map<ConvertPlayerType, Object>> result = new LinkedHashMap<>(playerIds.size());
	for (long playerId : playerIds) {
	    result.put(playerId, convertPlayerInfo(playerId, convertTypes));
	}
	return result;
    }

    @Override
    public RankPage rankPlayerInfo(ConvertRankType type, int page, int limit, Collection<ConvertPlayerType> convertTypes) {

	List<Map<Long, Map<ConvertPlayerType, Object>>> data = new ArrayList<>(limit);
	final IConvertRankHandle handle = getConvertRankHandle(type);
	final Collection<Long> ids = handle.getRank(page, limit);
	final int size =  handle.getSize();
	for (long id : ids) {
	    Map<Long, Map<ConvertPlayerType, Object>> info = new HashMap<>(1);
	    info.put(id, convertPlayerInfo(id, convertTypes));
	    data.add(info);
	}
	return RankPage.of(type,data, page, limit, size);
    }

    private IConvertPlayerHandle getConvertPlayerHandle(ConvertPlayerType type) {
	IConvertPlayerHandle result = mapConvertPlayerHandle.get(type);
	if (result == null) {
	    throw new RuntimeException("ConvertPlayerHandle is null " + type);
	}
	return result;
    }

    private IConvertRankHandle getConvertRankHandle(ConvertRankType type) {
	IConvertRankHandle result = mapConvertRankHandle.get(type);
	if (result == null) {
	    throw new RuntimeException("ConvertRankHandle is null " + type);
	}
	return result;
    }

}
