package com.wecode.game.exception;

public abstract class AgentException extends FBException {

	protected int ERROR_COUNT = 2;
	
	private static final long serialVersionUID = -6724212177390175539L;

	public AgentException() {
	}

	public AgentException(String msg) {
		super(msg);
	}

	public AgentException(Exception e) {
		super(e);
	}

	@Override
	protected final int getBaseErrorCode() {
		return 700;
	}
}
