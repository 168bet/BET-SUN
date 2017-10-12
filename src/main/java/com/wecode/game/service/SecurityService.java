package com.wecode.game.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wecode.game.bean.Permission;
import com.wecode.game.dao.SecurityDao;

@Service("securityService")
public class SecurityService {

	@Resource(name = "securityDao")
	private SecurityDao securityDao;

	public List<Permission> getAllPermission() {
		return securityDao.getAllPermission();
	}

	public Map<Integer, List<Long>> getRolePermission() {
		return securityDao.getRolePermission();
	}
}
