package com.wecode.game.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.wecode.game.exception.FBException;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;

public class ExceptionFilter implements Filter {

	private static Logger log = Logger.getLogger(ExceptionFilter.class);
	private static String LOL_FORMAT_KNOWN = "请求 %s 发生错误。错误代码:%5d,错误信息: %s ,异常信息：";
	private static String LOL_FORMAT_UNKNOWN = "请求 %s 发生未知错误,异常信息：";

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		try {
			// 此处将后面可能所有的异常进行拦截
			chain.doFilter(request, servletResponse);
		} catch (Exception exception) {
			// 仅针对已知的异常进行专门的返回值处理，其他的认为是不可知的异常。
			String path = servletRequest.getPathInfo();
			if (path == null) {
				path = servletRequest.getServletPath();
			}

			if (exception.getCause() != null && exception.getCause() instanceof FBException) {
				FBException e = (FBException) exception.getCause();
				SrvCommonOpr.setRspResultForError(servletResponse, e.getErrorMsg(),
						e.getErrorCode());
				log.error(String.format(LOL_FORMAT_KNOWN, path, e.getErrorCode(), e.getErrorMsg()),
						e);
				return;
			} else {
				SrvCommonOpr.setRspResultForError(servletResponse, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
				log.error(String.format(LOL_FORMAT_UNKNOWN, path), exception);
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
