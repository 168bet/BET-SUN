package com.wecode.game.exception;

public class SaveObjectException extends CommonException {

	private static final long serialVersionUID = 2503405560366143919L;

	public SaveObjectException() {
		super();
	}

	public SaveObjectException(Exception e) {
		super(e);
	}

	public SaveObjectException(String msg) {
		super(msg);
	}

	@Override
	public String getErrorMsg() {
		return null;
	}

	@Override
	protected int getIndex() {
		return 6;
	}

}
