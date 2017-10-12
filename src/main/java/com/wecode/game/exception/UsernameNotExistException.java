package com.wecode.game.exception;

public class UsernameNotExistException extends Exception
{
	 public UsernameNotExistException()
	 {
	 }

	 public UsernameNotExistException(String msg)
	 {
	     super(msg);
	 }

	 private static final long serialVersionUID = -1L;
}
