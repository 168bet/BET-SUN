package com.wecode.game.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.Manager;
import com.wecode.game.bean.ResetPwd;
import com.wecode.game.bean.User;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.service.ManageService;
import com.wecode.game.service.UserService;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;

@Controller
public class ManagerController
{
	private Gson gson = new GsonBuilder().serializeNulls().create();
	@Resource(name = "managerService")
	private ManageService managerService;
	@Resource(name = "userService")
	private UserService userService;
	
	@RequestMapping(value = "/manager/{uuid}", method = RequestMethod.GET)
	public void getManagerByUuid(HttpServletRequest request, HttpServletResponse response, @PathVariable String uuid)
	{
		try
		{
			User manager = managerService.getManagerByUuid(uuid);		
		
			SrvCommonOpr.setRspResultForJson(response, manager, HTTP_RESPONCES_INFO.SUCCESS);
		}
		catch ( Exception e )
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG, HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
	
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public void getManagerList(HttpServletRequest request, HttpServletResponse response)
	{
		String numStr = request.getParameter("num");
		Integer num = null;
		if (numStr != null)
		{
			num = Integer.parseInt(numStr);
		}

		String pageStr = request.getParameter("page");
		Integer page = null;
		if (pageStr != null)
		{
			page = Integer.parseInt(pageStr);
		}
		
		String roleStr = request.getParameter("role");
		Integer role = null;
		if (roleStr != null)
		{
			role = Integer.parseInt(roleStr);
		}
		
		ListResult<User> result;
		
		try
		{
			result = managerService.getManagerListOfUser(role, num, page);
			SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
		}
		catch ( PermissionNotEnoughException e )
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG, HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);
		}
		catch ( Exception e )
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG, HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
	
	@RequestMapping(value = "/manager", method = RequestMethod.POST)
	public void addManagerList(HttpServletRequest request, HttpServletResponse response, @RequestBody String json)
	{
		Manager manager = gson.fromJson(json, Manager.class);
		
		try
		{
			if ( userService.checkUsername(manager.username) )
			{
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.USERNAME_IS_EXIST_MSG, HTTP_RESPONCES_INFO.USERNAME_IS_EXIST);
				return ;
			}
			
			if ( managerService.addManager(manager) )
			{
				SrvCommonOpr.setRspResultForJson(response, manager, HTTP_RESPONCES_INFO.SUCCESS);
			}
			else
			{
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG, HTTP_RESPONCES_INFO.UNKOWN);
			}
		} 
		catch (PermissionNotEnoughException e)
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG, HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);	
		}
		catch ( Exception e )
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG, HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
	
	@RequestMapping(value = "/manager/resetLoginPwd", method = RequestMethod.PUT)
	public void resetLoginPwd(HttpServletRequest request, HttpServletResponse response, @RequestBody String json) throws Exception
	{
		ResetPwd resetPwd = gson.fromJson(json,ResetPwd.class);
		resetPwd.currentUuid = request.getHeader("uuid");
		resetPwd.currentRole = Integer.parseInt(request.getHeader("role"));
		if ( managerService.resetLoginPwdForUser(resetPwd) )
		{
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		}
	}
	
	@RequestMapping(value = "/manager/resetMoneyPwd", method = RequestMethod.PUT)
	public void resetMoneyPwd(HttpServletRequest request, HttpServletResponse response, @RequestBody String json) throws Exception
	{
		ResetPwd resetPwd = gson.fromJson(json,ResetPwd.class);
		resetPwd.currentUuid = request.getHeader("uuid");
		resetPwd.currentRole = Integer.parseInt(request.getHeader("role"));
		if ( managerService.resetMoneyPwdForUser(resetPwd) )
		{
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.ROLE_NOT_RIGHT_MSG, HTTP_RESPONCES_INFO.ROLE_NOT_RIGHT);
		}
	}
	
	@RequestMapping(value = "/manager/{uuid}", method = RequestMethod.PUT)
	public void modifyManagerList(HttpServletRequest request, HttpServletResponse response, @RequestBody String json, @PathVariable String uuid)
	{
		Manager manager = gson.fromJson(json, Manager.class);
		manager.uuid = uuid;
		if ( managerService.modifyManager(manager) )
		{
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		}
		else
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG, HTTP_RESPONCES_INFO.UNKOWN);	
		}
	}
	
	@RequestMapping(value = "/manager/{uuid}", method = RequestMethod.DELETE)
	public void deleteManagerList(HttpServletRequest request, HttpServletResponse response, @PathVariable String uuid)
	{
		if ( managerService.deleteManager(uuid) )
		{
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		}
		else
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG, HTTP_RESPONCES_INFO.UNKOWN);	
		}
	}
}
