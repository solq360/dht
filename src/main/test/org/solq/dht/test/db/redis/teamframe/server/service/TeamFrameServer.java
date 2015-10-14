package org.solq.dht.test.db.redis.teamframe.server.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.solq.dht.db.redis.service.RedisIdHelper;
import org.solq.dht.test.db.redis.teamframe.common.config.TeamFrameConfig;
import org.solq.dht.test.db.redis.teamframe.common.model.TeamFrame;
import org.solq.dht.test.db.redis.teamframe.common.model.TeamMember;
import org.solq.dht.test.db.redis.teamframe.common.model.TeamState;
import org.solq.dht.test.db.redis.teamframe.common.redis.TeamFrameRedis;
import org.solq.dht.test.db.redis.teamframe.common.redis.TeamMemberRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***
 * 团队框架 server 回收队伍
 * 
 * @author solq
 * 
 */
@Service
public class TeamFrameServer implements ITeamFrameServer {

    @Autowired
    private TeamFrameRedis teamFrameRedis;

    @Autowired
    private TeamMemberRedis teamMemberRedis;

    @PostConstruct
    private void postConstruct() {
	Timer timer = new Timer(true);
	timer.scheduleAtFixedRate(new TimerTask() {

	    @Override
	    public void run() {
		scan();
	    }
	}, 1000 * 10, 1000 * 60 * 15);
    }

    @Override
    public void scan() {
	final String pattern = RedisIdHelper.search(TeamMember.class, "*");
	List<TeamMember> datas = teamMemberRedis.query(pattern);
	// 离队处理数据
	List<TeamMember> exitData = new LinkedList<TeamMember>();
	// 离线处理数据
	List<TeamMember> offLineData = new LinkedList<TeamMember>();
	// 回收队伍处理数据
	Set<String> gcTeamFrame = new HashSet<String>();

	for (TeamMember data : datas) {
	    if (data.checkTimeOut(TeamFrameConfig.EXIT_TIME)) {
		exitData.add(data);
		continue;
	    }
	    if (!data.checkState(TeamState.OFF_LINE) && data.checkTimeOut(TeamFrameConfig.OFF_LINE_TIME)) {
		offLineData.add(data);
	    }
	}

	for (TeamMember teamMember : offLineData) {
	    teamMemberRedis.changeState(teamMember.toId(), TeamState.OFF_LINE, null);
	}

	for (TeamMember teamMember : exitData) {
	    boolean isGC = exit(teamMember);
	    if (isGC) {
		gcTeamFrame.add(teamMember.getTeamId());
	    }
	}
	for (String key : gcTeamFrame) {
	    GC(key);
	}
    }

    @Override
    public void GC(String id) {
	teamFrameRedis.remove(id);
	// TODO event
    }

    @Override
    public boolean exit(TeamMember teamMember) {
	teamMemberRedis.remove(teamMember.toId());
	final String teamId = RedisIdHelper.search(TeamFrame.class, teamMember.getTeamId());
	final Long player = teamMember.getId();
	return teamFrameRedis.exit(teamId, player);
    }

}
