package com.wecode.game.exception;

public abstract class BillException extends FBException {

	protected int ERROR_COUNT = 4;

	private static final long serialVersionUID = -4087523800790036347L;

	public BillException() {
		super();
	}

	public BillException(String msg) {
		super(msg);
	}

	public BillException(Exception e) {
		super(e);
	}

	protected final int getBaseErrorCode() {
		return 800;
	}
}
