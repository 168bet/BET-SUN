package com.wecode.game.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.wecode.game.bean.Agent;
import com.wecode.game.bean.AgentInfo;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.PasswordModify;
import com.wecode.game.bean.User;
import com.wecode.game.exception.CommonException;
import com.wecode.game.exception.MoneyPasswordIncorrectException;
import com.wecode.game.exception.ParamMissingException;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.exception.UserNotExistException;
import com.wecode.game.identified.Role;
import com.wecode.game.service.AgentService;
import com.wecode.game.service.UserService;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;

@Controller
public class AgentController
{
	private Gson gson = new GsonBuilder().serializeNulls().create();
	@Resource(name = "agentService")
	private AgentService agentService;
	
	@Resource(name = "userService")
	private UserService userService;
	
	@RequestMapping(value = "/agent/tree", method = RequestMethod.GET)
	public void getAgentTreeByUuid(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String uuid = request.getParameter("uuid");
			if ( uuid == null )
			{
				uuid = request.getParameter("originid");
				User user = userService.getUserByUuid(uuid);
				if ( user == null )
				{
					SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.USER_NOT_EXIST_MSG, HTTP_RESPONCES_INFO.USER_NOT_EXIST);
					return ;
				}
				
				if ( user.role < Role.AGENT )
				{
					List<AgentInfo> agentInfoList = new ArrayList<AgentInfo>();
					ListResult<Agent> result = agentService.getAgentListOfUser(uuid, null, null);
					agentInfoList.add(new AgentInfo(user.uuid, user.username, result.count));
					SrvCommonOpr.setRspResultForJson(response, agentInfoList, HTTP_RESPONCES_INFO.SUCCESS);
					return ;
				}
				Agent agent = agentService.getAgentByUuid(uuid);
				List<AgentInfo> agentInfoList = new ArrayList<AgentInfo>();
				agentInfoList.add(new AgentInfo(agent.uuid, agent.username, agent.agentNum));
				
				SrvCommonOpr.setRspResultForJson(response, agentInfoList, HTTP_RESPONCES_INFO.SUCCESS);
				return ;
			}
			ListResult<Agent> result;

			result = agentService.getAgentListOfUser(uuid, null, null);
			
			List<AgentInfo> agentInfoList = new ArrayList<AgentInfo>();
			
			for (Agent agent : result.list)
			{
				AgentInfo agentInfo = new AgentInfo(agent.uuid, agent.username, agent.agentNum);
				agentInfoList.add(agentInfo);
			}

			SrvCommonOpr.setRspResultForJson(response, agentInfoList, HTTP_RESPONCES_INFO.SUCCESS);
		}
		catch (PermissionNotEnoughException e1)
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG, HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);
		} 
		catch (UserNotExistException e1)
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.USER_NOT_EXIST_MSG, HTTP_RESPONCES_INFO.USER_NOT_EXIST);
		} 
		catch (Exception e1)
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG, HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	@RequestMapping(value = "/agent/{uuid}", method = RequestMethod.GET)
	public void getAgentByUuid(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String uuid)
	{
		Agent agent = null;
		try
		{
			agent = agentService.getAgentByUuid(uuid);
		} catch (ResultToBeanException e)
		{
			SrvCommonOpr
					.setRspResultForError(response,
							HTTP_RESPONCES_INFO.UNKNOWN_MSG,
							HTTP_RESPONCES_INFO.UNKOWN);
			return;
		}
		if (agent == null)
		{
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.NOT_FIND_AGENT_MSG,
					HTTP_RESPONCES_INFO.NOT_FIND_AGENT);
			return;
		}
		SrvCommonOpr.setRspResultForJson(response, agent,
				HTTP_RESPONCES_INFO.SUCCESS);
	}

	// 获取下线代理列表
	@RequestMapping(value = "/user/{uuid}/agent", method = RequestMethod.GET)
	public void getAgentListOfUser(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String uuid)
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

		try
		{
			ListResult<Agent> result;

			result = agentService.getAgentListOfUser(uuid, num, page);

			SrvCommonOpr.setRspResultForJson(response, result,
					HTTP_RESPONCES_INFO.SUCCESS);
		} catch (PermissionNotEnoughException e1)
		{
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);
		} catch (UserNotExistException e1)
		{
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.USER_NOT_EXIST_MSG,
					HTTP_RESPONCES_INFO.USER_NOT_EXIST);
		} catch (Exception e1)
		{
			SrvCommonOpr
					.setRspResultForError(response,
							HTTP_RESPONCES_INFO.UNKNOWN_MSG,
							HTTP_RESPONCES_INFO.UNKOWN);
		}

	}

	// 新增代理列表
	@RequestMapping(value = "/agent", method = RequestMethod.POST)
	public void addAgent(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String json)
	{
		Agent agent = gson.fromJson(json, Agent.class);
		try
		{
			if ( userService.checkUsername(agent.username) )
			{
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.USERNAME_IS_EXIST_MSG, HTTP_RESPONCES_INFO.USERNAME_IS_EXIST);
				return ;
			}
			if (agentService.addAgent(agent))
			{
				SrvCommonOpr.setRspResultForJson(response, agent,
						HTTP_RESPONCES_INFO.SUCCESS);
			} else
			{
				SrvCommonOpr
						.setRspResultForError(response,
								HTTP_RESPONCES_INFO.UNKNOWN_MSG,
								HTTP_RESPONCES_INFO.UNKOWN);
			}
		}
		catch (Exception e)
		{
			SrvCommonOpr
			.setRspResultForError(response,
					HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);	
		}
	}

	// 修改代理信息
	@RequestMapping(value = "/agent/{uuid}", method = RequestMethod.PUT)
	public void modifyAgent(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String uuid,
			@RequestBody String json)
	{
		Agent agent = gson.fromJson(json, Agent.class);
		agent.uuid = uuid;

		if (agentService.modifyAgent(agent))
		{
			SrvCommonOpr.setRspResultForJson(response, null,
					HTTP_RESPONCES_INFO.SUCCESS);
		} else
		{
			SrvCommonOpr
					.setRspResultForError(response,
							HTTP_RESPONCES_INFO.UNKNOWN_MSG,
							HTTP_RESPONCES_INFO.UNKOWN);
		}

	}

	// 删除代理信息
	@RequestMapping(value = "/agent/{uuid}", method = RequestMethod.DELETE)
	public void deleteAgent(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String uuid)
	{
		if (agentService.delete(uuid))
		{
			SrvCommonOpr.setRspResultForJson(response, null,
					HTTP_RESPONCES_INFO.SUCCESS);
		} else
		{
			SrvCommonOpr
					.setRspResultForError(response,
							HTTP_RESPONCES_INFO.UNKNOWN_MSG,
							HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 设定代理是否只查账
	@RequestMapping(value = "/agent/{uuid}/onlyRead", method = RequestMethod.PUT)
	public void modifyAgentOnlyRead(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String uuid,
			@RequestBody String json)
	{
		try
		{
			if ( agentService.modifyAgentOnlyRead(uuid) )
			{
				SrvCommonOpr.setRspResultForJson(response, null,
						HTTP_RESPONCES_INFO.SUCCESS);
			} else
			{
				SrvCommonOpr
						.setRspResultForError(response,
								HTTP_RESPONCES_INFO.UNKNOWN_MSG,
								HTTP_RESPONCES_INFO.UNKOWN);
			}
		} 
		catch (ResultToBeanException e)
		{
			SrvCommonOpr
			.setRspResultForError(response,
					HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}

	}

	// 修改密码
	@RequestMapping(value = "/agent/moneypassword", method = RequestMethod.PUT)
	public void modifyPassword(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json) throws CommonException {
		PasswordModify password = gson.fromJson(json, PasswordModify.class);
		password.userId = request.getHeader("uuid");
		if (password.preMoneyPswd == null || password.newMoneyPswd == null) {
			throw new ParamMissingException("preMoneyPswd or newMoneyPswd");
		}
		try {
			if (agentService.modifyMoneyPassword(password)) {
				SrvCommonOpr.setRspResultForJson(response, "success", HTTP_RESPONCES_INFO.SUCCESS);
			} else {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (MoneyPasswordIncorrectException e) {
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.MONEY_PASSWORD_INCORRECT_MSG,
					HTTP_RESPONCES_INFO.MONEY_PASSWORD_INCORRECT);
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
}
