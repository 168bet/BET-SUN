package com.wecode.game.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wecode.game.bean.Bill;
import com.wecode.game.exception.IntConvertBoolException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.identified.Role;
import com.wecode.game.util.MapConvertBeanUtil;
import com.wecode.game.util.SpringJdbc;

@Repository("billDao")
public class BillDao {
	@Resource
	private SpringJdbc jdbcBase;
	private static Logger log = Logger.getLogger(BillDao.class);

	// 存款
	public boolean deposit(Bill bill) {
		String sql = "insert into bill (uuid, userId, point, type, info, createdTime,cardType,cardSite,cardArea,cardCity,state) values (?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] { bill.uuid, bill.userId, bill.point, bill.type, bill.info,
				bill.createdTime, bill.cardType, bill.cardSite, bill.cardArea, bill.cardCity,
				bill.state };
		int optResult = jdbcBase.update(sql, params);

		if (optResult < 0) {
			log.error("deposit table error");
			return false;
		}
		return true;
	}

	public boolean withdrawals(Bill bill) {
		String sql = "insert into bill (uuid, userId, point, type, info, cardType, cardNum, cardArea, cardCity, cardSite, state, createdTime)"
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] { bill.uuid, bill.userId, bill.point, bill.type, bill.info,
				bill.cardType, bill.cardNum, bill.cardArea, bill.cardCity, bill.cardSite,
				bill.state, bill.createdTime};
		int optResult = jdbcBase.update(sql, params);

		if (optResult < 0) {
			log.error("withdrawals table error");
			return false;
		}
		return true;
	}

	public boolean updateBillState(Bill bill) {
		String sql = "update bill set state = ? where uuid = ?";
		Object[] params = new Object[] { bill.state, bill.uuid };
		int optResult = jdbcBase.update(sql, params);
		if (optResult < 0) {
			log.error("update bill error");
			return false;
		}
		return true;
	}

	public List<Bill> getBillListOfWithdrawals(Integer state, Integer role, Integer page, Integer num) {
		String sql;
		int[] types;
		Object[] params;
		if (role == Role.MEMBER)
		{
			sql = "select b.uuid as uuid, b.userId as userId, u.username as username, u.name as name, m.point as totalPoint, "
					+ "b.point as point, cardType, cardNum, cardCity, cardSite, cardArea,  phone, b.createdTime as createdTime"
					+ " from bill as b inner join user as u on b.userId = u.uuid inner join member as m on u.uuid = m.uuid "
					+ " where b.type = 1";// BillType.WITHDRAWALS = 1
		}
		else
		{
			sql = "select b.uuid as uuid, b.userId as userId, u.username as username, u.name as name, a.point as totalPoint, "
					+ "b.point as point, cardType, cardNum, cardCity, cardSite, cardArea,  phone, b.createdTime as createdTime"
					+ " from bill as b inner join user as u on b.userId = u.uuid inner join agent as a on u.uuid = a.uuid "
					+ " where b.type = 1";// BillType.WITHDRAWALS = 1
		}
		types = new int[] {};
		params = new Object[] {};
		if (state != null) {
			sql += " and b.state = ?";
			types = new int[] { Types.INTEGER };
			params = new Object[] { state };
		}
		List<Map<String, Object>> resultSet = null;
		if (page != null && num != null) {
			resultSet = jdbcBase.query(sql, params, types, page, num);
		} else {
			resultSet = jdbcBase.query(sql, params, types);
		}

		List<Bill> billList = new ArrayList<Bill>();
		for (Map<String, Object> result : resultSet) {
			Bill bill;
			try {
				bill = MapConvertBeanUtil.resultToBean(result, Bill.class);
				billList.add(bill);
			} catch (Exception e) {
				log.error("covert bill error");
				continue;
			}
		}
		return billList;
	}

	public List<Bill> getDepositesByState(Integer state, Integer page, Integer num) {
		String sql;
		int[] types;
		Object[] params;
		sql = "select b.uuid as uuid, b.userId as userId, u.username as username, u.name as name, m.point as totalPoint, "
				+ "b.point as point, cardType, cardNum, b.info, cardCity, cardSite, cardArea,  phone, b.createdTime as createdTime"
				+ " from bill as b inner join user as u on b.userId = u.uuid inner join member as m on u.uuid = m.uuid "
				+ " where b.type = 0";// BillType.DEPOSITE = 0
		types = new int[] {};
		params = new Object[] {};
		if (state != null) {
			sql += " and b.state = ?";
			types = new int[] { Types.INTEGER };
			params = new Object[] { state };
		}
		List<Map<String, Object>> resultSet = null;
		if (page != null && num != null) {
			resultSet = jdbcBase.query(sql, params, types, page, num);
		} else {
			resultSet = jdbcBase.query(sql, params, types);
		}

		List<Bill> billList = new ArrayList<Bill>();
		for (Map<String, Object> result : resultSet) {
			Bill bill;
			try {
				bill = MapConvertBeanUtil.resultToBean(result, Bill.class);
				billList.add(bill);
			} catch (Exception e) {
				log.error("covert bill error");
				continue;
			}
		}
		return billList;
	}

	public Long countDepositesByState(Integer state) {
		// BillType.DEPOSITE = 0
		String sql = "select count(*) from bill where type = 0";
		int[] types = new int[] {};
		Object[] params = new Object[] {};

		if (state != null) {
			sql += " and state = ?";
			types = new int[] { Types.INTEGER };
			params = new Object[] { state };
		}
		List<Map<String, Object>> resultSet = jdbcBase.query(sql, params, types);

		return (Long) resultSet.get(0).get("count(*)");
	}

	public Long computeBillsOfWithdrawals(Integer state) {
		// BillType.WITHDRAWALS = 1
		String sql = "select count(*) from bill where type = 1";
		int[] types = new int[] {};
		Object[] params = new Object[] {};
		if (state != null) {
			sql += " and state = ?";
			types = new int[] { Types.INTEGER };
			params = new Object[] { state };
		}
		List<Map<String, Object>> resultSet = jdbcBase.query(sql, params, types);

		return (Long) resultSet.get(0).get("count(*)");
	}

	public List<Bill> getBillListOfDeposite(String memberId, Integer page, Integer num) {
		String sql = "select * from bill where userId = ?";
		int[] types = new int[] { Types.VARCHAR };
		Object[] params = new Object[] { memberId };
		List<Map<String, Object>> resultSet = null;
		if (page != null && num != null) {
			resultSet = jdbcBase.query(sql, params, types, page, num);
		} else {
			resultSet = jdbcBase.query(sql, params, types);
		}

		List<Bill> billList = new ArrayList<Bill>();
		for (Map<String, Object> result : resultSet) {
			Bill bill;
			try {
				bill = MapConvertBeanUtil.resultToBean(result, Bill.class);
				billList.add(bill);
			} catch (Exception e) {
				log.error("covert bill error");
				continue;
			}
		}
		return billList;
	}

	public Long computeBillsOfDeposite(String memberId) {
		String sql = "select count(*) from bill where userId = ?";
		int[] types = new int[] { Types.VARCHAR };
		Object[] params = new Object[] { memberId };
		List<Map<String, Object>> resultSet = jdbcBase.query(sql, params, types);

		return (Long) resultSet.get(0).get("count(*)");
	}

	public List<Bill> getMemberBills(String memberId, Integer page, Integer num) {
		String sql = "select * from bill where userId = ? order by createdTime desc";
		int[] types = new int[] { Types.VARCHAR };
		Object[] params = new Object[] { memberId };
		List<Map<String, Object>> resultSet = null;
		if (page != null && num != null) {
			resultSet = jdbcBase.query(sql, params, types, page, num);
		} else {
			resultSet = jdbcBase.query(sql, params, types);
		}

		List<Bill> billList = new ArrayList<Bill>();
		for (Map<String, Object> result : resultSet) {
			Bill bill;
			try {
				bill = MapConvertBeanUtil.resultToBean(result, Bill.class);
				billList.add(bill);
			} catch (Exception e) {
				log.error("covert bill error");
				continue;
			}
		}
		return billList;
	}

	public Long countMemberBills(String memberId) {
		String sql = "select count(*) from bill where userId = ?";
		int[] types = new int[] { Types.VARCHAR };
		Object[] params = new Object[] { memberId };
		List<Map<String, Object>> resultSet = jdbcBase.query(sql, params, types);
		return (Long) resultSet.get(0).get("count(*)");
	}

	public Bill getBillByUuid(String uuid) throws ResultToBeanException {
		String sql = "select * from bill where uuid = ?";
		Object[] params = new Object[] { uuid };
		int[] types = new int[] { Types.VARCHAR };
		List<Map<String, Object>> result = null;

		result = jdbcBase.query(sql, params, types);

		if (result.size() != 1) {
			return null;
		}

		try {
			return MapConvertBeanUtil.resultToBean(result.get(0), Bill.class);
		} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
				| IntConvertBoolException e) {
			log.error("getGameByTypeAndID: change result to bean fail. Exception message: "
					+ e.getMessage());
			throw new ResultToBeanException();
		}
	}
}
