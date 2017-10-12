package com.wecode.game.exception;

public class ParamMissingException extends CommonException {

	private static final long serialVersionUID = 3968954454610747287L;

	private String param;

	public ParamMissingException(String msg) {
		super(msg);
		this.param = msg;
	}

	public ParamMissingException(Exception e) {
		super(e);
	}

	@Override
	public String getErrorMsg() {
		return "param missing:" + param;
	}

	@Override
	protected int getIndex() {
		return 3;
	}

}
