package com.wecode.game.exception;

public class CaptchaIncorrectException extends CaptchaException {

	private static final long serialVersionUID = 3001278972772633432L;

	@Override
	public String getErrorMsg() {
		return "验证码错误";
	}

	@Override
	protected int getIndex() {
		return 1;
	}

}
