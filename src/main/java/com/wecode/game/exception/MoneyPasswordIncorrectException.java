package com.wecode.game.exception;

public class MoneyPasswordIncorrectException extends BillException {

	private static final long serialVersionUID = -7323603520822227270L;

	public MoneyPasswordIncorrectException() {
		super();
	}

	public MoneyPasswordIncorrectException(String msg) {
		super(msg);
	}

	public MoneyPasswordIncorrectException(Exception e) {
		super(e);
	}

	@Override
	public String getErrorMsg() {
		return "取款密码错误";
	}

	@Override
	protected int getIndex() {
		return 3;
	}

}
