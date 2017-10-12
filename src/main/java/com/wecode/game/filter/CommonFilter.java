package com.wecode.game.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;

import com.wecode.game.bean.User;
import com.wecode.game.util.CurrentOperatorUtil;


public class CommonFilter implements Filter
{
	private static final String ADMINID               = "adminId";
	private static final String USERID             	  = "userId";
    private static final String SECRETKEY             = "sk";
	
    
    
	@Override
	public void destroy()
	{
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest servletRequest = (HttpServletRequest)request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		servletRequest.setCharacterEncoding("UTF-8");
		servletResponse.setCharacterEncoding("UTF-8");
		servletResponse.setHeader("Access-Control-Allow-Origin", "*");
		servletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type," + ADMINID + "," + USERID + "," + SECRETKEY + ",Accept");
		servletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
		if ( HttpMethod.OPTIONS.toString().equals(servletRequest.getMethod())  )
		{
			return ;
		}
		chain.doFilter(request, servletResponse);
	}
}
