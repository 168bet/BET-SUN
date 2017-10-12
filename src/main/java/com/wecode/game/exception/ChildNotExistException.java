package com.wecode.game.exception;

public class ChildNotExistException extends AgentException {
	public ChildNotExistException() {
	}

	public ChildNotExistException(Exception e) {
		super(e);
	}

	public ChildNotExistException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = -1L;

	@Override
	public String getErrorMsg() {
		return "子账号不存在";
	}

	@Override
	protected int getIndex() {
		return 2;
	}
}
