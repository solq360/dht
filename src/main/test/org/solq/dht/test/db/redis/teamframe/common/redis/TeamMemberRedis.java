package org.solq.dht.test.db.redis.teamframe.common.redis;

import java.util.Date;

import org.solq.dht.db.redis.model.LockCallBack;
import org.solq.dht.db.redis.service.RedisDao;
import org.solq.dht.test.db.redis.teamframe.common.model.TeamMember;
import org.solq.dht.test.db.redis.teamframe.common.model.TeamState;

/***
 * 
 * @author solq
 */
public class TeamMemberRedis extends RedisDao<TeamMember> {

    /***
     * 更改玩家状态
     * 
     * @return true 代表更改成功
     */
    public boolean changeState(String key, TeamState state, Date update) {
	return lock(key, new LockCallBack<Boolean>() {
	    @Override
	    public Boolean exec(String key) {
		TeamMember entity = findOne(key);
		if (entity == null) {
		    return false;
		}
		entity.checkState(state);
		if (update != null) {
		    entity.setLastRefresh(update);
		}
		saveOrUpdate(entity);
		return true;
	    }
	});
    }
}
