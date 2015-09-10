package org.solq.dht.test.db.shards;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.shards.ShardId;
import org.hibernate.shards.strategy.resolution.ShardResolutionStrategy;
import org.hibernate.shards.strategy.selection.ShardResolutionStrategyData;

public class MyShardResolutionStrategy implements ShardResolutionStrategy {

	private int count = 1;

	private List<ShardId> _shardIds;

	public MyShardResolutionStrategy(List<ShardId> shardIds) {
		this._shardIds = shardIds;
		this.count = shardIds.size();

		System.out.println(" shardIds len : " + shardIds.size());
	}

	public List selectShardIdsFromShardResolutionStrategyData(ShardResolutionStrategyData arg0) {
		List ids = new ArrayList();
		String id = (String) arg0.getId();

		System.out.println(" id : " + id);

		return this._shardIds;
	}
}