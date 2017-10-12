package com.wecode.game.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.UserLog;
import com.wecode.game.exception.FBException;
import com.wecode.game.exception.ParamParseFailException;
import com.wecode.game.service.UserLogService;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;

@Controller
public class UserLogController {

	@Resource(name = "userLogService")
	private UserLogService userLogService;

	@RequestMapping(value = "/userlog/all", method = RequestMethod.GET)
	public void getAllLog(HttpServletRequest request, HttpServletResponse response)
			throws FBException {
		Integer num = null;
		Integer page = null;
		Long start = null;
		Long end = null;
		String username = null;
		try {
			String numStr = request.getParameter("num");
			if (numStr != null && !numStr.trim().isEmpty()) {
				num = Integer.parseInt(numStr);
			}
			String pageStr = request.getParameter("page");
			if (pageStr != null && !pageStr.trim().isEmpty()) {
				page = Integer.parseInt(pageStr);
			}
			String startStr = request.getParameter("start");
			if (startStr != null && !startStr.trim().isEmpty()) {
				start = Long.parseLong(startStr);
			}
			String endStr = request.getParameter("end");
			if (endStr != null && !endStr.trim().isEmpty()) {
				end = Long.parseLong(endStr);
			}
			username = request.getParameter("username");
			if (username != null && !username.trim().isEmpty()) {
				username = username.trim();
			} else {
				username = null;
			}
		} catch (Exception e) {
			throw new ParamParseFailException(e);
		}
		ListResult<UserLog> result = null;
		if (username == null) {
			result = userLogService.getAllLog(request.getHeader("uuid"), start, end, num, page);
		} else {
			result = userLogService.getUserLog("%" + username + "%", start, end, num, page);
		}

		SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
	}

	// 获取当个成员及其下属的日志(管理日志)
	@RequestMapping(value = "/userlog/{uuid}", method = RequestMethod.GET)
	public void getUserLog(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) throws FBException {
		Integer num = null;
		Integer page = null;
		Long start = null;
		Long end = null;
		try {
			String numStr = request.getParameter("num");
			if (numStr != null) {
				num = Integer.parseInt(numStr);
			}
			String pageStr = request.getParameter("page");
			if (pageStr != null) {
				page = Integer.parseInt(pageStr);
			}
			String startStr = request.getParameter("start");
			if (startStr != null) {
				start = Long.parseLong(startStr);
			}
			String endStr = request.getParameter("end");
			if (endStr != null) {
				end = Long.parseLong(endStr);
			}
		} catch (Exception e) {
			throw new ParamParseFailException(e);
		}
		ListResult<UserLog> result = userLogService.getUserManageLog(uuid, start, end, num, page);

		SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
	}

	@RequestMapping(value = "/userlog/{id}", method = RequestMethod.DELETE)
	public void deleteMsg(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String id) {
		if (userLogService.deleteLog(id)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

}
