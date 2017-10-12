package com.wecode.game.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wecode.bean.select.BetSelect;
import com.wecode.game.bean.Bet;
import com.wecode.game.util.MapConvertBeanUtil;
import com.wecode.game.util.SpringJdbc;

@Repository("reportDao")
public class ReportDao
{
	@Resource
	private SpringJdbc jdbcBase;
	private static Logger log = Logger.getLogger(ReportDao.class);
	public boolean insertBet(Bet bet) 
	{
		String sql = "insert into bet (uuid, productId, username, gameRecordId, orderNumber, tableId, stage, inning, gameNameId"
				+ ", gameBettingKind, resultType, bettingAmount, compensateRate, winLoseAmount, balance, platformId, addTime, vendorId)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[]{
				bet.uuid, bet.productId, bet.username, bet.gameRecordId, bet.orderNumber, bet.tableId, bet.stage, bet.inning, bet.gameNameId,
				bet.gameBettingKind, bet.resultType, bet.bettingAmount, bet.compensateRate, bet.winLoseAmount, bet.balance, bet.platformId, 
				bet.addTime, bet.vendorId
		};
		int optResult = jdbcBase.update(sql, params);
		if (optResult < 0)
		{
			return false;
		}
		return true;
	}
	public List<Bet> getBetList(BetSelect betSelect) 
	{
		String sql = "select * from bet where 1 = 1";
		List<Integer> typeList = new ArrayList<Integer>();
		List<Object> paramList = new ArrayList<Object>();
		sql = addWhereForSql(sql, betSelect, typeList, paramList);
		int[] types = ArrayUtils.toPrimitive(typeList.toArray(new Integer[typeList.size()]));
		Object[] params = paramList.toArray();
		List< Map<String, Object> > resultSet = jdbcBase.query(sql, params, types);
		
		List<Bet> betList = new ArrayList<Bet>();
		for (Map<String, Object> result : resultSet) 
		{
			Bet bet;
			try 
			{
				bet = MapConvertBeanUtil.resultToBean(result, Bet.class);
				betList.add(bet);
			} 
			catch (Exception e) 
			{
				log.error("covert bet error");
				continue;
			}
		}
		return betList;
	}
	
	private String addWhereForSql(String sql, BetSelect betSelect, List<Integer> typeList, List<Object> paramList)
	{
		if ( betSelect.username != null )
		{
			sql += " and username = ?";
			typeList.add(Types.VARCHAR);
			paramList.add(betSelect.username);
		}
		if ( betSelect.startTime != null )
		{
			sql += " and addTime >= ?";
			typeList.add(Types.BIGINT);
			paramList.add(betSelect.startTime);
		}
		if ( betSelect.finishTime != null )
		{
			sql += " and addTime <= ?";
			typeList.add(Types.BIGINT);
			paramList.add(betSelect.finishTime);
		}
		return sql;
	}
}
