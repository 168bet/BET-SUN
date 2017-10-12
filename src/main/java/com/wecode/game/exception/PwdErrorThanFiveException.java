package com.wecode.game.exception;

public class PwdErrorThanFiveException extends Exception
{
	public PwdErrorThanFiveException()
	{
		
	}

	public PwdErrorThanFiveException(String msg)
	{
		super(msg);
	}

	private static final long serialVersionUID = -1L;
}
