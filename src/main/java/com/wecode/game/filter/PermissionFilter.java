package com.wecode.game.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.wecode.game.bean.Permission;
import com.wecode.game.bean.User;
import com.wecode.game.bean.UserLog;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.service.SecurityService;
import com.wecode.game.service.UserLogService;
import com.wecode.game.service.UserService;

public class PermissionFilter implements Filter {

	private List<Permission> permissions;// 权限路径+访问方式-对应的权限
	private Map<Integer, List<Long>> rolePermission;// 角色Id-对应的权限Id
	private UserService userService;
	private UserLogService userLogService;

	@Override
	public void destroy() {
		this.permissions = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		String path = servletRequest.getPathInfo();
		if (path == null) {
			path = servletRequest.getServletPath();
		}
		Permission p = this.getPermission(path, servletRequest.getMethod());
		if (p == null || !(p.needCheck != null && p.needCheck)) {
			chain.doFilter(request, response);
			return;
		}
		String userId = servletRequest.getHeader("uuid");
		User user = userService.getUserByUuid(userId);
		if (user == null) {
			throw new ServletException(new PermissionNotEnoughException("guest", p.desc, p.id));
		}
		int operateState = UserLog.LOG_STATE_FAILED;
		try {
			if (!this.hasPermission(user.role, p.id)) {
				throw new ServletException(new PermissionNotEnoughException(user.username, p.desc,
						p.id));
			}
			chain.doFilter(request, response);
			operateState = UserLog.LOG_STATE_SUCCEED;
		} finally {
			try {
				if (p.log != null && p.log) {
					UserLog log = new UserLog();
					log.time = System.currentTimeMillis();
					log.ip = this.getIpAddr(servletRequest);
					log.userId = servletRequest.getHeader("uuid");
					log.username = user.username;
					log.pid = p.id;
					log.role = user.role;
					log.content = p.desc;
					log.state = operateState;
					userLogService.addLog(log);
				}
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}

	private Permission getPermission(String path, String method) {
		if (path == null || method == null) {
			return null;
		}
		for (Permission p : permissions) {
			if (path.matches(p.path) && method.equalsIgnoreCase(p.method))
				return p;
		}
		return null;
	}

	private boolean hasPermission(Integer role, Long id) {
		if (rolePermission.get(role) == null) {
			return false;
		}
		for (Long p : rolePermission.get(role)) {
			if (p == id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext context = filterConfig.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
		SecurityService securityService = (SecurityService) ctx.getBean("securityService");
		userService = (UserService) ctx.getBean("userService");
		userLogService = (UserLogService) ctx.getBean("userLogService");

		permissions = securityService.getAllPermission();
		rolePermission = securityService.getRolePermission();
	}

	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader(" x-forwarded-for ");
		if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
			ip = request.getHeader(" Proxy-Client-IP ");
		}
		if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
			ip = request.getHeader(" WL-Proxy-Client-IP ");
		}
		if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
