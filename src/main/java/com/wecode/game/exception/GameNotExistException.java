package com.wecode.game.exception;

public class GameNotExistException extends Exception
{
	public GameNotExistException()
    {
    }

    public GameNotExistException(String msg)
    {
        super(msg);
    }

    private static final long serialVersionUID = -1L;
}
