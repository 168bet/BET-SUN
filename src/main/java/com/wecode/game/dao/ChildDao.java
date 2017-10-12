package com.wecode.game.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wecode.game.bean.Child;
import com.wecode.game.exception.IntConvertBoolException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.util.MapConvertBeanUtil;
import com.wecode.game.util.SpringJdbc;

@Repository("childDao")
public class ChildDao
{
	@Resource
	private SpringJdbc jdbcBase;
	private static Logger log = Logger.getLogger(ChildDao.class);
	
	//根据uuid获取代理
	public Child getChildByUuid(String uuid) throws ResultToBeanException
	{
		String sql = "select * from user as u left join child as a on u.uuid like a.uuid where a.uuid = ?";
		Object[] params = new Object[]{uuid};
		int[] types = new int[]{Types.VARCHAR};
		List<Map<String,Object>> result = null;
		
		result = jdbcBase.query(sql, params, types);
		
		if ( result.size() != 1 )
		{
			return null;
		}
		
		Child child;
		try
		{
			child = MapConvertBeanUtil.resultToBean(result.get(0), Child.class);
			child.password = null;
		} 
		catch (IllegalArgumentException | InstantiationException | IllegalAccessException | IntConvertBoolException e)
		{
			log.error("ChildDao-getChildByChildname: change result to bean fail. Exception message: " + e.getMessage());
			throw new ResultToBeanException();
		}
		return child;		
	}
	
	//
	public Long countChildsByUser(String uuid)
	{
		String sql = "select count(*) from user as u inner join child as a on u.uuid like a.uuid where a.parentId = ?";
		Object[] params = new Object[]{uuid};
		int[] types = new int[]{Types.VARCHAR};
		List<Map<String, Object>> result = null;
		result = jdbcBase.query(sql, params, types);
		
		if ( result == null || result.size() == 0 )
		{
			return null;
		}
		
		return (Long)result.get(0).get("count(*)");
	}

	//根据uuid获取子代理列表
	public List<Child> getChildListByUser(String uuid, Integer num, Integer page)
	{
		String sql = "select * from user as u inner join child as a on u.uuid like a.uuid where a.parentId = ?";
		Object[] params = new Object[]{uuid};
		int[] types = new int[]{Types.VARCHAR};
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
		
		List<Child> childList = new ArrayList<Child>();
		for ( Map<String, Object> object : result )
		{
			try
			{
				Child child = (Child)MapConvertBeanUtil.resultToBean(object, Child.class);
				child.password = null;
				childList.add(child);
			} 
			catch (IllegalArgumentException | InstantiationException
					| IllegalAccessException | IntConvertBoolException e)
			{
				log.error("ChildDao-getChildListByUser: change result to bean fail. Exception message: " + e.getMessage());
				continue;
			}
		}
		return childList;
	}
	
	public boolean addChild(Child child)
	{
		String sql = "insert into child (uuid, parentId) "
					 + "values(?,?)";
		Object[] params = new Object[]{
				child.uuid, child.parentId
		};
		
		int optResult = jdbcBase.update(sql, params);
		
		if ( optResult < 0 )
		{
			log.error("ChildDao-addChild: add child table error");
			return false;
		}
		return true;
	}
	
	public boolean updateChild(Child child)
	{
		String sql = "update child set";
		List<Object> paramsList = new ArrayList<Object>();
		
		sql = createUpdateSql(sql, paramsList, child);
		if ( sql != null )
		{
			Object[] params = paramsList.toArray();
			
			int optResult = jdbcBase.update(sql, params);
			
			if ( optResult < 0 )
			{
				log.error("ChildDao-updateChild: update child error");
				return false;
			}
		}
		return true;
	}
	
	public boolean deleteChild(String uuid)
	{
		String sql = "delete from child where uuid = ?";
		Object[] params = new Object[]{uuid};
		
		int optResult = jdbcBase.update(sql, params);
		if ( optResult < 0 )
		{
			log.error("ChildDao-deleteChild: delete child error");
			return false;
		}
		
		sql = "delete from user where uuid = ?";
		optResult = jdbcBase.update(sql, params);
		
		if ( optResult < 0 )
		{
			log.error("ChildDao-deleteChild: delete child error");
			return false;
		}
		return true;
	}
	
	private String createUpdateSql(String sql, List<Object> paramsList, Child child)
	{
		if ( child.parentId != null )
		{
			sql = sql + " parentId = ?,";
			paramsList.add(child.parentId);
		}
		int length = sql.length();
		if ( sql.charAt(length-1) != ',' )
		{
			return null;
		}
		sql = sql.substring(0,	sql.length() - 1);
		sql = sql + " where uuid = ?";
		paramsList.add(child.uuid);
		return sql;
	}

	public Long computeChildNumOfUuid(String uuid) 
	{		
		String sql = "select count(*) from child where parentId = ? ";
		int[] types = new int[]{Types.VARCHAR};
		Object[] params = new Object[]{uuid};
		List< Map<String, Object> > resultSet = jdbcBase.query(sql, params, types);
		
		return (Long)resultSet.get(0).get("count(*)");
	}
}
