package com.wecode.game.util;

import com.wecode.game.bean.User;

public class CurrentOperatorUtil
{
	 private static ThreadLocal<User> userList = new ThreadLocal<User>();

	 public static User getUser()
	 {
	     return userList.get();
	 }

	 public static void setUser(User user)
	 {
		 userList.set(user);
	 }
}
