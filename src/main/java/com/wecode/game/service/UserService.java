package com.wecode.game.service;


import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wecode.game.bean.Agent;
import com.wecode.game.bean.Login;
import com.wecode.game.bean.PasswordModify;
import com.wecode.game.bean.ResetPwd;
import com.wecode.game.bean.User;
import com.wecode.game.dao.UserDao;
import com.wecode.game.exception.PasswordIncorrectException;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.PwdErrorThanFiveException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.exception.UnknownException;
import com.wecode.game.exception.UserIsLockedException;
import com.wecode.game.exception.UserIsStoppedException;
import com.wecode.game.exception.UsernameNotExistException;
import com.wecode.game.identified.Role;
import com.wecode.game.identified.State;
import com.wecode.game.util.MD5;



@Service("userService")
public class UserService
{
  
	@Resource(name = "userDao")
    private UserDao userDao;
	
	
	public User login(Login login) throws Exception
	{
		User user = userDao.getUserByUsername(login.username);
		login.password = MD5.GetMD5Code(login.password);
		if ( user == null )
		{
			throw new UsernameNotExistException();
		}
		if ( user.role >= Role.MEMBER )
		{
			throw new PermissionNotEnoughException();
		}
		if ( user.locked )
		{
			throw new UserIsLockedException();
		}
		if ( user.state == State.stop )
		{
			throw new UserIsStoppedException();
		}
		if ( !user.password.equals(login.password) )
		{
			User userNew = new User(user.uuid);
			userNew.pwdError = user.pwdError + 1; 
			System.out.println(userNew.pwdError);
			if ( userNew.pwdError >= 5 )
			{
				User userNew2 = new User(user.uuid);
				userNew2.locked = true;
				if ( userDao.updateUser(userNew2) )
				{
					throw new UnknownException();
				}
				throw new PwdErrorThanFiveException();
			}
			if ( userDao.updateUser(userNew) )
			{
				throw new UnknownException();
			}
			throw new PasswordIncorrectException();
		}
		user.password = null;
		User userNew = new User(user.uuid);
		userNew.authKey = UUID.randomUUID().toString();
		userNew.lastTime = System.currentTimeMillis();
		if (!userDao.updateUser(userNew) )
		{
			throw new UnknownException();
		}
		return user;
	}
	
	public boolean modifyInfo(User user)
	{
		User userNew = new User(user.uuid);
		userNew.email = user.email;
		userNew.phone = user.phone;
		return userDao.updateUser(userNew);
	}


	public boolean resetPwd(ResetPwd resetPwd) throws PasswordIncorrectException, ResultToBeanException
	{
		User user = userDao.getUserByUuid(resetPwd.uuid);
		String pwd = MD5.GetMD5Code(resetPwd.password);
		if ( pwd.equals(user.password) )
		{
			user.password = MD5.GetMD5Code(resetPwd.newPassword);
			User userNew = new User(user.uuid);
			userNew.password = user.password;
			if ( userDao.updateUser(userNew) )
			{
				return true;
			}
			return false;
		}
		else
		{
			throw new PasswordIncorrectException();
		}
	}

	public boolean modifyState(User user)
	{
		User userNew = new User(user.uuid);
		userNew.state = user.state;
		if ( userDao.updateUser(userNew) )
		{
			return true;
		}
		return false;
	}

	public boolean modifyLock(String uuid) throws ResultToBeanException
	{
		User user = userDao.getUserByUuid(uuid);
		User userNew = new User(uuid);
		userNew.locked = !user.locked;
		if ( userDao.updateUser(userNew) )
		{
			return true;
		}
		return false;
	}
	
	public boolean checkUsername(String username) throws Exception
	{
		User user = userDao.getUserByUsername(username);
		if ( user == null )
		{
			return false;
		}
		return true;
	}

	public User getUserByUuid(String uuid) 
	{
		return userDao.getUserByUuid(uuid);
	}

	public boolean modifyPassword(PasswordModify password) throws PasswordIncorrectException {
		User user = userDao.getUserByUuid(password.userId);
		if (!MD5.GetMD5Code(password.prePassword).equals(user.password)) {
			throw new PasswordIncorrectException();
		}
		User userNew = new Agent(user.uuid);
		userNew.password = MD5.GetMD5Code(password.newPassword);
		return userDao.updateUser(userNew);
	}

	public User getUserByUsername(String username) 
	{
		User user = userDao.getUserByUsername(username);
		if ( user == null)
		{
			return null;
		}
		User userNew = new User(user.uuid);
		userNew.username = user.username;
		userNew.phone = user.phone;
		userNew.email = user.email;
		userNew.name = user.name;
		return userNew;
	}
}
