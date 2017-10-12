package com.wecode.game.exception;

public class ParamParseFailException extends CommonException {

	private static final long serialVersionUID = -5978836814390442161L;

	public ParamParseFailException() {
	}

	public ParamParseFailException(String msg) {
		super(msg);
	}

	public ParamParseFailException(Exception e) {
		super(e);
	}

	@Override
	public String getErrorMsg() {
		return "parse param error";
	}

	@Override
	protected int getIndex() {
		return 2;
	}

}
