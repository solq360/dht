package org.solq.dht.test.db.redis.teamframe.server.service;

import org.solq.dht.test.db.redis.teamframe.common.model.TeamMember;

/***
 * 团队框架 server 回收队伍
 * 
 * @author solq
 * 
 */
public interface ITeamFrameServer {

    /**
     * 扫描队伍 <br>
     * 1分钟没更新标记为离线状态 <br>
     * 2分钟没更新退出处理<br>
     **/
    public void scan();

    /** 回收队伍 **/
    public void GC(String id);

    /**
     * 退出队伍
     * 
     * @return
     **/
    public boolean exit(TeamMember teamMember);
}
