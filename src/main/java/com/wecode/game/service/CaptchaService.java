package com.wecode.game.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import com.wecode.game.bean.Captcha;
import com.wecode.game.exception.CaptchaGenFailException;
import com.wecode.game.util.CaptchaUtils;

@Service("captchaService")
public class CaptchaService {

	private static Map<String, Captcha> captcha = new HashMap<String, Captcha>();
	private Long validityPeriod = 5 * 60 * 1000L;

	public Captcha getCaptcha(String sessionId) {
		Captcha c = captcha.get(sessionId);
		if (c == null) {
			return null;
		}
		captcha.remove(sessionId);
		if (c.expireTime < System.currentTimeMillis()) {
			return null;
		}
		return c;
	}

	public boolean check(String sessionId, String code) {
		Captcha c = captcha.get(sessionId);
		return captcha != null && c.code != null && c.code.equalsIgnoreCase(code);
	}

	public Captcha genCaptcha(String sessionId) throws CaptchaGenFailException {
		Captcha c = new Captcha();
		c.code = CaptchaUtils.genCode(4);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(CaptchaUtils.genCaptcha(c.code), "png", out);
		} catch (IOException e) {
			throw new CaptchaGenFailException(e);
		}
		byte[] binaryData = out.toByteArray();
		String data = Base64.encodeBase64String(binaryData).replaceAll("\r\n", "");
		if (data == null || data.isEmpty()) {
			throw new CaptchaGenFailException();
		}
		c.image = "data:image/png;base64," + data;
		c.expireTime = System.currentTimeMillis() + validityPeriod;
		c.userId = sessionId;
		captcha.put(sessionId, c);
		return c;
	}

	public void clearInvalid() {
		List<String> sessionIds = new ArrayList<String>();
		Long current = System.currentTimeMillis();
		for (Captcha c : captcha.values()) {
			if (c.expireTime > current) {
				sessionIds.add(c.userId);
			}
		}
		for (String sessionId : sessionIds) {
			captcha.remove(sessionId);
		}
	}
}
