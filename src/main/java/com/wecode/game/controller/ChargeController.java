package com.wecode.game.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wecode.game.bean.Bill;
import com.wecode.game.bean.Charge;
import com.wecode.game.bean.Member;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.identified.ChargeState;
import com.wecode.game.identified.GoodType;
import com.wecode.game.service.BillService;
import com.wecode.game.service.ChargeService;
import com.wecode.game.service.MemberService;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;
import com.wecode.game.util.yjpay.PayAPIURIList;
import com.wecode.game.util.yjpay.YJPayConfig;

@Controller
public class ChargeController {

	private Gson gson = new GsonBuilder().serializeNulls().create();
	@Resource(name = "chargeService")
	private ChargeService chargeService;
	@Resource(name = "billService")
	private BillService billService;
	@Resource(name = "memberService")
	private MemberService memberService;

	private static String callback = "";
	private static String fcallback = "";

	@RequestMapping(value = "/charge", method = RequestMethod.POST)
	public void charge(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json) {
		ChargeController.initCallback(request);
		Charge charge = gson.fromJson(json, Charge.class);
		Member member = null;
		try {
			member = memberService.getMemberByUuid(charge.uid);
		} catch (ResultToBeanException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
		if (member == null) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
			return;
		}
		charge.uname = member.username;
		charge.upoint = new Double(member.point * 100).longValue();
		charge.payTime = 0L;
		charge.payTimes = 0;
		charge.state = ChargeState.NEW;
		charge.userAgent = request.getHeader("user-agent");
		charge.goodsId = GoodType.COMPANY_CHARGE;
		charge.ip = ChargeController.getRemortIP(request);
		charge.orderNum = chargeService.createOrderNum(charge.uid);
		charge.createTime = System.currentTimeMillis() / 1000;
		if (!chargeService.addCharge(charge)) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
			return;
		}

		HashMap<String, String> rslt = new HashMap<String, String>();
		rslt.put("data", chargeService.getYJPayEncryptData(charge, callback, fcallback));
		rslt.put("encryptkey", chargeService.getYJPayEncryptKey());
		rslt.put("url", YJPayConfig.UrlPrefix + PayAPIURIList.PCWEB_PAY.getValue());
		rslt.put("merchantaccount", YJPayConfig.MerchantAccount);
		SrvCommonOpr.setRspResultForJson(response, rslt, HTTP_RESPONCES_INFO.SUCCESS);
	}

	@RequestMapping(value = "/chargeCallBack", method = RequestMethod.POST)
	public void chargeCallBack(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("data") String yb_data, @RequestParam("encryptkey") String yb_encryptkey) {

		// 对易宝返回的结果进行验签
		boolean passSign = chargeService.checkDecryptAndSign(yb_data, yb_encryptkey);
		if (passSign) {
			// 验签通过
			String decryptData = chargeService.decryptData(yb_data, yb_encryptkey);
			Map<String, String> map = gson.fromJson(decryptData,
					new TypeToken<Map<String, String>>() {
					}.getType());
			try {
				Charge charge = chargeService.updateCharge(map);
				if (charge != null) {
					Member member = memberService.getMemberByUuid(charge.uid);
					Bill bill = new Bill();
					bill.username = charge.uname;
					bill.name = member.name;
					bill.userId = charge.uid;
					bill.point = charge.amount / 100.0;
					billService.deposite(bill);
					SrvCommonOpr.setRspResultForJson(response, "success",
							HTTP_RESPONCES_INFO.SUCCESS);
				}
			} catch (Exception e) {
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
				return;
			}
		}
		SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
				HTTP_RESPONCES_INFO.UNKOWN);
	}

	private static String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}

	private static void initCallback(HttpServletRequest request) {
		if (callback == "") {
			String basePath = request.getScheme() + "://" + request.getServerName() + ":"
					+ request.getServerPort();
			callback = basePath + "/game/chargeCallBack";
			fcallback = basePath + "/game/homepage/pay_success.html";
		}
	}
}
