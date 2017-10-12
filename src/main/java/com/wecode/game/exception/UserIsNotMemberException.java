package com.wecode.game.exception;

public class UserIsNotMemberException extends Exception 
{
	public UserIsNotMemberException()
	{
	}

	public UserIsNotMemberException(String msg)
	{
		super(msg);
	}

	private static final long serialVersionUID = -1L;
}
