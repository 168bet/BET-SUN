package com.wecode.game.exception;

public class AgentNotExistException extends AgentException
{
	public AgentNotExistException()
	{
	}

	public AgentNotExistException(String msg)
	{
		super(msg);
	}

	private static final long serialVersionUID = -1L;

	@Override
	public String getErrorMsg() {
		return "代理不存在";
	}

	@Override
	protected int getIndex() {
		return 1;
	}
}
