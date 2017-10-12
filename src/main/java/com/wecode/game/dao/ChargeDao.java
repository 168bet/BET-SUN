package com.wecode.game.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wecode.game.bean.Charge;
import com.wecode.game.exception.IntConvertBoolException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.util.MapConvertBeanUtil;
import com.wecode.game.util.SpringJdbc;

@Repository("chargeDao")
public class ChargeDao {

	@Resource
	private SpringJdbc jdbcBase;
	private static Logger log = Logger.getLogger(ChargeDao.class);

	public boolean addCharge(Charge charge) {
		String sql = "insert into charge "
				+ "(orderNum,ybOrderNum,uid,uname,upoint,goodsId,amount,payTimes,state,createTime,payTime,userAgent,extendInfo)"
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] { charge.orderNum, charge.ybOrderNum, charge.uid,
				charge.uname, charge.upoint, charge.goodsId, charge.amount, charge.payTimes,
				charge.state, charge.createTime, charge.payTime, charge.userAgent,
				charge.extendInfo };
		int optResult = jdbcBase.update(sql, params);

		if (optResult < 0) {
			log.error("deposit table error");
			return false;
		}
		return true;
	}

	public Charge getChargeByOrderNum(String orderNum) throws ResultToBeanException {
		if (orderNum == null || orderNum.trim() == "") {
			return null;
		}
		String sql = "select * from charge as c where c.orderNum = ?";
		Object[] params = new Object[] { orderNum };
		int[] types = new int[] { Types.VARCHAR };
		List<Map<String, Object>> result = null;

		result = jdbcBase.query(sql, params, types);

		if (result.size() != 1) {
			return null;
		}

		Charge charge;
		try {
			charge = MapConvertBeanUtil.resultToBean(result.get(0), Charge.class);
		} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
				| IntConvertBoolException e) {
			log.error("getChargeByOrderNum: change charge result to bean fail. Exception message: "
					+ e.getMessage());
			throw new ResultToBeanException();
		}
		return charge;
	}

	public boolean updateCharge(Charge charge) {
		String sql = "update charge set";
		List<Object> paramsList = new ArrayList<Object>();

		sql = createUpdateSql(sql, paramsList, charge);
		if (sql != null) {
			Object[] params = paramsList.toArray();
			System.out.println(sql);
			int optResult = jdbcBase.update(sql, params);

			if (optResult < 0) {
				log.error("updateCharge: update user error");
				return false;
			}
			return true;
		}
		return false;
	}

	private String createUpdateSql(String sql, List<Object> paramsList, Charge charge) {
		if (charge.ybOrderNum != null) {
			sql = sql + " ybOrderNum = ?,";
			paramsList.add(charge.ybOrderNum);
		}
		if (charge.uid != null) {
			sql = sql + " uid = ?,";
			paramsList.add(charge.uid);
		}
		if (charge.uname != null) {
			sql = sql + " uname = ?,";
			paramsList.add(charge.uname);
		}
		if (charge.upoint != null) {
			sql = sql + " upoint = ?,";
			paramsList.add(charge.upoint);
		}
		if (charge.goodsId != null) {
			sql = sql + " goodsId = ?,";
			paramsList.add(charge.goodsId);
		}
		if (charge.amount != null) {
			sql = sql + " amount = ?,";
			paramsList.add(charge.amount);
		}
		if (charge.payTimes != null) {
			sql = sql + " payTimes = ?,";
			paramsList.add(charge.payTimes);
		}
		if (charge.state != null) {
			sql = sql + " state = ?,";
			paramsList.add(charge.state);
		}
		if (charge.createTime != null) {
			sql = sql + " createTime = ?,";
			paramsList.add(charge.createTime);
		}
		if (charge.payTime != null) {
			sql = sql + " payTime = ?,";
			paramsList.add(charge.payTime);
		}
		if (charge.ip != null) {
			sql = sql + " ip = ?,";
			paramsList.add(charge.ip);
		}
		if (charge.userAgent != null) {
			sql = sql + " userAgent = ?,";
			paramsList.add(charge.userAgent);
		}
		if (charge.extendInfo != null) {
			sql = sql + " extendInfo = ?,";
			paramsList.add(charge.extendInfo);
		}
		int length = sql.length();
		if (sql.charAt(length - 1) != ',') {
			return null;
		}
		sql = sql.substring(0, sql.length() - 1);
		sql = sql + " where orderNum = ?";
		paramsList.add(charge.orderNum);
		return sql;
	}
}
