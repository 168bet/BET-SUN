package com.wecode.game.exception;

public class PasswordIncorrectException extends Exception
{
	public PasswordIncorrectException()
    {
    }

    public PasswordIncorrectException(String msg)
    {
        super(msg);
    }

    private static final long serialVersionUID = -1L;
}
