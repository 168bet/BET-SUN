package com.wecode.game.exception;

public abstract class UserException extends FBException {

	protected int ERROR_COUNT = 8;
	protected String INDEX_USED = "3";

	private static final long serialVersionUID = 1015198342232678163L;

	public UserException() {
	}

	public UserException(String msg) {
		super(msg);
	}

	public UserException(Exception e) {
		super(e);
	}

	@Override
	protected final int getBaseErrorCode() {
		return 650;
	}
}
