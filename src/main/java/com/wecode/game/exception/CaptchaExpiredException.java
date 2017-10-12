package com.wecode.game.exception;

public class CaptchaExpiredException extends CaptchaException {

	private static final long serialVersionUID = -8519868023232665922L;

	@Override
	public String getErrorMsg() {
		return "验证码过期";
	}

	@Override
	protected int getIndex() {
		return 2;
	}

}
