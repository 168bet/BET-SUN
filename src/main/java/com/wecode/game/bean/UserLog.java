package com.wecode.game.bean;

public class UserLog {

	public Long id;
	public Long time;
	public String ip;
	public String userId;
	public String username;
	public Long pid;
	public Integer role;
	public String content;
	public Integer state;

	public static final int LOG_STATE_FAILED = 0;
	public static final int LOG_STATE_SUCCEED = 1;
}
