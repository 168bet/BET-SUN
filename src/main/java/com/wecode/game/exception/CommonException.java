package com.wecode.game.exception;

public abstract class CommonException extends FBException {

	protected int ERROR_COUNT = 7;
	protected String INDEX_USED = "1,2,3,5,6";

	private static final long serialVersionUID = 191055633640335025L;

	public CommonException() {
	}

	public CommonException(String msg) {
		super(msg);
	}

	public CommonException(Exception e) {
		super(e);
	}

	@Override
	protected final int getBaseErrorCode() {
		return 600;
	}
}
