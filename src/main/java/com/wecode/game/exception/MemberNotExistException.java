package com.wecode.game.exception;

public class MemberNotExistException extends Exception
{
	public MemberNotExistException()
	{
	}

	public MemberNotExistException(String msg)
	{
		super(msg);
	}

	private static final long serialVersionUID = -1L;
}
