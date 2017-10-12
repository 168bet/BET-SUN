package com.wecode.game.exception;

public class AcceptedBillModifyException extends BillException {

	private static final long serialVersionUID = -7276492335664034676L;

	public AcceptedBillModifyException(String msg) {
		super(msg);
	}

	public AcceptedBillModifyException() {

	}

	@Override
	public String getErrorMsg() {
		return "签收订单不允许再修改";
	}

	@Override
	protected int getIndex() {
		return 2;
	}

}
