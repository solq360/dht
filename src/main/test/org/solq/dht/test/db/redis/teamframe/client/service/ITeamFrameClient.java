package org.solq.dht.test.db.redis.teamframe.client.service;

import org.solq.dht.test.db.redis.teamframe.common.model.TeamFrame;

/***
 * 团队框架 client <br>
 * 定时刷新状态 <br>
 * 操作业务<br>
 * 
 * @author solq
 * 
 */
public interface ITeamFrameClient {
    /** 加入队伍 **/
    public boolean join(TeamFrame teamFrame, long player);

    /** 退出队伍 **/
    public boolean exit(TeamFrame teamFrame, long player);

    /** 转让队长 **/
    public boolean transfer(TeamFrame teamFrame, long from, long to);

    /** 更新个人信息 **/
    public void changePlayerInfo(TeamFrame teamFrame, long player);

}
