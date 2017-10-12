package com.wecode.game.controller;

import java.net.URLDecoder;

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
import com.wecode.game.bean.Bill;
import com.wecode.game.bean.ListResult;
import com.wecode.game.exception.BillNotExistException;
import com.wecode.game.exception.CaptchaIncorrectException;
import com.wecode.game.exception.FBException;
import com.wecode.game.exception.JsonParseException;
import com.wecode.game.exception.MemberNotExistException;
import com.wecode.game.exception.NameInCorrectException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.service.BillService;
import com.wecode.game.service.CaptchaService;
import com.wecode.game.service.UserService;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;

@Controller
public class BillController {
	private Gson gson = new GsonBuilder().serializeNulls().create();
	@Resource(name = "billService")
	private BillService billService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "captchaService")
	private CaptchaService captchaService;

	// 新建存款单
	@RequestMapping(value = "/deposite/manual", method = RequestMethod.POST)
	public void manualDeposite(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json) throws Exception {
		Bill bill = gson.fromJson(json, Bill.class);
		if (!captchaService.check(request.getSession().getId(), bill.captcha)) {
			throw new CaptchaIncorrectException();
		}
		if (billService.manualdeDosite(bill)) {
			SrvCommonOpr.setRspResultForJson(response, bill, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 确认存款订单
	@RequestMapping(value = "/deposite/{uuid}/check", method = RequestMethod.PUT)
	public void check(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) {
		try {
			if (billService.check(uuid)) {
				SrvCommonOpr.setRspResultForJson(response, "Checked", HTTP_RESPONCES_INFO.SUCCESS);
			} else {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (NameInCorrectException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.NAME_IN_CORRECT_MSG,
					HTTP_RESPONCES_INFO.NAME_IN_CORRECT);
		} catch (MemberNotExistException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.MEMBER_NOT_EXIST_MSG,
					HTTP_RESPONCES_INFO.MEMBER_NOT_EXIST);
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 拒绝存款订单
	@RequestMapping(value = "/deposite/{uuid}/reject", method = RequestMethod.PUT)
	public void reject(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) {
		try {
			if (billService.reject(uuid)) {
				SrvCommonOpr.setRspResultForJson(response, "reject", HTTP_RESPONCES_INFO.SUCCESS);
			} else {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (NameInCorrectException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.NAME_IN_CORRECT_MSG,
					HTTP_RESPONCES_INFO.NAME_IN_CORRECT);
		} catch (MemberNotExistException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.MEMBER_NOT_EXIST_MSG,
					HTTP_RESPONCES_INFO.MEMBER_NOT_EXIST);
		} catch (BillNotExistException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.MEMBER_NOT_EXIST_MSG,
					HTTP_RESPONCES_INFO.MEMBER_NOT_EXIST);
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 将订单转为未确认
	@RequestMapping(value = "/deposite/{uuid}/checking", method = RequestMethod.PUT)
	public void checking(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String uuid) {
		try {
			if (billService.checking(uuid)) {
				SrvCommonOpr.setRspResultForJson(response, "checking", HTTP_RESPONCES_INFO.SUCCESS);
			} else {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (NameInCorrectException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.NAME_IN_CORRECT_MSG,
					HTTP_RESPONCES_INFO.NAME_IN_CORRECT);
		} catch (MemberNotExistException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.MEMBER_NOT_EXIST_MSG,
					HTTP_RESPONCES_INFO.MEMBER_NOT_EXIST);
		} catch (BillNotExistException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.MEMBER_NOT_EXIST_MSG,
					HTTP_RESPONCES_INFO.MEMBER_NOT_EXIST);
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 获取存款列表
	@RequestMapping(value = "/deposite/all", method = RequestMethod.GET)
	public void getDepositesByState(HttpServletRequest request, HttpServletResponse response) {
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
		String stateStr = request.getParameter("state");
		Integer state = null;
		if (stateStr != null) {
			state = Integer.parseInt(stateStr);
		}
		try {
			ListResult<Bill> result = billService.getDepositesByState(state, page, num);
			SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 存款
	@RequestMapping(value = "/deposite", method = RequestMethod.POST)
	public void deposite(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json) {
		Bill bill = gson.fromJson(json, Bill.class);
		try {
			if (billService.deposite(bill)) {
				SrvCommonOpr.setRspResultForJson(response, bill, HTTP_RESPONCES_INFO.SUCCESS);
			} else {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (NameInCorrectException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.NAME_IN_CORRECT_MSG,
					HTTP_RESPONCES_INFO.NAME_IN_CORRECT);
		} catch (MemberNotExistException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.MEMBER_NOT_EXIST_MSG,
					HTTP_RESPONCES_INFO.MEMBER_NOT_EXIST);
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 取款
	@RequestMapping(value = "/withdrawals", method = RequestMethod.POST)
	public void withdrawals(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json) throws FBException {
		Bill bill = null;
		try {
			bill = gson.fromJson(URLDecoder.decode(json, "UTF-8"), Bill.class);
		} catch (Exception e) {
			throw new JsonParseException(e, json);
		}
		if (!captchaService.check(request.getSession().getId(), bill.captcha)) {
			throw new CaptchaIncorrectException();
		}
		bill.userId = request.getHeader("uuid");
		if (billService.withdrawals(bill)) {
			SrvCommonOpr.setRspResultForJson(response, bill, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 更新提款单状态
	@RequestMapping(value = "/withdrawals/{uuid}/state", method = RequestMethod.PUT)
	public void updataBillState(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json, @PathVariable String uuid) throws Exception {
		Bill bill = gson.fromJson(json, Bill.class);
		bill.uuid = uuid;
		try {
			if (billService.checkResult(bill)) {
				SrvCommonOpr.setRspResultForJson(response, bill, HTTP_RESPONCES_INFO.SUCCESS);
			} else {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (ResultToBeanException e) {
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.RESUALT_PARSE_ERROR_MSG,
					HTTP_RESPONCES_INFO.RESUALT_PARSE_ERROR);
		}
	}

	// 获取取款列表
	@RequestMapping(value = "/withdrawals", method = RequestMethod.GET)
	public void getBillListOfWithdrawals(HttpServletRequest request, HttpServletResponse response) {
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
		String stateStr = request.getParameter("state");
		Integer state = null;
		if (stateStr != null) {
			state = Integer.parseInt(stateStr);
		}

		Integer role = Integer.parseInt(request.getParameter("role"));

		try {
			ListResult<Bill> result = billService.getBillListOfWithdrawals(state, role, page, num);
			SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 获取充值记录
	@RequestMapping(value = "/deposite/{uuid}", method = RequestMethod.GET)
	public void getBillListOfDeposite(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String memberId) {
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
			ListResult<Bill> result = billService.getBillListOfDeposite(memberId, page, num);
			SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	// 获取单个会员充值记录
	@RequestMapping(value = "/bill/{memberId}/all", method = RequestMethod.GET)
	public void getMemberBills(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String memberId) {
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
			ListResult<Bill> result = billService.getMemberBills(memberId, page, num);
			SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
		} catch (Exception e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
}
