package com.wecode.game.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wecode.game.bean.UserLog;
import com.wecode.game.exception.CommonException;
import com.wecode.game.exception.IntConvertBoolException;
import com.wecode.game.exception.SaveObjectException;
import com.wecode.game.util.MapConvertBeanUtil;
import com.wecode.game.util.SpringJdbc;

@Repository("userLogDao")
public class UserLogDao {
	@Resource
	private SpringJdbc jdbcBase;
	private static Logger log = Logger.getLogger(UserLogDao.class);

	public void addLog(UserLog userLog) throws CommonException {
		String sql = "insert into userlog (time, ip, userId, username, role, pid, content, state)"
				+ " values (?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] { userLog.time, userLog.ip, userLog.userId,
				userLog.username, userLog.role, userLog.pid, userLog.content, userLog.state };
		int optResult = jdbcBase.update(sql, params);

		if (optResult < 0) {
			throw new SaveObjectException();
		}

	}

	public List<UserLog> getManageLog(String userId, Integer role, Long start, Long end,
			Integer num, Integer page) {
		String sql = "select * from userlog where (userId = ? or role > ?) ";
		List<Object> plist = new ArrayList<Object>();
		plist.add(userId);
		plist.add(role);
		List<Integer> tlist = new ArrayList<Integer>();
		tlist.add(Types.VARCHAR);
		tlist.add(Types.INTEGER);
		if (start != null) {
			sql += " and time > ? ";
			plist.add(start);
			tlist.add(Types.BIGINT);
		}
		if (end != null) {
			sql += " and time <= ? ";
			plist.add(end);
			tlist.add(Types.BIGINT);
		}
		List<Map<String, Object>> result = null;

		if (num != null && page != null) {
			result = jdbcBase.query(sql, plist.toArray(), listToIntArray(tlist), page, num);
		} else {
			result = jdbcBase.query(sql, plist.toArray(), listToIntArray(tlist));
		}

		if (result == null || result.size() == 0) {
			return null;
		}

		List<UserLog> msgs = new ArrayList<UserLog>();
		for (Map<String, Object> object : result) {
			try {
				msgs.add(MapConvertBeanUtil.resultToBean(object, UserLog.class));
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
					| IntConvertBoolException e) {
				log.error("getAllMsgs: change result to bean fail. Exception message: "
						+ e.getMessage());
				continue;
			}
		}
		return msgs;
	}

	public Long countManageLog(String userId, Integer role, Long start, Long end) {
		String sql = "select count(1) from userlog where userId = ? or role > ?";
		List<Object> plist = new ArrayList<Object>();
		plist.add(userId);
		plist.add(role);
		List<Integer> tlist = new ArrayList<Integer>();
		tlist.add(Types.VARCHAR);
		tlist.add(Types.INTEGER);
		if (start != null) {
			sql += " and time > ? ";
			plist.add(start);
			tlist.add(Types.BIGINT);
		}
		if (end != null) {
			sql += " and time <= ? ";
			plist.add(end);
			tlist.add(Types.BIGINT);
		}
		List<Map<String, Object>> result = null;
		result = jdbcBase.query(sql, plist.toArray(), listToIntArray(tlist));

		if (result == null || result.size() == 0) {
			return null;
		}

		return (Long) result.get(0).get("count(1)");
	}

	public List<UserLog> getUsersLogByIds(List<String> subs, Long start, Long end, Integer num,
			Integer page) {
		if (subs == null || subs.size() < 0) {
			return null;
		}
		StringBuilder sqlBuilder = new StringBuilder("select * from userlog where userId in (");
		for (String s : subs) {
			sqlBuilder.append("'").append(s).append("',");
		}
		sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
		sqlBuilder.append(")");
		List<Object> plist = new ArrayList<Object>();
		List<Integer> tlist = new ArrayList<Integer>();
		if (start != null) {
			sqlBuilder.append(" and time > ? ");
			plist.add(start);
			tlist.add(Types.BIGINT);
		}
		if (end != null) {
			sqlBuilder.append(" and time <= ? ");
			plist.add(end);
			tlist.add(Types.BIGINT);
		}

		List<Map<String, Object>> result = null;

		if (num != null && page != null) {
			result = jdbcBase.query(sqlBuilder.toString(), plist.toArray(), listToIntArray(tlist),
					page, num);
		} else {
			result = jdbcBase.query(sqlBuilder.toString(), plist.toArray(), listToIntArray(tlist));
		}

		if (result == null || result.size() == 0) {
			return null;
		}

		List<UserLog> msgs = new ArrayList<UserLog>();
		for (Map<String, Object> object : result) {
			try {
				msgs.add(MapConvertBeanUtil.resultToBean(object, UserLog.class));
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
					| IntConvertBoolException e) {
				log.error("getAllMsgs: change result to bean fail. Exception message: "
						+ e.getMessage());
				continue;
			}
		}
		return msgs;
	}

	public Long countUsersLogByIds(List<String> subs, Long start, Long end) {
		if (subs == null || subs.size() < 0) {
			return 0L;
		}
		StringBuilder sqlBuilder = new StringBuilder(
				"select count(1) from userlog where userId in (");
		for (String s : subs) {
			sqlBuilder.append("'").append(s).append("',");
		}
		sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
		sqlBuilder.append(")");
		List<Object> plist = new ArrayList<Object>();
		List<Integer> tlist = new ArrayList<Integer>();
		if (start != null) {
			sqlBuilder.append(" and time > ? ");
			plist.add(start);
			tlist.add(Types.BIGINT);
		}
		if (end != null) {
			sqlBuilder.append(" and time <= ? ");
			plist.add(end);
			tlist.add(Types.BIGINT);
		}

		List<Map<String, Object>> result = null;
		result = jdbcBase.query(sqlBuilder.toString(), plist.toArray(), listToIntArray(tlist));
		if (result == null || result.size() == 0) {
			return null;
		}
		return (Long) result.get(0).get("count(1)");
	}

	private int[] listToIntArray(List<Integer> list) {
		int ret[] = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ret[i] = ((Integer) list.get(i)).intValue();
		}
		return ret;
	}

	public boolean deleteLog(String id) {
		String sql = "delete from userlog where id = ?";

		int optResult = jdbcBase.update(sql, new Object[] { id });
		if (optResult < 0) {
			log.error("deleteMsg: delete userlog(id:" + id + ") error");
			return false;
		}
		return true;
	}

	public List<UserLog> getUsersLogByName(String username, Long start, Long end, Integer num,
			Integer page) {
		String sql = "select * from userlog where username like ? ";
		List<Object> plist = new ArrayList<Object>();
		plist.add(username);
		List<Integer> tlist = new ArrayList<Integer>();
		tlist.add(Types.VARCHAR);
		if (start != null) {
			sql += " and time > ? ";
			plist.add(start);
			tlist.add(Types.BIGINT);
		}
		if (end != null) {
			sql += " and time <= ? ";
			plist.add(end);
			tlist.add(Types.BIGINT);
		}
		List<Map<String, Object>> result = null;

		if (num != null && page != null) {
			result = jdbcBase.query(sql, plist.toArray(), listToIntArray(tlist), page, num);
		} else {
			result = jdbcBase.query(sql, plist.toArray(), listToIntArray(tlist));
		}

		if (result == null || result.size() == 0) {
			return null;
		}

		List<UserLog> msgs = new ArrayList<UserLog>();
		for (Map<String, Object> object : result) {
			try {
				msgs.add(MapConvertBeanUtil.resultToBean(object, UserLog.class));
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
					| IntConvertBoolException e) {
				log.error("getAllMsgs: change result to bean fail. Exception message: "
						+ e.getMessage());
				continue;
			}
		}
		return msgs;
	}

	public Long countUsersLogByName(String username, Long start, Long end) {
		String sql = "select count(1) from userlog where username like ?";
		List<Object> plist = new ArrayList<Object>();
		plist.add(username);
		List<Integer> tlist = new ArrayList<Integer>();
		tlist.add(Types.VARCHAR);
		if (start != null) {
			sql += " and time > ? ";
			plist.add(start);
			tlist.add(Types.BIGINT);
		}
		if (end != null) {
			sql += " and time <= ? ";
			plist.add(end);
			tlist.add(Types.BIGINT);
		}
		List<Map<String, Object>> result = null;
		result = jdbcBase.query(sql, plist.toArray(), listToIntArray(tlist));

		if (result == null || result.size() == 0) {
			return null;
		}

		return (Long) result.get(0).get("count(1)");
	}

}
