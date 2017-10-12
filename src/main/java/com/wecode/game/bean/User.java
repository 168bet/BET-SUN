package com.wecode.game.bean;

public class User 
{
	public String uuid;
	public String username;
	public String password;
	public String authKey;
	public Integer state;
	public Boolean locked;
	public String phone;
	public String email;
	public Integer role;
	public Long createdTime;
	public Long lastTime;
	public Integer pwdError;
	public String name;
	
	public User()
	{
		// TODO Auto-generated constructor stub
	}
	
	public User(String uuid)
	{
		this.uuid = uuid;
	}
}
