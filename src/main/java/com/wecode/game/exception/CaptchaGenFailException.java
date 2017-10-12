package com.wecode.game.exception;

public class CaptchaGenFailException extends CaptchaException {

	private static final long serialVersionUID = -396214429676847549L;

	public CaptchaGenFailException() {
	}

	public CaptchaGenFailException(String msg) {
		super(msg);
	}

	public CaptchaGenFailException(Exception e) {
		super(e);
	}

	@Override
	public String getErrorMsg() {
		return "验证码生成失败";
	}

	@Override
	protected int getIndex() {
		return 0;
	}

}
