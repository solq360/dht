package org.solq.dht.test.db.redis.convertinfo.service;

import java.util.ArrayList;
import java.util.List;

public abstract class ConvertRankCommonHandle implements IConvertRankHandle {

    @Override
    public List<Long> getRank(int page, int limit) {
	List<Long> ids = getAll();
	List<Long> result = new ArrayList<>(limit);
	final int size = ids.size();
	int start = page * limit;
	int end = Math.min((page + 1) * limit, size);
	for (; start < end; start++) {
	    result.add(ids.get(start));
	}
	return result;
    }
}
