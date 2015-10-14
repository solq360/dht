package org.solq.dht.test.db.redis.teamframe.common.redis;

import org.solq.dht.db.redis.model.LockCallBack;
import org.solq.dht.db.redis.service.RedisDao;
import org.solq.dht.test.db.redis.teamframe.common.model.TeamFrame;

/***
 * 
 * @author solq
 */
public class TeamFrameRedis extends RedisDao<TeamFrame> {

    /***
     * @return true 代表队伍要清理
     * */
    public boolean exit(String teamId, long player) {
	return lock(teamId, new LockCallBack<Boolean>() {
	    @Override
	    public Boolean exec(String key) {
		TeamFrame teamFrame = findOne(key);
		if (teamFrame == null) {
		    return false;
		}
		teamFrame.exit(player);
		saveOrUpdate(teamFrame);
		
		//清理逻辑给上层处理
		return teamFrame.canGC();
	    }
	});
    }
}
