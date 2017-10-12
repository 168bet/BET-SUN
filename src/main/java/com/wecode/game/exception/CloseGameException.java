package com.wecode.game.exception;

public class CloseGameException extends Exception 
{
	public CloseGameException()
	{
	}

	public CloseGameException(String msg)
	{
		super(msg);
	}

	private static final long serialVersionUID = -1L;
}
