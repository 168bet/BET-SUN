package com.wecode.game.service;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wecode.game.bean.Agent;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.Login;
import com.wecode.game.bean.Member;
import com.wecode.game.bean.PasswordModify;
import com.wecode.game.bean.User;
import com.wecode.game.dao.AgentDao;
import com.wecode.game.dao.MemberDao;
import com.wecode.game.dao.UserDao;
import com.wecode.game.exception.AgentNotExistException;
import com.wecode.game.exception.MoneyPasswordIncorrectException;
import com.wecode.game.exception.PasswordIncorrectException;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.PwdErrorThanFiveException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.exception.UnknownException;
import com.wecode.game.exception.UserIsLockedException;
import com.wecode.game.exception.UserIsNotMemberException;
import com.wecode.game.exception.UserIsStoppedException;
import com.wecode.game.exception.UsernameNotExistException;
import com.wecode.game.identified.Role;
import com.wecode.game.identified.State;
import com.wecode.game.util.MD5;

@Service("memberService")
public class MemberService
{	
	@Resource(name = "memberDao")
    private MemberDao memberDao;
	@Resource(name = "userDao")
	private UserDao userDao;
	@Resource(name = "agentDao")
	private AgentDao agentDao;
	
	public Member login(Login login) throws Exception
	{
		Member member = memberDao.getMemberByUsername(login.username);
		login.password = MD5.GetMD5Code(login.password);
		if ( member == null )
		{
			throw new UsernameNotExistException();
		}
		if ( member.role != Role.MEMBER )
		{
			throw new UserIsNotMemberException();
		}
		if ( member.locked )
		{
			throw new UserIsLockedException();
		}
		if ( member.state == State.stop )
		{
			throw new UserIsStoppedException();
		}
		if ( !member.password.equals(login.password) )
		{
			Member memberNew = new Member(member.uuid);
			memberNew.pwdError = member.pwdError + 1; 
			if ( memberNew.pwdError >= 5 )
			{
				Member memberNew2 = new Member(member.uuid);
				memberNew2.locked = true;
				if ( userDao.updateUser(memberNew2) )
				{
					throw new UnknownException();
				}
				throw new PwdErrorThanFiveException();
			}
			if ( userDao.updateUser(memberNew) )
			{
				throw new UnknownException();
			}
			throw new PasswordIncorrectException();
		}
		member.password = null;
		Member memberNew = new Member(member.uuid);
		memberNew.authKey = UUID.randomUUID().toString();
		memberNew.lastTime = System.currentTimeMillis();
		if (!userDao.updateUser(memberNew) )
		{
			throw new UnknownException();
		}
		return member;
	}
	
	public Member getMemberByUuid(String uuid) throws ResultToBeanException
	{
		Member member = memberDao.getMemberByUuid(uuid);	
		return member;
	}
	
	public ListResult<Member> getMemberListOfUser(String uuid, Integer num, Integer page) throws PermissionNotEnoughException, ResultToBeanException
	{
		User user = userDao.getUserByUuid(uuid);
		if ( user.role == Role.SUPER || user.role == Role.MANAGER || user.role == Role.QRMANAGER)
		{
			uuid = "manager";
		}
		else if ( user.role == Role.MEMBER )
		{
			throw new PermissionNotEnoughException();
		}
		List<Member> memberList = memberDao.getMemberListByUser(uuid, num, page);
		Long count = memberDao.countMembersByUser(uuid);
		return new ListResult<Member>(count, memberList);
	}
	
	public boolean register(Member member) throws Exception 
	{
		member.uuid = UUID.randomUUID().toString();
		member.createdTime = System.currentTimeMillis();
		member.role = Role.MEMBER;
		member.password = MD5.GetMD5Code(member.password);
		member.moneyPwd = MD5.GetMD5Code(member.moneyPwd);
		if ( member.agent == null )
		{
			member.parentId = "manager";
		}
		else
		{
			Agent agent = agentDao.getAgentByUsername(member.agent);
			if ( agent == null )
			{
				throw new AgentNotExistException();
			}
			member.parentId = agent.uuid;
		}
		if ( member.locked == null )
		{
			member.locked = false;
		}
		if ( member.state == null )
		{
			member.state = 0;
		}
		if ( member.point == null )
		{
			member.point = 0.0;
		}
		if ( member.privilegeNum == null )
		{
			member.privilegeNum = 3;
		}
		if ( member.upperLimit == null )
		{
			member.upperLimit = 0;
		}
		if ( member.profit == null )
		{
			member.profit = 0.0;
		}
		if ( member.pwdError == null )
		{
			member.pwdError = 0;
		}
		if ( !userDao.addUser(member) )
		{
			return false;
		}
			
		if ( !memberDao.addMember(member) )
		{
			return false;
		}
	
		member.password = null;
		member.moneyPwd = null;
		return true;
	}
	
	public boolean addMember(Member member) throws Exception
	{
		member.uuid = UUID.randomUUID().toString();
		member.createdTime = System.currentTimeMillis();
		member.role = Role.MEMBER;
		member.password = MD5.GetMD5Code(member.password);
		if ( member.locked == null )
		{
			member.locked = false;
		}
		if ( member.state == null )
		{
			member.state = 0;
		}
		if ( member.locked == null )
		{
			member.locked = false;
		}
		if ( member.point == null )
		{
			member.point = 0.0;
		}
		if ( member.privilegeNum == null )
		{
			member.privilegeNum = 3;
		}
		if ( member.upperLimit == null )
		{
			member.upperLimit = 0;
		}
		if ( member.profit == null )
		{
			member.profit = 0.0;
		}
		if ( member.parentId == null )
		{
			member.parentId = "manager";
		}
		else
		{
			User user = userDao.getUserByUuid(member.parentId);
			if ( user.role < Role.AGENT )
			{
				member.parentId = "manager";
			}
		}
		if ( member.pwdError == null )
		{
			member.pwdError = 0;
		}
		if ( !userDao.addUser(member) )
		{
			return false;
		}
			
		if ( !memberDao.addMember(member) )
		{
			return false;
		}
		member.password = null;
		return true;
	}

	public boolean modifyMember(Member member)
	{
		Member memberNew = new Member(member.uuid);
		memberNew.upperLimit = member.upperLimit;
		memberNew.phone = member.phone;
		memberNew.email = member.email;
		if ( !userDao.updateUser(memberNew) )
		{
			return false;
		}
		return memberDao.updateMember(memberNew);
	}
	
	public boolean delete(String uuid)
	{
		return memberDao.deleteMember(uuid);
	}

	public boolean modifyPassword(PasswordModify password) throws PasswordIncorrectException,
			MoneyPasswordIncorrectException {
		Member member = memberDao.getMemberByUuid(password.userId);
		if (!MD5.GetMD5Code(password.prePassword).equals(member.password)) {
			throw new PasswordIncorrectException();
		}
		if (!MD5.GetMD5Code(password.preMoneyPswd).equals(member.moneyPwd)) {
			throw new MoneyPasswordIncorrectException();
		}
		Member memberNew = new Member(member.uuid);
		memberNew.password = MD5.GetMD5Code(password.newPassword);
		memberNew.moneyPwd = MD5.GetMD5Code(password.newMoneyPswd);
		if (!userDao.updateUser(memberNew)) {
			return false;
		}
		return memberDao.updateMember(memberNew);
	}
}
