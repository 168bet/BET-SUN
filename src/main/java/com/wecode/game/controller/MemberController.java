package com.wecode.game.controller;

import java.util.ArrayList;

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
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.Login;
import com.wecode.game.bean.Member;
import com.wecode.game.bean.MemberTreeNode;
import com.wecode.game.bean.PasswordModify;
import com.wecode.game.exception.AgentNotExistException;
import com.wecode.game.exception.ParamMissingException;
import com.wecode.game.exception.PasswordIncorrectException;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.PwdErrorThanFiveException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.exception.UserIsLockedException;
import com.wecode.game.exception.UserIsStoppedException;
import com.wecode.game.exception.UsernameNotExistException;
import com.wecode.game.service.AgentService;
import com.wecode.game.service.CaptchaService;
import com.wecode.game.service.MemberService;
import com.wecode.game.service.UserService;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;

@Controller
public class MemberController {
	private Gson gson = new GsonBuilder().serializeNulls().create();
	@Resource(name = "memberService")
	private MemberService memberService;
	@Resource(name = "agentService")
	private AgentService agentService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "captchaService")
	private CaptchaService captchaService;

	// 登录
	@RequestMapping(value = "/member/login", method = RequestMethod.POST)
	public void login(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String loginJson) {
		Login login = gson.fromJson(loginJson, Login.class);
		Member member = null;
		try {
			member = memberService.login(login);
		} catch (UsernameNotExistException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.USERNAME_NOT_EXIST_MSG,
					HTTP_RESPONCES_INFO.USERNAME_NOT_EXIST);
			return;
		} catch (PasswordIncorrectException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.PASSWORD_INCORRECT_MSG,
					HTTP_RESPONCES_INFO.PASSWORD_INCORRECT);
			return;
		} catch (UserIsLockedException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.USER_IS_LOCKED_MSG,
					HTTP_RESPONCES_INFO.USER_IS_LOCKED);
			return;
		} catch (UserIsStoppedException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.USER_IS_STOPPED_MSG,
					HTTP_RESPONCES_INFO.USER_IS_STOPPED);
			return;
		} catch (PwdErrorThanFiveException e) {
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.PWD_ERROR_THAN_FIVE_MSG,
					HTTP_RESPONCES_INFO.PWD_ERROR_THAN_FIVE);
			return;
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
			return;
		}
		SrvCommonOpr.setRspResultForJson(response, member, HTTP_RESPONCES_INFO.SUCCESS);
		return;
	}

	@RequestMapping(value = "/member/{uuid}", method = RequestMethod.GET)
	public void getMemberByUuid(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) {
		Member member = null;
		try {
			member = memberService.getMemberByUuid(uuid);
		} catch (ResultToBeanException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
			return;
		}
		if (member == null) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.NOT_FIND_AGENT_MSG,
					HTTP_RESPONCES_INFO.NOT_FIND_AGENT);
			return;
		}
		SrvCommonOpr.setRspResultForJson(response, member, HTTP_RESPONCES_INFO.SUCCESS);
	}

	// 获取下线会员列表
	@RequestMapping(value = "/user/{uuid}/member", method = RequestMethod.GET)
	public void getMemberListOfUser(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) {
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
			ListResult<Member> result = memberService.getMemberListOfUser(uuid, num, page);

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

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public void register(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json) {
		Member member = gson.fromJson(json, Member.class);
		try {
			if (userService.checkUsername(member.username)) {
				SrvCommonOpr.setRspResultForError(response,
						HTTP_RESPONCES_INFO.USERNAME_IS_EXIST_MSG,
						HTTP_RESPONCES_INFO.USERNAME_IS_EXIST);
				return;
			}
			if (!captchaService.check(request.getSession().getId(), member.captcha)) {

				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.CAPTCHA_INCRECT_MSG,
						HTTP_RESPONCES_INFO.CAPTCHA_INCRECT);
				return;
			}

			if (memberService.register(member)) {
				SrvCommonOpr.setRspResultForJson(response, member, HTTP_RESPONCES_INFO.SUCCESS);
				return;
			} else {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (AgentNotExistException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.NOT_FIND_AGENT_MSG,
					HTTP_RESPONCES_INFO.NOT_FIND_AGENT);
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 新增代理列表
	@RequestMapping(value = "/member", method = RequestMethod.POST)
	public void addMember(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json) {
		Member member = gson.fromJson(json, Member.class);
		try {
			if (userService.checkUsername(member.username)) {
				SrvCommonOpr.setRspResultForError(response,
						HTTP_RESPONCES_INFO.USERNAME_IS_EXIST_MSG,
						HTTP_RESPONCES_INFO.USERNAME_IS_EXIST);
				return;
			}
			if (memberService.addMember(member)) {
				SrvCommonOpr.setRspResultForJson(response, member, HTTP_RESPONCES_INFO.SUCCESS);
			} else {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 修改密码
	@RequestMapping(value = "/member/password", method = RequestMethod.PUT)
	public void modifyPassword(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json) {
		PasswordModify password = gson.fromJson(json, PasswordModify.class);
		password.userId = request.getHeader("uuid");
		try {
			if (!captchaService.check(request.getSession().getId(), password.captcha)) {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.CAPTCHA_INCRECT_MSG,
						HTTP_RESPONCES_INFO.CAPTCHA_INCRECT);
				return;
			}
			if (password.preMoneyPswd == null || password.prePassword == null
					|| password.newMoneyPswd == null || password.newPassword == null) {
				throw new ParamMissingException("password");
			}
			if (memberService.modifyPassword(password)) {
				SrvCommonOpr.setRspResultForJson(response, "success", HTTP_RESPONCES_INFO.SUCCESS);
			} else {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 修改代理信息o.
	@RequestMapping(value = "/member/{uuid}", method = RequestMethod.PUT)
	public void modifyMember(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid, @RequestBody String json) {
		Member member = gson.fromJson(json, Member.class);
		member.uuid = uuid;

		if (memberService.modifyMember(member)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}

	}

	// 删除代理信息
	@RequestMapping(value = "/member/{uuid}", method = RequestMethod.DELETE)
	public void deleteMember(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) {
		if (memberService.delete(uuid)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 获取下线代理列表
	@RequestMapping(value = "/member/tree", method = RequestMethod.GET)
	public void getMemberAndAgentListOfUser(HttpServletRequest request, HttpServletResponse response) {
		try {
			String uuid = request.getParameter("uuid");
			if (uuid == null) {
				uuid = request.getParameter("originid");
			}
			ListResult<Agent> agents = agentService.getAgentListOfUser(uuid, null, null);
			ListResult<Member> members = memberService.getMemberListOfUser(uuid, null, null);

			ArrayList<MemberTreeNode> result = new ArrayList<MemberTreeNode>();
			if (agents.count > 0) {
				for (Agent agent : agents.list) {
					result.add(new MemberTreeNode(agent));
				}
			}
			if (members.count > 0) {
				for (Member member : members.list) {
					result.add(new MemberTreeNode(member));
				}
			}
			SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
		} catch (PermissionNotEnoughException e1) {
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);
		} catch (ResultToBeanException e1) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
}
