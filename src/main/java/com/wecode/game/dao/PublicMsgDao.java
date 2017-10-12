package com.wecode.game.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wecode.game.bean.PublicMsg;
import com.wecode.game.exception.IntConvertBoolException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.util.MapConvertBeanUtil;
import com.wecode.game.util.SpringJdbc;

@Repository("publicMsgDao")
public class PublicMsgDao {

	@Resource
	private SpringJdbc jdbcBase;
	private static Logger log = Logger.getLogger(PublicMsgDao.class);

	public PublicMsg getMsgByTypeAndID(String type, String id) throws ResultToBeanException {
		String sql = "select * from publicmsg where type=? and id = ?";
		Object[] params = new Object[] { type, id };
		int[] types = new int[] { Types.VARCHAR, Types.INTEGER };
		List<Map<String, Object>> result = null;

		result = jdbcBase.query(sql, params, types);

		if (result.size() != 1) {
			return null;
		}

		try {
			return MapConvertBeanUtil.resultToBean(result.get(0), PublicMsg.class);
		} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
				| IntConvertBoolException e) {
			log.error("getMsgByTypeAndID: change result to bean fail. Exception message: "
					+ e.getMessage());
			throw new ResultToBeanException();
		}
	}

	public PublicMsg getLatestMsgs(String type) throws ResultToBeanException {
		String sql = "select * from publicmsg where type=? order by createTime desc limit 1";
		Object[] params = new Object[] { type };
		int[] types = new int[] { Types.VARCHAR };
		List<Map<String, Object>> result = null;

		result = jdbcBase.query(sql, params, types);

		if (result == null || result.size() != 1) {
			return null;
		}

		try {
			return MapConvertBeanUtil.resultToBean(result.get(0), PublicMsg.class);
		} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
				| IntConvertBoolException e) {
			log.error("getMsgByTypeAndID: change result to bean fail. Exception message: "
					+ e.getMessage());
			throw new ResultToBeanException();
		}
	}

	public boolean addMsg(String type, PublicMsg msg) {
		String sql = "insert into publicmsg (type, content, createTime,publisher) "
				+ "values(?,?,?,?)";
		Object[] params = new Object[] { type, msg.content, msg.createTime, msg.publisher };

		int optResult = jdbcBase.update(sql, params);

		if (optResult < 0) {
			log.error("addMsg: add publicmsg table error");
			return false;
		}
		return true;
	}

	public boolean deleteMsg(String type, String id) {
		String sql = "delete from publicmsg where type = ? and id = ?";

		int optResult = jdbcBase.update(sql, new Object[] { type, id });
		if (optResult < 0) {
			log.error("deleteMsg: delete publicMsg(type:" + type + ",id:" + id + ") error");
			return false;
		}
		return true;
	}

	public boolean modifyMsg(String type, PublicMsg msg) {
		String sql = "update publicmsg set ";
		List<Object> paramsList = new ArrayList<Object>();
		if (msg.content != null) {
			sql = sql + " content= ?,";
			paramsList.add(msg.content);
		}
		if (msg.createTime != null) {
			sql = sql + " createTime = ?,";
			paramsList.add(msg.createTime);
		}
		if (msg.publisher != null) {
			sql = sql + " publisher = ?,";
			paramsList.add(msg.publisher);
		}
		if (paramsList.size() < 1) {
			return false;
		}
		sql = sql.substring(0, sql.length() - 1);
		sql = sql + " where id = ? and type = ?";
		paramsList.add(msg.id);
		paramsList.add(type);

		if (jdbcBase.update(sql, paramsList.toArray()) < 0) {
			log.error("modifyMsg: update publicmsg error");
			return false;
		}
		return true;
	}

	public List<PublicMsg> getAllMsgs(String type, Integer num, Integer page) {
		String sql = "select * from publicmsg where type = ?";
		Object[] params = new Object[] { type };
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

		List<PublicMsg> msgs = new ArrayList<PublicMsg>();
		for (Map<String, Object> object : result) {
			try {
				msgs.add(MapConvertBeanUtil.resultToBean(object, PublicMsg.class));
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
					| IntConvertBoolException e) {
				log.error("getAllMsgs: change result to bean fail. Exception message: "
						+ e.getMessage());
				continue;
			}
		}
		return msgs;
	}

	public Long countMsgs(String type) {
		String sql = "select count(1) from publicmsg where type = ?";
		Object[] params = new Object[] { type };
		int[] types = new int[] { Types.VARCHAR };
		List<Map<String, Object>> result = null;
		result = jdbcBase.query(sql, params, types);

		if (result == null || result.size() == 0) {
			return null;
		}

		return (Long) result.get(0).get("count(1)");
	}
}
