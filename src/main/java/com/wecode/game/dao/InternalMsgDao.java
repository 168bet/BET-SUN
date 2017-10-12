package com.wecode.game.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wecode.game.bean.InternalMsg;
import com.wecode.game.exception.IntConvertBoolException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.util.MapConvertBeanUtil;
import com.wecode.game.util.SpringJdbc;

@Repository("internalMsgDao")
public class InternalMsgDao {

	@Resource
	private SpringJdbc jdbcBase;
	private static Logger log = Logger.getLogger(InternalMsgDao.class);

	public InternalMsg getMsgByID(String id) throws ResultToBeanException {
		String sql = "select * from internalmsg where id = ?";
		Object[] params = new Object[] { id };
		int[] types = new int[] { Types.VARCHAR };
		List<Map<String, Object>> result = null;

		result = jdbcBase.query(sql, params, types);

		if (result.size() != 1) {
			return null;
		}

		try {
			return MapConvertBeanUtil.resultToBean(result.get(0), InternalMsg.class);
		} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
				| IntConvertBoolException e) {
			log.error("getMsgByTypeAndID: change result to bean fail. Exception message: "
					+ e.getMessage());
			throw new ResultToBeanException();
		}
	}

	public boolean addMsg(InternalMsg msg) {
		String sql = "insert into internalmsg (userId,title, content,state, createTime,username) "
				+ "values(?,?,?,?,?,?)";
		Object[] params = new Object[] { msg.userId, msg.title, msg.content, msg.state,
				msg.createTime, msg.username };

		int optResult = jdbcBase.update(sql, params);

		if (optResult < 0) {
			log.error("addMsg: add internalmsg table error");
			return false;
		}
		return true;
	}

	public boolean deleteMsg(String id) {
		String sql = "delete from internalmsg where id = ?";

		int optResult = jdbcBase.update(sql, new Object[] { id });
		if (optResult < 0) {
			log.error("deleteMsg: delete internalMsg(id:" + id + ") error");
			return false;
		}
		return true;
	}

	public boolean modifyMsg(InternalMsg msg) {
		String sql = "update internalmsg set ";
		List<Object> paramsList = new ArrayList<Object>();
		if (msg.title != null) {
			sql = sql + " title = ?,";
			paramsList.add(msg.title);
		}
		if (msg.content != null) {
			sql = sql + " content = ?,";
			paramsList.add(msg.content);
		}
		if (msg.state != null) {
			sql = sql + " state= ?,";
			paramsList.add(msg.state);
		}
		if (msg.createTime != null) {
			sql = sql + " createTime = ?,";
			paramsList.add(msg.createTime);
		}
		if (paramsList.size() < 1) {
			return false;
		}
		sql = sql.substring(0, sql.length() - 1);
		sql = sql + " where id = ?";
		paramsList.add(msg.id);

		if (jdbcBase.update(sql, paramsList.toArray()) < 0) {
			log.error("modifyMsg: update internalmsg error");
			return false;
		}
		return true;
	}

	public List<InternalMsg> getAllMsgs(Integer num, Integer page) {
		String sql = "select * from internalmsg ";
		Object[] params = new Object[] {};
		int[] types = new int[] {};
		List<Map<String, Object>> result = null;

		if (num != null && page != null) {
			result = jdbcBase.query(sql, params, types, page, num);
		} else {
			result = jdbcBase.query(sql, params, types);
		}

		if (result == null || result.size() == 0) {
			return null;
		}

		List<InternalMsg> msgs = new ArrayList<InternalMsg>();
		for (Map<String, Object> object : result) {
			try {
				msgs.add(MapConvertBeanUtil.resultToBean(object, InternalMsg.class));
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
					| IntConvertBoolException e) {
				log.error("getAllMsgs: change result to bean fail. Exception message: "
						+ e.getMessage());
				continue;
			}
		}
		return msgs;
	}

	public Long countAllMsgs() {
		String sql = "select count(1) from internalMsg";
		Object[] params = new Object[] {};
		int[] types = new int[] {};
		List<Map<String, Object>> result = null;
		result = jdbcBase.query(sql, params, types);

		if (result == null || result.size() == 0) {
			return null;
		}

		return (Long) result.get(0).get("count(1)");
	}

	public List<InternalMsg> getUserMsgs(String userId, Integer num, Integer page) {
		String sql = "select * from internalmsg where userId = ? order by state, createTime desc";
		Object[] params = new Object[] { userId };
		int[] types = new int[] { Types.VARCHAR };
		List<Map<String, Object>> result = null;

		if (num != null && page != null) {
			result = jdbcBase.query(sql, params, types, page, num);
		} else {
			result = jdbcBase.query(sql, params, types);
		}

		if (result == null || result.size() == 0) {
			return null;
		}

		List<InternalMsg> msgs = new ArrayList<InternalMsg>();
		for (Map<String, Object> object : result) {
			try {
				msgs.add(MapConvertBeanUtil.resultToBean(object, InternalMsg.class));
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
					| IntConvertBoolException e) {
				log.error("getAllMsgs: change result to bean fail. Exception message: "
						+ e.getMessage());
				continue;
			}
		}
		return msgs;
	}

	public Long countUserMsgs(String userId) {
		String sql = "select count(1) from internalmsg where userId = ?";
		Object[] params = new Object[] { userId };
		int[] types = new int[] { Types.VARCHAR };
		List<Map<String, Object>> result = null;
		result = jdbcBase.query(sql, params, types);

		if (result == null || result.size() == 0) {
			return null;
		}

		return (Long) result.get(0).get("count(1)");
	}

	public Long countNewMsgs(String userId) {
		String sql = "select count(1) from internalmsg where userId = ? and state = 0";
		Object[] params = new Object[] { userId };
		int[] types = new int[] { Types.VARCHAR };
		List<Map<String, Object>> result = null;
		result = jdbcBase.query(sql, params, types);

		if (result == null || result.size() == 0) {
			return null;
		}

		return (Long) result.get(0).get("count(1)");
	}
}
