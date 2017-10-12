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
import com.wecode.game.bean.Agent;
import com.wecode.game.bean.Child;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.Member;
import com.wecode.game.exception.AgentNotExistException;
import com.wecode.game.exception.ChildNotExistException;
import com.wecode.game.exception.FBException;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.service.AgentService;
import com.wecode.game.service.ChildService;
import com.wecode.game.service.MemberService;
import com.wecode.game.service.UserService;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;

@Controller
public class ChildController {
	private Gson gson = new GsonBuilder().serializeNulls().create();
	@Resource(name = "childService")
	private ChildService childService;
	@Resource(name = "agentService")
	private AgentService agentService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "memberService")
	private MemberService memberService;

	@RequestMapping(value = "/child/{uuid}", method = RequestMethod.GET)
	public void getChildByUuid(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) {
		Child child = null;
		try {
			child = childService.getChildByUuid(uuid);
		} catch (ResultToBeanException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
			return;
		}
		if (child == null) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.NOT_FIND_AGENT_MSG,
					HTTP_RESPONCES_INFO.NOT_FIND_AGENT);
			return;
		}
		SrvCommonOpr.setRspResultForJson(response, child, HTTP_RESPONCES_INFO.SUCCESS);
	}

	// 获取下线子账号
	@RequestMapping(value = "/user/{uuid}/child", method = RequestMethod.GET)
	public void getChildListOfUser(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) throws FBException {
		String numStr = request.getParameter("num");
		Integer num = null;
		if (numStr != null) {
			num = Integer.parseInt(numStr);
		}

		String pageStr = request.getParameter("page");
		Integer page = null;
		if (pageStr != null) {
			page = Integer.parseInt(pageStr);
		}

		try {
			ListResult<Child> result = childService.getChildListOfUser(uuid, num, page);

			SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
		} catch (PermissionNotEnoughException e1) {
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);
		} catch (ResultToBeanException e1) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
	
	// 获取下线子账号
	@RequestMapping(value = "/child/{uuid}/child", method = RequestMethod.GET)
	public void getChildListOfChild(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) throws FBException {
		String numStr = request.getParameter("num");
		Integer num = null;
		if (numStr != null) {
			num = Integer.parseInt(numStr);
		}

		String pageStr = request.getParameter("page");
		Integer page = null;
		if (pageStr != null) {
			page = Integer.parseInt(pageStr);
		}

		try {
			Child child = childService.getChildByUuid(uuid);
			if (child == null) {
				throw new ChildNotExistException();
			}
			Agent agent = agentService.getAgentByUuid(child.parentId);
			if (agent == null) {
				throw new AgentNotExistException();
			}
			ListResult<Child> result = childService.getChildListOfUser(agent.uuid, num, page);

			SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
		} catch (PermissionNotEnoughException e1) {
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);
		} catch (ResultToBeanException e1) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 获取下线子账号
	@RequestMapping(value = "/child/{uuid}/agent", method = RequestMethod.GET)
	public void getAgentListOfChild(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) throws Exception {
		String numStr = request.getParameter("num");
		Integer num = null;
		if (numStr != null) {
			num = Integer.parseInt(numStr);
		}

		String pageStr = request.getParameter("page");
		Integer page = null;
		if (pageStr != null) {
			page = Integer.parseInt(pageStr);
		}

		try {
			Child child = childService.getChildByUuid(uuid);
			if (child == null) {
				throw new ChildNotExistException();
			}
			Agent agent = agentService.getAgentByUuid(child.parentId);
			if (agent == null) {
				throw new AgentNotExistException();
			}
			ListResult<Agent> result = agentService.getAgentListOfUser(agent.uuid, num, page);

			SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
		} catch (PermissionNotEnoughException e1) {
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);
		} catch (ResultToBeanException e1) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 获取下线子账号
	@RequestMapping(value = "/child/{uuid}/member", method = RequestMethod.GET)
	public void getMemberListOfChild(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) throws Exception {
		String numStr = request.getParameter("num");
		Integer num = null;
		if (numStr != null) {
			num = Integer.parseInt(numStr);
		}

		String pageStr = request.getParameter("page");
		Integer page = null;
		if (pageStr != null) {
			page = Integer.parseInt(pageStr);
		}

		try {
			Child child = childService.getChildByUuid(uuid);
			if (child == null) {
				throw new ChildNotExistException();
			}
			Agent agent = agentService.getAgentByUuid(child.parentId);
			if (agent == null) {
				throw new AgentNotExistException();
			}
			ListResult<Member> result = memberService.getMemberListOfUser(agent.uuid, num, page);

			SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
		} catch (PermissionNotEnoughException e1) {
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);
		} catch (ResultToBeanException e1) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 新增代理
	@RequestMapping(value = "/child", method = RequestMethod.POST)
	public void addChild(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json) {
		Child child = gson.fromJson(json, Child.class);
		try {
			if (userService.checkUsername(child.username)) {
				SrvCommonOpr.setRspResultForError(response,
						HTTP_RESPONCES_INFO.USERNAME_IS_EXIST_MSG,
						HTTP_RESPONCES_INFO.USERNAME_IS_EXIST);
				return;
			}
			if (childService.addChild(child)) {
				SrvCommonOpr.setRspResultForJson(response, child, HTTP_RESPONCES_INFO.SUCCESS);
			} else {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 修改子账号信息
	@RequestMapping(value = "/child/{uuid}", method = RequestMethod.PUT)
	public void modifyChild(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid, @RequestBody String json) {
		Child child = gson.fromJson(json, Child.class);
		child.uuid = uuid;

		if (childService.modifyChild(child)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 删除代理信息
	@RequestMapping(value = "/child/{uuid}", method = RequestMethod.DELETE)
	public void deleteChild(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) {
		if (childService.delete(uuid)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
}
