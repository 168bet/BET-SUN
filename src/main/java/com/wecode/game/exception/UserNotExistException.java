package com.wecode.game.exception;

public class UserNotExistException extends UserException {

	private static final long serialVersionUID = -1L;

	private String userId;

	public UserNotExistException() {
	}

	public UserNotExistException(String userId) {
		super(userId);
		this.userId = userId;
	}

	@Override
	public String getErrorMsg() {
		return "User Not Exist:" + this.userId;
	}

	@Override
	protected int getIndex() {
		return 3;
	}
}
