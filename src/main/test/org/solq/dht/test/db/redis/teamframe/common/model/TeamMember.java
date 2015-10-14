package org.solq.dht.test.db.redis.teamframe.common.model;

import java.util.Date;

import org.solq.dht.db.redis.model.IRedisEntity;
import org.solq.dht.db.redis.service.RedisIdHelper;

/***
 * @author solq
 */
public class TeamMember implements IRedisEntity {

    /***
     * 玩家id
     */
    private long id;

    /***
     * 团队id
     */
    private String teamId;

    private TeamState state = TeamState.NORMAL;

    private Date lastRefresh = new Date();

    public static TeamMember of(long player, String teamId) {
	TeamMember result = new TeamMember();
	result.id = player;
	result.teamId = teamId;
	return result;
    }
    
    ///////////////////////业务方法////////////////////////
    @Override
    public String toId() {
	return RedisIdHelper.toId(getClass(), String.valueOf(this.id));
    }

    public boolean checkState(TeamState state) {
	return this.state.equals(state);
    }

    public boolean checkTimeOut(int time) {
	return (System.currentTimeMillis() - this.lastRefresh.getTime()) >= time;
    }
    
    // get set

    public TeamState getState() {
	return state;
    }

    public long getId() {
	return id;
    }

    void setId(long id) {
	this.id = id;
    }

    void setState(TeamState state) {
	this.state = state;
    }

    public Date getLastRefresh() {
	return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
	this.lastRefresh = lastRefresh;
    }

    public String getTeamId() {
	return teamId;
    }

    public void setTeamId(String teamId) {
	this.teamId = teamId;
    }


}
