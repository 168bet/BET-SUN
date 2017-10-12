package com.wecode.game.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wecode.bean.select.BetSelect;
import com.wecode.game.bean.Member;
import com.wecode.game.exception.IntConvertBoolException;
import com.wecode.game.util.MapConvertBeanUtil;
import com.wecode.game.util.SpringJdbc;

@Repository("memberDao")
public class MemberDao
{
	@Resource
	private SpringJdbc jdbcBase;
	private static Logger log = Logger.getLogger(MemberDao.class);
	
	//根据uuid获取代理
	public Member getMemberByUuid(String uuid) 
	{
		String sql = "select * from user as u inner join member as a on u.uuid like a.uuid where a.uuid = ?";
		Object[] params = new Object[]{uuid};
		int[] types = new int[]{Types.VARCHAR};
		List<Map<String,Object>> result = null;
		
		result = jdbcBase.query(sql, params, types);
		
		if ( result.size() != 1 )
		{
			return null;
		}
		
		Member member;
		try
		{
			member = MapConvertBeanUtil.resultToBean(result.get(0), Member.class);
		} 
		catch (IllegalArgumentException | InstantiationException | IllegalAccessException | IntConvertBoolException e)
		{
			log.error("MemberDao-getMemberByMembername: change result to bean fail. Exception message: " + e.getMessage());
			return null;
		}
		return member;		
	}
	
	public Member getMemberByUsername(String username)
	{
		String sql = "select * from user as u inner join member as a on u.uuid like a.uuid where u.username = ?";
		Object[] params = new Object[]{username};
		int[] types = new int[]{Types.VARCHAR};
		List<Map<String,Object>> result = null;
		
		result = jdbcBase.query(sql, params, types);
		
		if ( result.size() != 1 )
		{
			return null;
		}
		
		Member member;
		try
		{
			member = MapConvertBeanUtil.resultToBean(result.get(0), Member.class);
		} 
		catch (IllegalArgumentException | InstantiationException | IllegalAccessException | IntConvertBoolException e)
		{
			log.error("MemberDao-getMemberByUsername: change result to bean fail. Exception message: " + e.getMessage());
			return null;
		}
		return member;	
	}
	
	public List<Member> getMemberListByUsername(BetSelect betSelect) 
	{
		String sql = "select * from user as u inner join member as a on u.uuid like a.uuid where u.username like ?";
		Object[] params = new Object[]{"%" + betSelect.username + "%"};
		int[] types = new int[]{Types.VARCHAR};
		List<Map<String,Object>> resultSet = null;
		
		if ( betSelect.page != null && betSelect.num != null )
		{
			resultSet = jdbcBase.query(sql, params, types,  betSelect.page, betSelect.num );
		}
		else
		{
			resultSet = jdbcBase.query(sql, params, types);
		}
		
		if ( resultSet == null || resultSet.size() == 0 )
		{
			return null;
		}
		
		List<Member> memberList = new ArrayList<Member>();
		for ( Map<String, Object> object : resultSet )
		{
			try
			{
				Member member = (Member)MapConvertBeanUtil.resultToBean(object, Member.class);
				member.password = null;
				memberList.add(member);
			} 
			catch (IllegalArgumentException | InstantiationException
					| IllegalAccessException | IntConvertBoolException e)
			{
				log.error("MemberDao-getMemberListByUsername: change result to bean fail. Exception message: " + e.getMessage());
				continue;
			}
		}
		return memberList;
	}
	
	public Long countMemberListByUsername(String username)
	{
		String sql = "select count(*) from user as u inner join member as a on u.uuid like a.uuid where u.username like ?";
		Object[] params = new Object[]{"%" + username + "%"};
		int[] types = new int[]{Types.VARCHAR};
		List<Map<String, Object>> result = null;
		result = jdbcBase.query(sql, params, types);
		
		if ( result == null || result.size() == 0 )
		{
			return null;
		}
		
		return (Long)result.get(0).get("count(*)");
	}
	
	//
	public Long countMembersByUser(String uuid)
	{
		String sql = "select count(*) from user as u inner join member as a on u.uuid like a.uuid where a.parentId = ?";
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
	public List<Member> getMemberListByUser(String uuid, Integer num, Integer page)
	{
		String sql = "select * from user as u inner join member as a on u.uuid like a.uuid where a.parentId = ?";
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
		
		List<Member> memberList = new ArrayList<Member>();
		for ( Map<String, Object> object : result )
		{
			try
			{
				Member member = (Member)MapConvertBeanUtil.resultToBean(object, Member.class);
				member.password = null;
				memberList.add(member);
			} 
			catch (IllegalArgumentException | InstantiationException
					| IllegalAccessException | IntConvertBoolException e)
			{
				log.error("MemberDao-getMemberListByUser: change result to bean fail. Exception message: " + e.getMessage());
				continue;
			}
		}
		return memberList;
	}
	
	public boolean addMember(Member member)
	{
		String sql = "insert into member (uuid, point, upperLimit, privilegeNum, profit, parentId, moneyPwd) "
					 + "values(?,?,?,?,?,?,?)";
		Object[] params = new Object[]{
				member.uuid, member.point, member.upperLimit, member.privilegeNum, member.profit, member.parentId, member.moneyPwd
		};
		
		int optResult = jdbcBase.update(sql, params);
		
		if ( optResult < 0 )
		{
			log.error("MemberDao-addMember: add member table error");
			return false;
		}
		return true;
	}
	
	public boolean updateMember(Member member)
	{
		String sql = "update member set";
		List<Object> paramsList = new ArrayList<Object>();
		
		sql = createUpdateSql(sql, paramsList, member);
		if ( sql != null)
		{
			Object[] params = paramsList.toArray();
			
			int optResult = jdbcBase.update(sql, params);
			
			if ( optResult < 0 )
			{
				log.error("MemberDao-updateMember: update member error");
				return false;
			}
		}
		return true;
	}
	
	public boolean deleteMember(String uuid)
	{
		String sql = "delete from member where uuid = ?";
		Object[] params = new Object[]{uuid};
		
		int optResult = jdbcBase.update(sql, params);
		if ( optResult < 0 )
		{
			log.error("MemberDao-deleteMember: delete member error");
			return false;
		}
		
		sql = "delete from user where uuid = ?";
		optResult = jdbcBase.update(sql, params);
		
		if ( optResult < 0 )
		{
			log.error("MemberDao-deleteMember: delete member error");
			return false;
		}
		return true;
	}
	
	private String createUpdateSql(String sql, List<Object> paramsList, Member member)
	{
		if ( member.point != null )
		{
			sql = sql + " point = ?,";
			paramsList.add(member.point);
		}
		if ( member.upperLimit != null )
		{
			sql = sql + " upperLimit = ?,";
			paramsList.add(member.upperLimit);
		}
		if ( member.privilegeNum != null )
		{
			sql = sql + " privilegeNum = ?,";
			paramsList.add(member.privilegeNum);
		}
		if ( member.profit != null )
		{
			sql = sql + " profit = ? ";
			paramsList.add(member.profit);
		}
		if ( member.parentId != null )
		{
			sql = sql + " parentId = ?,";
			paramsList.add(member.parentId);
		}
		if ( member.lastGameId != null )
		{
			sql = sql + " lastGameId = ?,";
			paramsList.add(member.lastGameId);
		}
		if ( member.moneyPwd != null )
		{
			sql = sql + " moneyPwd = ?,";
			paramsList.add(member.moneyPwd);
		}
		int length = sql.length();
		if ( sql.charAt(length-1) != ',' )
		{
			return null;
		}
		sql = sql.substring(0,	sql.length() - 1);
		sql = sql + " where uuid = ?";
		paramsList.add(member.uuid);
		return sql;
	}

	public Long computeMemberNumOfUuid(String uuid) 
	{
		String sql = "select count(*) from member where parentId = ? ";
		int[] types = new int[]{Types.VARCHAR};
		Object[] params = new Object[]{uuid};
		List< Map<String, Object> > resultSet = jdbcBase.query(sql, params, types);
		
		return (Long)resultSet.get(0).get("count(*)");
	}
}
