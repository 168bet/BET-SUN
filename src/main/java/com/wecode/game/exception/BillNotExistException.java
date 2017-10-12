package com.wecode.game.exception;

public class BillNotExistException extends BillException {

	public BillNotExistException() {
	}

	public BillNotExistException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = -8637734531776203807L;

	@Override
	public String getErrorMsg() {
		return "订单不存在";
	}

	@Override
	protected int getIndex() {
		return 1;
	}

}
