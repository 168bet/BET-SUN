package com.wecode.game.exception;

public class PointNotEnoughException extends BillException {
	private static final long serialVersionUID = -1L;

	public PointNotEnoughException() {
	}

	public PointNotEnoughException(String msg) {
		super(msg);
	}

	@Override
	public String getErrorMsg() {
		return "余额不足";
	}

	@Override
	protected int getIndex() {
		return 0;
	}

}
