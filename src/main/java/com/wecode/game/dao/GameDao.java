package com.wecode.game.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wecode.game.bean.Game;
import com.wecode.game.exception.IntConvertBoolException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.util.MapConvertBeanUtil;
import com.wecode.game.util.SpringJdbc;

@Repository("gameDao")
public class GameDao {

	@Resource
	private SpringJdbc jdbcBase;
	private static Logger log = Logger.getLogger(GameDao.class);

	public Game getGameByTypeAndID(String type, String id) throws ResultToBeanException {
		String sql = "select * from game where type=? and id = ?";
		Object[] params = new Object[] { type, id };
		int[] types = new int[] { Types.VARCHAR, Types.INTEGER };
		List<Map<String, Object>> result = null;

		result = jdbcBase.query(sql, params, types);

		if (result.size() != 1) {
			return null;
		}

		try {
			return MapConvertBeanUtil.resultToBean(result.get(0), Game.class);
		} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
				| IntConvertBoolException e) {
			log.error("getGameByTypeAndID: change result to bean fail. Exception message: "
					+ e.getMessage());
			throw new ResultToBeanException();
		}
	}
	
	public Game getGameById(Long id) throws ResultToBeanException {
		String sql = "select * from game where id = ?";
		Object[] params = new Object[] { id };
		int[] types = new int[] { Types.INTEGER };
		List<Map<String, Object>> result = null;

		result = jdbcBase.query(sql, params, types);

		if (result.size() != 1) {
			return null;
		}

		try {
			return MapConvertBeanUtil.resultToBean(result.get(0), Game.class);
		} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
				| IntConvertBoolException e) {
			log.error("getGameByID: change result to bean fail. Exception message: "
					+ e.getMessage());
			throw new ResultToBeanException();
		}
	}

	public boolean addGame(String type, Game game) {
		String sql = "insert into game (type, name, link, image, enable) "
				+ "values(?,?,?,?,?)";
		Object[] params = new Object[] { type, game.name, game.link, game.image, game.enable };

		int optResult = jdbcBase.update(sql, params);

		if (optResult < 0) {
			log.error("addGame: add game table error");
			return false;
		}
		return true;
	}

	public boolean deleteGame(String type, String id) {
		String sql = "delete from game where type = ? and id = ?";

		int optResult = jdbcBase.update(sql, new Object[] { type, id });
		if (optResult < 0) {
			log.error("deleteGame: delete game(type:" + type + ",id:" + id + ") error");
			return false;
		}
		return true;
	}

	public boolean modifyGame(String type, Game game) {
		String sql = "update game set ";
		List<Object> paramsList = new ArrayList<Object>();
		if (game.image != null) {
			sql = sql + " image= ?,";
			paramsList.add(game.image);
		}
		if (game.link != null) {
			sql = sql + " link = ?,";
			paramsList.add(game.link);
		}
		if (game.enable != null) {
			sql = sql + " enable = ?,";
			paramsList.add(game.enable);
		}
		if (game.name != null) {
			sql = sql + " name = ?,";
			paramsList.add(game.name);
		}
		if (paramsList.size() < 1) {
			return false;
		}
		sql = sql.substring(0, sql.length() - 1);
		sql = sql + " where id = ? and type = ?";
		paramsList.add(game.id);
		paramsList.add(type);

		if (jdbcBase.update(sql, paramsList.toArray()) < 0) {
			log.error("modifyGame: update gmae error");
			return false;
		}
		return true;
	}

	public List<Game> getAllGames(String type, Integer num, Integer page) {
		String sql = "select * from game where type = ?";
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

		List<Game> games = new ArrayList<Game>();
		for (Map<String, Object> object : result) {
			try {
				games.add(MapConvertBeanUtil.resultToBean(object, Game.class));
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
					| IntConvertBoolException e) {
				log.error("getAllGames: change result to bean fail. Exception message: "
						+ e.getMessage());
				continue;
			}
		}
		return games;
	}

	public Long countGames(String type) {
		String sql = "select count(1) from game where type = ?";
		Object[] params = new Object[] { type };
		int[] types = new int[] { Types.VARCHAR };
		List<Map<String, Object>> result = null;
		result = jdbcBase.query(sql, params, types);

		if (result == null || result.size() == 0) {
			return null;
		}

		return (Long) result.get(0).get("count(1)");
	}


	public List<Game> getEnableGames(String type, Integer num, Integer page) {
		String sql = "select * from game where enable = 1 and type = ?";
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

		List<Game> games = new ArrayList<Game>();
		for (Map<String, Object> object : result) {
			try {
				games.add(MapConvertBeanUtil.resultToBean(object, Game.class));
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
					| IntConvertBoolException e) {
				log.error("getAllGames: change result to bean fail. Exception message: "
						+ e.getMessage());
				continue;
			}
		}
		return games;
	}

	public Long countEnableGames(String type) {
		String sql = "select count(1) from game where enable = 1 and type = ?";
		Object[] params = new Object[] { type };
		int[] types = new int[] { Types.VARCHAR };
		List<Map<String, Object>> result = null;
		result = jdbcBase.query(sql, params, types);

		if (result == null || result.size() == 0) {
			return null;
		}
		return (Long) result.get(0).get("count(1)");
	}
	public boolean enableGame(String type, String id, boolean enable) {
		String sql = null;
		if (enable) {
			sql = "update game set enable = 1 where type = ? and id = ?";
		} else {
			sql = "update game set enable = 0 where type = ? and id = ?";
		}
		Object[] params = new Object[] { type, id };

		if (jdbcBase.update(sql, params) < 0) {
			log.error("disableGame: disable game(type:" + type + ",id:" + id + ") error");
			return false;
		}
		return true;
	}
}
