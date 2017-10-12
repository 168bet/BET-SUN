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
import com.wecode.game.bean.Login;
import com.wecode.game.bean.PasswordModify;
import com.wecode.game.bean.ResetPwd;
import com.wecode.game.bean.User;
import com.wecode.game.exception.CommonException;
import com.wecode.game.exception.ParamMissingException;
import com.wecode.game.exception.PasswordIncorrectException;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.PwdErrorThanFiveException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.exception.UserIsLockedException;
import com.wecode.game.exception.UserIsStoppedException;
import com.wecode.game.exception.UsernameNotExistException;
import com.wecode.game.service.CaptchaService;
import com.wecode.game.service.UserService;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;

@Controller
public class UserController {
	private Gson gson = new GsonBuilder().serializeNulls().create();
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "captchaService")
	private CaptchaService captchaService;

	// 登录
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String loginJson) throws Exception {
		Login login = gson.fromJson(loginJson, Login.class);
		if (!captchaService.check(request.getSession().getId(), login.captcha)) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.CAPTCHA_INCRECT_MSG,
					HTTP_RESPONCES_INFO.CAPTCHA_INCRECT);
			return;
		}
		User user = null;
		try {
			user = userService.login(login);
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
		} catch (PermissionNotEnoughException e) {
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);
			return;
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
			return;
		}
		SrvCommonOpr.setRspResultForJson(response, user, HTTP_RESPONCES_INFO.SUCCESS);
		return;
	}

	// 修改账户信息
	@RequestMapping(value = "/user/{uuid}", method = RequestMethod.PUT)
	public void modifyUserInfo(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid, @RequestBody String json) {
		User user = gson.fromJson(json, User.class);
		user.uuid = uuid;
		if (userService.modifyInfo(user)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 修改自身账户密码
	@RequestMapping(value = "/user/{uuid}/password", method = RequestMethod.PUT)
	public void resetPwd(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid, @RequestBody String json) {
		ResetPwd resetPwd = gson.fromJson(json, ResetPwd.class);
		resetPwd.uuid = uuid;
		try {
			if (userService.resetPwd(resetPwd)) {
				SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
			} else {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
			return;
		} catch (PasswordIncorrectException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.PASSWORD_INCORRECT_MSG,
					HTTP_RESPONCES_INFO.PASSWORD_INCORRECT);
		} catch (ResultToBeanException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 变更状态（启用/停用）
	@RequestMapping(value = "/user/{uuid}/state", method = RequestMethod.PUT)
	public void modifyState(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid, @RequestBody String json) {
		User user = gson.fromJson(json, User.class);
		user.uuid = uuid;

		if (userService.modifyState(user)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 锁定或解锁用户
	@RequestMapping(value = "/user/{uuid}/locked", method = RequestMethod.PUT)
	public void modifyLock(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) {

		try {
			if (userService.modifyLock(uuid)) {
				SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
			} else {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (ResultToBeanException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 修改密码
	@RequestMapping(value = "/user/password", method = RequestMethod.PUT)
	public void modifyPassword(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json) throws CommonException {
		PasswordModify password = gson.fromJson(json, PasswordModify.class);
		password.userId = request.getHeader("uuid");
		if (password.prePassword == null || password.newPassword == null) {
			throw new ParamMissingException("prePassword or newPassword");
		}
		try {
			if (userService.modifyPassword(password)) {
				SrvCommonOpr.setRspResultForJson(response, "success", HTTP_RESPONCES_INFO.SUCCESS);
			} else {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (PasswordIncorrectException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.PASSWORD_INCORRECT_MSG,
					HTTP_RESPONCES_INFO.PASSWORD_INCORRECT);
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
	@RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
	public void getUserInfoByUsername(HttpServletRequest request, HttpServletResponse response, @PathVariable String username)
	{
		User user = userService.getUserByUsername(username);
		if ( user == null )
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.USER_NOT_EXIST_MSG,
					HTTP_RESPONCES_INFO.USER_NOT_EXIST);
		}
		else
		{
			SrvCommonOpr.setRspResultForJson(response, user, HTTP_RESPONCES_INFO.SUCCESS);
		}
	}
}
