package com.wecode.game.bean;

public class Member extends User 
{	
	public Double point;
	public Integer upperLimit;
	public Integer privilegeNum;
	public String parentId;
	public Double profit;
	public String agent;
	public String moneyPwd;
	public Long lastGameId;
	public String captcha;
	
	public Member()
	{
		
	}
	
	public Member(String uuid)
	{
		super(uuid);
	}
}
