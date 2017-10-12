package com.wecode.game.exception;

public class UserIsLockedException extends Exception
{
	 public UserIsLockedException()
	 {
	 }

	 public UserIsLockedException(String msg)
	 {
	     super(msg);
	 }

	 private static final long serialVersionUID = -1L;
}
