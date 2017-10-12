package com.wecode.game.service;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wecode.game.bean.Agent;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.Manager;
import com.wecode.game.bean.Member;
import com.wecode.game.bean.ResetPwd;
import com.wecode.game.bean.User;
import com.wecode.game.dao.AgentDao;
import com.wecode.game.dao.MemberDao;
import com.wecode.game.dao.UserDao;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.exception.UserNotExistException;
import com.wecode.game.identified.Role;
import com.wecode.game.util.MD5;

@Service("managerService")
public class ManageService
{
	@Resource(name = "userDao")
	private UserDao userDao;
	@Resource(name = "agentDao")
	private AgentDao agentDao;
	@Resource(name = "memberDao")
	private MemberDao memberDao;

	public ListResult<User> getManagerListOfUser(Integer role, Integer num, Integer page)
			throws ResultToBeanException, PermissionNotEnoughException
	{
		if ( role != Role.MANAGER && role != Role.QRMANAGER )
		{
			throw new PermissionNotEnoughException();
		}
		User userCondition = new User();
		userCondition.role = role;
		List<User> userList = userDao.getUserList(userCondition, num, page);
		Long count = userDao.computeUserList(userCondition);
		return new ListResult<User>(count, userList);
	}

	public boolean addManager(Manager manager) throws PermissionNotEnoughException
	{
		manager.uuid = UUID.randomUUID().toString();
		manager.createdTime = System.currentTimeMillis();
		if ( manager.role != Role.MANAGER && manager.role != Role.QRMANAGER )
		{
			throw new PermissionNotEnoughException();
		}
		manager.password = MD5.GetMD5Code(manager.password);

		if (manager.locked == null)
		{
			manager.locked = false;
		}
		if (manager.state == null)
		{
			manager.state = 0;
		}
		return userDao.addUser(manager);
	}

	public boolean modifyManager(Manager manager)
	{
		Manager managerNew = new Manager(manager.uuid);
		managerNew.phone = manager.phone;
		managerNew.email = manager.email;
		managerNew.role = manager.role;

		return userDao.updateUser(managerNew);
	}

	public boolean deleteManager(String uuid)
	{
		return userDao.deleteUser(uuid);
	}

	public User getManagerByUuid(String uuid) throws ResultToBeanException
	{
		User manager = userDao.getUserByUuid(uuid);
		manager.password = null;
		return manager;
	}

	public boolean resetLoginPwdForUser(ResetPwd resetPwd) throws Exception
	{
		if ( resetPwd.currentRole >= Role.AGENT)
		{
			throw new PermissionNotEnoughException();
		}
		User user = userDao.getUserByUsername(resetPwd.username);
		if ( user == null )
		{
			throw new UserNotExistException();
		}
		if ( resetPwd.currentRole >= user.role )
		{
			throw new PermissionNotEnoughException();		
		}
		User userNew = new User(user.uuid);
		userNew.password = MD5.GetMD5Code(resetPwd.newPassword);
		return userDao.updateUser(userNew);
	}
	
	public boolean resetMoneyPwdForUser(ResetPwd resetPwd) throws Exception
	{
		if ( resetPwd.currentRole >= Role.AGENT)
		{
			throw new PermissionNotEnoughException();
		}
		User user = userDao.getUserByUsername(resetPwd.username);
		if ( user == null )
		{
			throw new UserNotExistException();
		}
		if ( user.role == Role.AGENT)
		{
			Agent agentNew = new Agent(user.uuid);
			agentNew.moneyPwd = resetPwd.newPassword;
			return agentDao.updateAgent(agentNew);
		}
		if ( user.role == Role.MEMBER)
		{
			Member memberNew = new Member(user.uuid);
			memberNew.moneyPwd = resetPwd.newPassword;
			return memberDao.updateMember(memberNew);
		}
		return false;
	}
}
