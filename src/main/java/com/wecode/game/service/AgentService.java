package com.wecode.game.service;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wecode.game.bean.Agent;
import com.wecode.game.bean.Child;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.Member;
import com.wecode.game.bean.PasswordModify;
import com.wecode.game.bean.User;
import com.wecode.game.dao.AgentDao;
import com.wecode.game.dao.ChildDao;
import com.wecode.game.dao.MemberDao;
import com.wecode.game.dao.UserDao;
import com.wecode.game.exception.MoneyPasswordIncorrectException;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.exception.UserNotExistException;
import com.wecode.game.identified.Role;
import com.wecode.game.util.MD5;

@Service("agentService")
public class AgentService
{	
	@Resource(name = "agentDao")
    private AgentDao agentDao;
	@Resource(name = "memberDao")
    private MemberDao memberDao;
	@Resource(name = "childDao")
    private ChildDao childDao;
	@Resource(name = "userDao")
	private UserDao userDao;
	
	
	public Agent getAgentByUuid(String uuid) throws ResultToBeanException
	{
		Agent agent = agentDao.getAgentByUuid(uuid);	
		agent.agentNum = agentDao.computeAgentNumOfUuid(agent.uuid);
		agent.memberNum = memberDao.computeMemberNumOfUuid(agent.uuid);
		agent.childNum = childDao.computeChildNumOfUuid(agent.uuid);
		return agent;
	}
	
	public ListResult<Agent> getAgentListOfUser(String uuid, Integer num, Integer page) throws Exception
	{
		User user = userDao.getUserByUuid(uuid);
		if ( user == null )
		{
			throw new UserNotExistException();
		}
		if ( user.role == Role.SUPER || user.role == Role.MANAGER || user.role == Role.QRMANAGER)
		{
			uuid = "manager";
		}
		else if ( user.role == Role.MEMBER )
		{
			throw new PermissionNotEnoughException();
		}
		List<Agent> agentList = agentDao.getAgentListByUser(uuid, num, page);
		if ( agentList == null )
		{
			return new ListResult<Agent>(0L, agentList);
		}
		for (Agent agent : agentList)
		{
			agent.agentNum = agentDao.computeAgentNumOfUuid(agent.uuid);
			agent.memberNum = memberDao.computeMemberNumOfUuid(agent.uuid);
			agent.childNum = childDao.computeChildNumOfUuid(agent.uuid);
		}
		Long count = agentDao.countAgentsByUser(uuid);
		return new ListResult<Agent>(count, agentList);
	}
	
	public boolean addAgent(Agent agent) throws Exception
	{
		agent.uuid = UUID.randomUUID().toString();
		agent.createdTime = System.currentTimeMillis();
		agent.role = Role.AGENT;
		agent.password = MD5.GetMD5Code(agent.password);
		agent.moneyPwd = MD5.GetMD5Code(agent.moneyPwd);
		
		if ( agent.point == null )
		{
			agent.point = 0.0;
		}
		if ( agent.onlyRead == null )
		{
			agent.onlyRead = false;
		}
		if ( agent.locked == null )
		{
			agent.locked = false;
		}
		if ( agent.state == null )
		{
			agent.state = 0;
		}
		if ( agent.parentId == null )
		{
			agent.parentId = "manager";
		}
		else
		{
			User user = userDao.getUserByUuid(agent.parentId);
			if ( user.role < Role.AGENT )
			{
				agent.parentId = "manager";
			}
		}
		if ( agent.pwdError == null )
		{
			agent.pwdError = 0;
		}
		if ( !userDao.addUser(agent) )
		{
			return false;
		}
			
		if ( !agentDao.addAgent(agent) )
		{
			return false;
		}
		agent.password = null;
		agent.moneyPwd = null;
		return true;
	}

	public boolean modifyAgent(Agent agent)
	{
		Agent agentNew = new Agent(agent.uuid);
		agentNew.commissionPer = agent.commissionPer;
		agentNew.rebate = agent.rebate;
		agentNew.phone = agent.phone;
		agentNew.email = agent.email;
		if ( !userDao.updateUser(agentNew) )
		{
			return false;
		}
		return agentDao.updateAgent(agentNew);
	}
	
	public boolean delete(String uuid)
	{
		List<Agent> agentList = agentDao.getAgentListByUser(uuid, null, null);
		List<Member> memberList = memberDao.getMemberListByUser(uuid, null, null);
		List<Child> childList = childDao.getChildListByUser(uuid, null, null);
		if ( memberList != null && memberList.size() > 0 )
		{
			for ( Member member : memberList )
			{
				memberDao.deleteMember(member.uuid);
			}
		}
		if ( childList != null && childList.size() > 0 )
		{
			for ( Child child : childList )
			{
				childDao.deleteChild(child.uuid);
			}
		}
		if ( agentList != null && agentList.size() > 0)
		{
			for ( Agent agent : agentList )
			{
				delete(agent.uuid);
			}
		}
		return agentDao.deleteAgent(uuid);
	}

	public boolean modifyAgentOnlyRead(String uuid) throws ResultToBeanException
	{
		Agent agent = agentDao.getAgentByUuid(uuid);
		Agent agentNew = new Agent(uuid);
		agentNew.onlyRead = !agent.onlyRead;
		return agentDao.updateAgent(agentNew);
	}

	public boolean modifyMoneyPassword(PasswordModify password) 
			throws MoneyPasswordIncorrectException {
		Agent agent = agentDao.getAgentByUuid(password.userId);
		if (!MD5.GetMD5Code(password.preMoneyPswd).equals(agent.moneyPwd)) {
			throw new MoneyPasswordIncorrectException();
		}
		Agent agentNew = new Agent(agent.uuid);
		agentNew.moneyPwd = MD5.GetMD5Code(password.newMoneyPswd);
		return agentDao.updateAgent(agentNew);
	}
}
