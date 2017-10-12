package com.wecode.game.exception;

public abstract class CaptchaException extends FBException {

	private static final long serialVersionUID = 7863392814110082679L;
	protected int ERROR_COUNT = 3;

	public CaptchaException() {
	}

	public CaptchaException(String msg) {
	}

	public CaptchaException(Exception e) {
		super(e);
	}

	@Override
	protected final int getBaseErrorCode() {
		return 900;
	}
}
