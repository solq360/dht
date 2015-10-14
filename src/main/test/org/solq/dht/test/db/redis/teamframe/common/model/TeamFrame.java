package org.solq.dht.test.db.redis.teamframe.common.model;

import java.util.LinkedHashSet;

import org.solq.dht.db.redis.model.IRedisEntity;
import org.solq.dht.db.redis.service.RedisIdHelper;

/***
 * @author solq
 */
public class TeamFrame implements IRedisEntity {

    /***
     * 唯一id
     */
    private String id;

    /***
     * 团长
     */
    private Long leader;

    /***
     * 成员
     */
    private LinkedHashSet<Long> members = new LinkedHashSet<Long>();

    
    public synchronized Boolean canGC() {
	return members.isEmpty();
    }

    /***
     * @return true 代表变更数据
     */
    public synchronized boolean join(long player) {
	if (members.isEmpty()) {
	    leader = player;
	}
	members.add(player);
	return true;
    }

    /***
     * @return true 代表变更数据
     */
    public synchronized boolean exit(Long player) {
	members.remove(player);
	if (leader == player) {
	    if (members.isEmpty()) {
		leader = null;
	    } else {
		for (Long m : members) {
		    leader = m;
		    return true;
		}
	    }
	}
	return true;
    }

    @Override
    public String toId() {
	return RedisIdHelper.toId(getClass(), id);
    }
    // get set

    public String getId() {
	return id;
    }

    void setId(String id) {
	this.id = id;
    }

    public LinkedHashSet<Long> getMembers() {
	return members;
    }

    void setMembers(LinkedHashSet<Long> members) {
	this.members = members;
    }

    public Long getLeader() {
	return leader;
    }

    void setLeader(Long leader) {
	this.leader = leader;
    }

}
