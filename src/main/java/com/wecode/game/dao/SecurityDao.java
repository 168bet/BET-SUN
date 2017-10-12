package com.wecode.game.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wecode.game.bean.Permission;
import com.wecode.game.exception.IntConvertBoolException;
import com.wecode.game.util.MapConvertBeanUtil;
import com.wecode.game.util.SpringJdbc;

@Repository("securityDao")
public class SecurityDao {
	@Resource
	private SpringJdbc jdbcBase;
	private static Logger log = Logger.getLogger(SecurityDao.class);

	public List<Permission> getAllPermission() {
		String sql = "select * from permission ORDER BY path ";
		Object[] params = new Object[] {};
		int[] types = new int[] {};
		List<Map<String, Object>> result = null;

		result = jdbcBase.query(sql, params, types);
		if (result == null || result.size() == 0) {
			return null;
		}

		List<Permission> permissions = new ArrayList<Permission>();
		for (Map<String, Object> object : result) {
			try {
				permissions.add(MapConvertBeanUtil.resultToBean(object, Permission.class));
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
					| IntConvertBoolException e) {
				log.error("getAllPermission: change result to bean fail. Exception message: "
						+ e.getMessage());
				continue;
			}
		}
		return permissions;
	}

	public Map<Integer, List<Long>> getRolePermission() {
		String sql = "select * from role_permission";
		List<Map<String, Object>> result = jdbcBase.query(sql);
		if (result == null || result.size() == 0) {
			return new HashMap<Integer, List<Long>>();
		}
		Map<Integer, List<Long>> rolePermission = new HashMap<Integer, List<Long>>();
		for (Map<String, Object> object : result) {
			Integer roleId = (Integer) object.get("roleId");
			Long permissionId = (Long) object.get("permissionId");
			if (rolePermission.get(roleId) == null) {
				rolePermission.put(roleId, new ArrayList<Long>());
			}
			rolePermission.get(roleId).add(permissionId);
		}
		return rolePermission;
	}
}