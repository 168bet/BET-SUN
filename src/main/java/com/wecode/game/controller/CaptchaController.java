package com.wecode.game.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wecode.game.bean.Captcha;
import com.wecode.game.service.CaptchaService;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;

@Controller
public class CaptchaController {
	@Resource(name = "captchaService")
	private CaptchaService captchaService;

	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
	public void captcha(HttpServletRequest request, HttpServletResponse response)
			throws IOException, Exception {
		Captcha captcha = captchaService.genCaptcha(request.getSession().getId());
		SrvCommonOpr.setRspResultForJson(response, captcha.image, HTTP_RESPONCES_INFO.SUCCESS);
	}
}
