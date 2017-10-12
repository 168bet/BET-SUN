package com.wecode.game.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wecode.game.bean.Agent;
import com.wecode.game.bean.Child;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.Member;
import com.wecode.game.bean.User;
import com.wecode.game.bean.UserLog;
import com.wecode.game.dao.AgentDao;
import com.wecode.game.dao.ChildDao;
import com.wecode.game.dao.MemberDao;
import com.wecode.game.dao.UserDao;
import com.wecode.game.dao.UserLogDao;
import com.wecode.game.identified.Role;

@Service("userLogService")
public class UserLogService {

	@Resource(name = "userLogDao")
	private UserLogDao userLogDao;

	@Resource(name = "userDao")
	private UserDao userDao;

	@Resource(name = "agentDao")
	private AgentDao agentDao;

	@Resource(name = "memberDao")
	private MemberDao memberDao;
	@Resource(name = "childDao")
	private ChildDao childDao;

	public void addLog(UserLog log) throws Exception {
		userLogDao.addLog(log);
	}

	public ListResult<UserLog> getAllLog(String userId, Long start, Long end, Integer num,
			Integer page) {
		User user = userDao.getUserByUuid(userId);
		List<UserLog> logs = null;
		Long count = null;
		if (user.role <= Role.QRMANAGER) {
			count = userLogDao.countManageLog(userId, user.role, start, end);
			logs = userLogDao.getManageLog(userId, user.role, start, end, num, page);
		} else if (user.role == Role.AGENT) {
			List<String> subs = new ArrayList<String>();
			subs.add(userId);
			this.getSubAgentsAndMembers(userId, subs);
			logs = userLogDao.getUsersLogByIds(subs, start, end, num, page);
			count = userLogDao.countUsersLogByIds(subs, start, end);
		} else if (user.role == Role.CHILD) {
			List<String> subs = new ArrayList<String>();
			subs.add(userId);
			this.getSubChildren(userId, subs);
			logs = userLogDao.getUsersLogByIds(subs, start, end, num, page);
			count = userLogDao.countUsersLogByIds(subs, start, end);
		} else if (user.role == Role.MEMBER) {
			List<String> subs = new ArrayList<String>();
			subs.add(userId);
			logs = userLogDao.getUsersLogByIds(subs, start, end, num, page);
			count = userLogDao.countUsersLogByIds(subs, start, end);
		}
		return new ListResult<UserLog>(count, logs);
	}

	public ListResult<UserLog> getUserManageLog(String userId, Long start, Long end, Integer num,
			Integer page) {
		List<String> subs = new ArrayList<String>();
		subs.add(userId);
		List<UserLog> logs = userLogDao.getUsersLogByIds(subs, start, end, num, page);
		Long count = userLogDao.countUsersLogByIds(subs, start, end);
		return new ListResult<UserLog>(count, logs);
	}

	// 获取代理下属所有成员的id，包括下属代理的下属
	private void getSubAgentsAndMembers(String userId, List<String> out) {
		List<Agent> subAgents = agentDao.getAgentListByUser(userId, null, null);
		if (subAgents != null && subAgents.size() > 0) {
			for (Agent agent : subAgents) {
				out.add(agent.uuid);
				getSubAgentsAndMembers(agent.uuid, out);
			}
		}
		List<Member> subMember = memberDao.getMemberListByUser(userId, null, null);
		if (subAgents != null && subAgents.size() > 0) {
			for (Member m : subMember) {
				out.add(m.uuid);
			}
		}
	}

	// 获取子账号所有下属子账号
	private void getSubChildren(String userId, List<String> out) {
		List<Child> subAgents = childDao.getChildListByUser(userId, null, null);
		if (subAgents != null && subAgents.size() > 0) {
			for (Child c : subAgents) {
				out.add(c.uuid);
				getSubAgentsAndMembers(c.uuid, out);
			}
		}
		List<Member> subMember = memberDao.getMemberListByUser(userId, null, null);
		if (subAgents != null && subAgents.size() > 0) {
			for (Member m : subMember) {
				out.add(m.uuid);
			}
		}
	}

	public boolean deleteLog(String id) {
		return userLogDao.deleteLog(id);
	}

	public ListResult<UserLog> getUserLog(String username, Long start, Long end, Integer num,
			Integer page) {
		List<UserLog> logs = userLogDao.getUsersLogByName(username, start, end, num, page);
		Long count = userLogDao.countUsersLogByName(username, start, end);
		return new ListResult<UserLog>(count, logs);
	}
}
