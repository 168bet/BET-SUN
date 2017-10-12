package com.wecode.game.service;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wecode.game.bean.Child;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.User;
import com.wecode.game.dao.AgentDao;
import com.wecode.game.dao.ChildDao;
import com.wecode.game.dao.UserDao;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.identified.Role;
import com.wecode.game.identified.State;
import com.wecode.game.util.MD5;

@Service("childService")
public class ChildService
{	
	@Resource(name = "childDao")
    private ChildDao childDao;
	@Resource(name = "userDao")
	private UserDao userDao;
	@Resource(name = "agentDao")
	private AgentDao agentDao;
	
	
	public Child getChildByUuid(String uuid) throws ResultToBeanException
	{
		Child child = childDao.getChildByUuid(uuid);	
		return child;
	}
	
	public ListResult<Child> getChildListOfUser(String uuid, Integer num, Integer page) throws PermissionNotEnoughException, ResultToBeanException
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
		List<Child> childList = childDao.getChildListByUser(uuid, num, page);
		Long count = childDao.countChildsByUser(uuid);
		return new ListResult<Child>(count, childList);
	}
	
	public boolean addChild(Child child) throws Exception
	{
		child.uuid = UUID.randomUUID().toString();
		child.createdTime = System.currentTimeMillis();
		child.role = Role.CHILD;
		child.password = MD5.GetMD5Code(child.password);
		child.locked = false;
		child.state = State.start;
		
		if ( child.pwdError == null )
		{
			child.pwdError = 0;
		}
		
		if ( !userDao.addUser(child) )
		{
			return false;
		}
			
		if ( !childDao.addChild(child) )
		{
			return false;
		}
		
		child.password = null;
		return true;
	}

	public boolean modifyChild(Child child)
	{
		Child childNew = new Child(child.uuid);
		childNew.phone = child.phone;
		childNew.email = child.email;
		if ( !userDao.updateUser(childNew) )
		{
			return false;
		}
		return childDao.updateChild(childNew);
	}
	
	public boolean delete(String uuid)
	{
		return childDao.deleteChild(uuid);
	}
}
