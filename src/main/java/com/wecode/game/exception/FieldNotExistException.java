package com.wecode.game.exception;

public class FieldNotExistException extends Exception
{
    public FieldNotExistException()
    {
    }

    public FieldNotExistException(String msg)
    {
        super(msg);
    }

    private static final long serialVersionUID = -1L;
}
