package org.solq.dht.test.db.shards;

import java.util.List;

import org.hibernate.shards.ShardId;
import org.hibernate.shards.strategy.selection.ShardSelectionStrategy;
import org.solq.dht.test.db.shards.model.ContactEntity;

public class MyShardSelectionStrategy implements ShardSelectionStrategy {
	private int count = 1;

	private List<ShardId> _shardIds;

	public MyShardSelectionStrategy(List<ShardId> shardIds) {
		this._shardIds = shardIds;
		this.count= shardIds.size();
		
        System.out.println(" shardIds len : " + shardIds.size());

	}

	public ShardId selectShardIdForNewObject(Object obj) {
		if (obj instanceof ContactEntity) {
			String id = ((ContactEntity) obj).getId();
			
			if (id == null || id.isEmpty())
				return this._shardIds.get(0);
			Integer i = new Integer(id.substring(0, 1)); 
			int index = count-1;
			if(index!=0){
				index = i%count;
			}
			return this._shardIds.get(index);
		}
		// for non-shardable entities we just use shard0
		return this._shardIds.get(0);
	}
}