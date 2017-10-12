package com.wecode.game.exception;

public class UserIsStoppedException extends Exception
{
	public UserIsStoppedException()
	{
	}

	public UserIsStoppedException(String msg)
	{
		super(msg);
	}

	private static final long serialVersionUID = -1L;
}
