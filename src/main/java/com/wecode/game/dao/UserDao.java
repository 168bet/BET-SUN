package com.wecode.game.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wecode.game.bean.User;
import com.wecode.game.exception.IntConvertBoolException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.util.MapConvertBeanUtil;
import com.wecode.game.util.SpringJdbc;

@Repository("userDao")
public class UserDao
{
	@Resource
	private SpringJdbc jdbcBase;
	private static Logger log = Logger.getLogger(UserDao.class);

	//根据用户名
	public User getUserByUsername(String username) 
	{
		String sql = "select * from user where username like ?";
		Object[] params = new Object[]{username};
		int[] types = new int[]{Types.VARCHAR};
		List<Map<String,Object>> result = null;
		
		result = jdbcBase.query(sql, params, types);
		if ( result.size() != 1 )
		{
			return null;
		}
		User user;
		try
		{
			user = MapConvertBeanUtil.resultToBean(result.get(0), User.class);
		} 
		catch (IllegalArgumentException | InstantiationException | IllegalAccessException | IntConvertBoolException e)
		{
			log.error("UserDao-getUserByUsername: change result to bean fail. Exception message: " + e.getMessage());
			return null;
		}
		return user;
	}
	
	public User getUserByUuid(String uuid) 
	{
		String sql = "select * from user where uuid like ?";
		Object[] params = new Object[]{uuid};
		int[] types = new int[]{Types.VARCHAR};
		List<Map<String,Object>> result = null;
		
		result = jdbcBase.query(sql, params, types);
		if ( result.size() != 1 )
		{
			return null;
		}
		User user;
		try
		{
			user = MapConvertBeanUtil.resultToBean(result.get(0), User.class);
		} 
		catch (IllegalArgumentException | InstantiationException | IllegalAccessException | IntConvertBoolException e)
		{
			log.error("UserDao-getUserByUuid: change result to bean fail. Exception message: " + e.getMessage());
			return null;
		}
		return user;
	}
	
	public List<User> getUserList(User userCondition, Integer num, Integer page)  throws ResultToBeanException
	{
		String sql = "select * from user where 1 = 1 ";
		List<Object> paramsList = new ArrayList<Object>();
		List<Integer> typesList = new ArrayList<Integer>();
		
		sql = createSelectSql(sql, paramsList, typesList, userCondition);
		Object[] params = paramsList.toArray();
		int[] types = new int[typesList.size()];
		for ( int i = 0; i < typesList.size(); i++ )
		{
			types[i] = typesList.get(i).intValue();
		}
		
		List<Map<String, Object>> result = null;
		
		if ( num != null && page != null )
		{
			result = jdbcBase.query(sql, params, types, page, num);
		}
		else
		{
			result = jdbcBase.query(sql, params, types);
		}
		
		if ( result == null || result.size() == 0 )
		{
			return null;
		}
		
		List<User> userList = new ArrayList<User>();
		for ( Map<String, Object> object : result )
		{
			try
			{
				User user = (User)MapConvertBeanUtil.resultToBean(object, User.class);
				user.password = null;
				userList.add(user);
			} 
			catch (IllegalArgumentException | InstantiationException
					| IllegalAccessException | IntConvertBoolException e)
			{
				log.error("UserDao-getUserList: change result to bean fail. Exception message: " + e.getMessage());
				continue;
			}
		}
		return userList;
	}

	public Long computeUserList(User userCondition)
	{
		String sql = "select count(*) from user where 1 = 1";
		List<Object> paramsList = new ArrayList<Object>();
		List<Integer> typesList = new ArrayList<Integer>();
		
		sql = createSelectSql(sql, paramsList, typesList, userCondition);
		Object[] params = paramsList.toArray();
		int[] types = new int[typesList.size()];
		for ( int i = 0; i < typesList.size(); i++ )
		{
			types[i] = typesList.get(i).intValue();
		}
		
		List<Map<String, Object>> result = null;
		result = jdbcBase.query(sql, params, types);

		if (result == null || result.size() == 0)
		{
			return null;
		}

		return (Long) result.get(0).get("count(*)");
	}

	public boolean addUser(User user)
	{
		String sql = "insert into user (uuid, username, password, locked, state, phone, email, role, createdTime, name, pwdError) values(?,?,?,?,?,?,?,?,?,?,?) ";
		Object[] params = new Object[]{
				user.uuid, user.username, user.password, user.locked, user.state, user.phone, user.email, user.role, user.createdTime, user.name, user.pwdError
		};
		int optResult = jdbcBase.insert(sql, params);
		if ( optResult < 0 )
		{
			log.error("UserDao-addUser: database opertion fail");
			return false;
		}
		return true;
	}

	public boolean updateUser(User user)
	{
		String sql = "update user set";
		List<Object> paramsList = new ArrayList<Object>();
		
		sql = createUpdateSql(sql, paramsList, user);
		if ( sql != null)
		{
			Object[] params = paramsList.toArray();
			System.out.println(sql);
			int optResult = jdbcBase.update(sql, params);
			
			if ( optResult < 0 )
			{
				log.error("UserDao-updateUser: update user error");
				return false;
			}
			return true;
		}
		return false;
	}

	private String createUpdateSql(String sql, List<Object> paramsList, User user)
	{
		if ( user.username != null )
		{
			sql = sql + " username = ?,";
			paramsList.add(user.username);
		}
		if ( user.password != null )
		{
			sql = sql + " password = ?,";
			paramsList.add(user.password);
		}
		if ( user.authKey != null )
		{
			sql = sql + " authKey = ?,";
			paramsList.add(user.authKey);
		}
		if ( user.state != null )
		{
			sql = sql + " state = ?,";
			paramsList.add(user.state);
		}
		if ( user.locked != null )
		{
			sql = sql + " locked = ?,";
			paramsList.add(user.locked);
		}
		if ( user.phone != null )
		{
			sql = sql + " phone = ?,";
			paramsList.add(user.phone);
		}
		if ( user.email != null )
		{
			sql = sql + " email = ?,";
			paramsList.add(user.email);
		}
		if ( user.role != null )
		{
			sql = sql + " role = ?,";
			paramsList.add(user.role);
		}
		if ( user.createdTime != null )
		{
			sql = sql + " createdTime = ?,";
			paramsList.add(user.createdTime);
		}
		if ( user.lastTime != null )
		{
			sql = sql + " lastTime = ?,";
			paramsList.add(user.lastTime);
		}
		if ( user.pwdError != null )
		{
			sql = sql + " pwdError = ?,";
		}
		int length = sql.length();
		if ( sql.charAt(length-1) != ',' )
		{
			return null;
		}
		sql = sql.substring(0,	sql.length() - 1);
		sql = sql + " where uuid = ?";
		paramsList.add(user.uuid);
		return sql;
	}
	
	private String createSelectSql(String sql, List<Object> paramsList,
			List<Integer> typesList, User userContition)
	{
		if ( userContition.uuid != null )
		{
			sql = sql + " and uuid = ?";
			paramsList.add(userContition.uuid);
			typesList.add(Types.VARCHAR);
		}
		if ( userContition.username != null )
		{
			sql = sql + " and username = ?";
			paramsList.add(userContition.username);
			typesList.add(Types.VARCHAR);
		}
		if ( userContition.state != null )
		{
			sql = sql + " and state = ?";
			paramsList.add(userContition.state);
			typesList.add(Types.INTEGER);
		}
		if ( userContition.locked != null )
		{
			sql = sql + " and locked = ?";
			paramsList.add(userContition.locked);
			typesList.add(Types.INTEGER);
		}
		if ( userContition.phone != null )
		{
			sql = sql + " and phone = ?";
			paramsList.add(userContition.phone);
			typesList.add(Types.VARCHAR);
		}
		if ( userContition.email != null )
		{
			sql = sql + " and email = ?";
			paramsList.add(userContition.email);
			typesList.add(Types.VARCHAR);
		}
		if ( userContition.role != null )
		{
			sql = sql + " and role = ?";
			paramsList.add(userContition.role);
			typesList.add(Types.INTEGER);
		}
		if ( userContition.createdTime != null )
		{
			sql = sql + " and createdTime = ?";
			paramsList.add(userContition.createdTime);
			typesList.add(Types.BIGINT);
		}
		if ( userContition.lastTime != null )
		{
			sql = sql + " and lastTime = ?";
			paramsList.add(userContition.lastTime);
			typesList.add(Types.BIGINT);
		}
		return sql;
	}

	public boolean deleteUser(String uuid)
	{
		String sql = "delete from user where uuid = ?";
		Object[] params = new Object[]
		{ uuid };

		int optResult = jdbcBase.update(sql, params);
		if (optResult < 0)
		{
			log.error("AgentDao-deleteAgent: delete user error");
			return false;
		}
		return true;
	}
}
