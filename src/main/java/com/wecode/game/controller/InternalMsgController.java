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
import com.wecode.game.bean.InternalMsg;
import com.wecode.game.bean.ListResult;
import com.wecode.game.exception.CommonException;
import com.wecode.game.exception.ParamParseFailException;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.identified.InternalMsgState;
import com.wecode.game.service.InternalMsgService;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;

@Controller
public class InternalMsgController {

	private Gson gson = new GsonBuilder().serializeNulls().create();
	@Resource(name = "internalMsgService")
	private InternalMsgService internalMsgService;

	@RequestMapping(value = "/internalMsg/{id}", method = RequestMethod.GET)
	public void getMsgById(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String id) {
		InternalMsg msg = null;
		try {
			msg = internalMsgService.getMsgById(id);
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

	@RequestMapping(value = "/internalMsg/all", method = RequestMethod.GET)
	public void getAllMsgs(HttpServletRequest request, HttpServletResponse response) {
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
			ListResult<InternalMsg> result = internalMsgService.getAllMsgs(num, page);

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

	@RequestMapping(value = "/internalMsg/{uuid}/all", method = RequestMethod.GET)
	public void getUserMsgs(HttpServletRequest request, HttpServletResponse response,
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
			ListResult<InternalMsg> result = internalMsgService.getUserMsgs(uuid, num, page);

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

	@RequestMapping(value = "/internalMsg", method = RequestMethod.POST)
	public void newMsg(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json) {
		InternalMsg msg = gson.fromJson(json, InternalMsg.class);

		String[] uuids = msg.userId.split(";");
		boolean hasError = false;
		for (int i = 0; i < uuids.length; ++i) {
			msg.userId = uuids[i];
			if (!internalMsgService.newMsg(msg)) {
				hasError = true;
			}
		}
		if (hasError) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		} else {
			SrvCommonOpr.setRspResultForJson(response, msg, HTTP_RESPONCES_INFO.SUCCESS);
		}
	}

	@RequestMapping(value = "/internalMsg/{id}", method = RequestMethod.DELETE)
	public void deleteMsg(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String id) {
		if (internalMsgService.deleteMsg(id)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	@RequestMapping(value = "/internalMsg/{id}", method = RequestMethod.PUT)
	public void modifyMsg(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String id, @RequestBody String json) throws CommonException {
		InternalMsg msg = gson.fromJson(json, InternalMsg.class);
		try {
			msg.id = Long.parseLong(id);
		} catch (Exception e) {
			throw new ParamParseFailException(e);
		}

		if (internalMsgService.modifyMsg(msg)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	@RequestMapping(value = "/internalMsg/{id}/read", method = RequestMethod.PUT)
	public void readMsg(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String id) throws CommonException {
		InternalMsg msg = new InternalMsg();
		try {
			msg.id = Long.parseLong(id);
		} catch (Exception e) {
			throw new ParamParseFailException(e);
		}
		msg.state = InternalMsgState.READ;
		if (internalMsgService.modifyMsg(msg)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	@RequestMapping(value = "/internalMsg/{userId}/new", method = RequestMethod.GET)
	public void haveNewMsg(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String userId) {
		SrvCommonOpr.setRspResultForJson(response, internalMsgService.getNewMsgs(userId),
				HTTP_RESPONCES_INFO.SUCCESS);
	}
}
