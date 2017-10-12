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
import com.wecode.game.bean.PublicMsg;
import com.wecode.game.bean.User;
import com.wecode.game.exception.CommonException;
import com.wecode.game.exception.ParamParseFailException;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.service.PublicMsgService;
import com.wecode.game.service.UserService;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;

@Controller
public class PublicMsgController {

	private Gson gson = new GsonBuilder().serializeNulls().create();
	@Resource(name = "publicMsgService")
	private PublicMsgService publicMsgService;
	@Resource(name = "userService")
	private UserService userService;

	@RequestMapping(value = "/publicMsg/{type}/{id}", method = RequestMethod.GET)
	public void getMsgById(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String id, @PathVariable String type) {
		PublicMsg msg = null;
		try {
			msg = publicMsgService.getMsgById(type, id);
		} catch (ResultToBeanException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
			return;
		}
		if (msg == null) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.NOT_FIND_AGENT_MSG,
					HTTP_RESPONCES_INFO.NOT_FIND_AGENT);
			return;
		}
		SrvCommonOpr.setRspResultForJson(response, msg, HTTP_RESPONCES_INFO.SUCCESS);
	}

	@RequestMapping(value = "/publicMsg/{type}/all", method = RequestMethod.GET)
	public void getAllMsgs(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String type) {
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
			ListResult<PublicMsg> result = publicMsgService.getAllMsgs(type, num, page);

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

	@RequestMapping(value = "/publicMsg/{type}/latest", method = RequestMethod.GET)
	public void getLatestMsgs(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String type) {
		PublicMsg msg = null;
		try {
			msg = publicMsgService.getLatestMsgs(type);
		} catch (ResultToBeanException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
			return;
		}
		SrvCommonOpr.setRspResultForJson(response, msg, HTTP_RESPONCES_INFO.SUCCESS);
	}

	@RequestMapping(value = "/publicMsg/{type}", method = RequestMethod.POST)
	public void newMsg(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json, @PathVariable String type) {
		PublicMsg msg = gson.fromJson(json, PublicMsg.class);
		if (request.getHeader("uuid") == null) {
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);
			return;
		}
		User user = userService.getUserByUuid(request.getHeader("uuid"));
		if (user == null) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.USER_NOT_EXIST_MSG,
					HTTP_RESPONCES_INFO.USER_NOT_EXIST);
			return;
		}
		msg.publisher = user.username;

		if (publicMsgService.newMsg(type, msg)) {
			SrvCommonOpr.setRspResultForJson(response, msg, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	@RequestMapping(value = "/publicMsg/{type}/{id}", method = RequestMethod.DELETE)
	public void deleteMsg(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String id, @PathVariable String type) {
		if (publicMsgService.deleteMsg(type, id)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	@RequestMapping(value = "/publicMsg/{type}/{id}", method = RequestMethod.PUT)
	public void modifyMsg(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String type, @PathVariable String id, @RequestBody String json)
			throws CommonException {
		PublicMsg msg = gson.fromJson(json, PublicMsg.class);
		if (request.getHeader("uuid") == null) {
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);
			return;
		}
		User user = userService.getUserByUuid(request.getHeader("uuid"));
		if (user == null) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.USER_NOT_EXIST_MSG,
					HTTP_RESPONCES_INFO.USER_NOT_EXIST);
			return;
		}
		msg.publisher = user.username;
		try {
			msg.id = Long.parseLong(id);
		} catch (Exception e) {
			throw new ParamParseFailException(e);
		}

		if (publicMsgService.modifyMsg(type, msg)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
}
