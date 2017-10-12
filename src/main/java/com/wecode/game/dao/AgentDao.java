package com.wecode.game.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wecode.game.bean.Agent;
import com.wecode.game.exception.IntConvertBoolException;
import com.wecode.game.util.MapConvertBeanUtil;
import com.wecode.game.util.SpringJdbc;

@Repository("agentDao")
public class AgentDao
{
	@Resource
	private SpringJdbc jdbcBase;
	private static Logger log = Logger.getLogger(AgentDao.class);

	// 根据uuid获取代理
	public Agent getAgentByUuid(String uuid)
	{
		String sql = "select * from user as u inner join agent as a on u.uuid like a.uuid where a.uuid = ?";
		Object[] params = new Object[]
		{ uuid };
		int[] types = new int[]
		{ Types.VARCHAR };
		List<Map<String, Object>> result = null;

		result = jdbcBase.query(sql, params, types);

		if (result.size() != 1)
		{
			return null;
		}

		Agent agent;
		try
		{
			agent = MapConvertBeanUtil.resultToBean(result.get(0), Agent.class);
		} catch (IllegalArgumentException | InstantiationException
				| IllegalAccessException | IntConvertBoolException e)
		{
			log.error("AgentDao-getAgentByAgentname: change result to bean fail. Exception message: "
					+ e.getMessage());
			return null;
		}
		return agent;
	}

	//
	public Long countAgentsByUser(String uuid)
	{
		String sql = "select count(*) from user as u inner join agent as a on u.uuid like a.uuid where a.parentId = ?";
		Object[] params = new Object[]
		{ uuid };
		int[] types = new int[]
		{ Types.VARCHAR };
		List<Map<String, Object>> result = null;
		result = jdbcBase.query(sql, params, types);

		if (result == null || result.size() == 0)
		{
			return null;
		}

		return (Long) result.get(0).get("count(*)");
	}
	
	public Agent getAgentByUsername(String username) 
	{
		String sql = "select * from user as u inner join agent as a on u.uuid like a.uuid where u.username = ?";
		Object[] params = new Object[]{ username };
		int[] types = new int[]
		{ Types.VARCHAR };
		List<Map<String, Object>> result = null;

		result = jdbcBase.query(sql, params, types);

		if (result.size() != 1)
		{
			return null;
		}

		Agent agent;
		try
		{
			agent = MapConvertBeanUtil.resultToBean(result.get(0), Agent.class);
		} catch (IllegalArgumentException | InstantiationException
				| IllegalAccessException | IntConvertBoolException e)
		{
			log.error("AgentDao-getAgentByAgentname: change result to bean fail. Exception message: "
					+ e.getMessage());
			return null;
		}
		return agent;
	}

	// 根据uuid获取子代理列表
	public List<Agent> getAgentListByUser(String uuid, Integer num, Integer page)
	{
		String sql = "select u.uuid, username, point, commissionPer, rebate, state, locked, onlyRead, createdTime, phone, email, parentId from user as u inner join agent as a on u.uuid like a.uuid where a.parentId = ?";

		Object[] params = new Object[]
		{ uuid };
		int[] types = new int[]
		{ Types.VARCHAR };
		List<Map<String, Object>> result = null;

		if (num != null && page != null)
		{
			result = jdbcBase.query(sql, params, types, page, num);
		} else
		{
			result = jdbcBase.query(sql, params, types);
		}

		if (result == null || result.size() == 0)
		{
			return null;
		}

		List<Agent> agentList = new ArrayList<Agent>();
		for (Map<String, Object> object : result)
		{
			try
			{
				Agent agent = (Agent) MapConvertBeanUtil.resultToBean(object,
						Agent.class);
				agent.password = null;
				agentList.add(agent);
			} catch (IllegalArgumentException | InstantiationException
					| IllegalAccessException | IntConvertBoolException e)
			{
				log.error("AgentDao-getAgentListByUser: change result to bean fail. Exception message: "
						+ e.getMessage());
				continue;
			}
		}
		return agentList;
	}

	public boolean addAgent(Agent agent)
	{
		String sql = "insert into agent (uuid,  point, commissionPer, rebate, onlyRead, parentId, moneyPwd) values (?,?,?,?,?,?,?)";
		Object[] params = new Object[]
		{ agent.uuid, agent.point, agent.commissionPer, agent.rebate, agent.onlyRead, agent.parentId, agent.moneyPwd };

		int optResult = jdbcBase.update(sql, params);

		if (optResult < 0)
		{
			log.error("AgentDao-addAgent: add agent table error");
			return false;
		}
		return true;
	}

	public boolean updateAgent(Agent agent)
	{
		String sql = "update agent set ";
		List<Object> paramsList = new ArrayList<Object>();

		sql = createUpdateSql(sql, paramsList, agent);
		if (sql != null)
		{
			Object[] params = paramsList.toArray();

			int optResult = jdbcBase.update(sql, params);

			if (optResult < 0)
			{
				log.error("AgentDao-updateAgent: update agent error");
				return false;
			}
		}
		return true;
	}

	public boolean deleteAgent(String uuid)
	{
		String sql = "delete from agent where uuid = ?";
		Object[] params = new Object[]
		{ uuid };

		int optResult = jdbcBase.update(sql, params);
		if (optResult < 0)
		{
			log.error("AgentDao-deleteAgent: delete agent error");
			return false;
		}

		sql = "delete from user where uuid = ?";
		optResult = jdbcBase.update(sql, params);

		if (optResult < 0)
		{
			log.error("AgentDao-deleteAgent: delete agent error");
			return false;
		}
		return true;
	}

	private String createUpdateSql(String sql, List<Object> paramsList,
			Agent agent)
	{
		if (agent.point != null)
		{
			sql = sql + " point = ?,";
			paramsList.add(agent.point);
		}
		if (agent.commissionPer != null)
		{
			sql = sql + " commissionPer = ?,";
			paramsList.add(agent.commissionPer);
		}
		if (agent.rebate != null)
		{
			sql = sql + " rebate = ?,";
			paramsList.add(agent.rebate);
		}
		if (agent.onlyRead != null)
		{
			sql = sql + " onlyRead = ?,";
			paramsList.add(agent.onlyRead);
		}
		if (agent.parentId != null)
		{
			sql = sql + " parentId = ?,";
			paramsList.add(agent.parentId);
		}
		if (agent.moneyPwd != null)
		{
			sql = sql + " moneyPwd = ?,";
			paramsList.add(agent.moneyPwd);
		}
		int length = sql.length();
		if (sql.charAt(length - 1) != ',')
		{
			return null;
		}
		sql = sql.substring(0, sql.length() - 1);
		sql = sql + " where uuid = ?";
		paramsList.add(agent.uuid);
		return sql;
	}

	public Long computeAgentNumOfUuid(String uuid) 
	{
		String sql = "select count(*) from agent where parentId = ? ";
		int[] types = new int[]{Types.VARCHAR};
		Object[] params = new Object[]{uuid};
		List< Map<String, Object> > resultSet = jdbcBase.query(sql, params, types);
		
		return (Long)resultSet.get(0).get("count(*)");
	}
}
