package com.wecode.game.bean;

public class Agent extends User 
{
	public Long childNum;
	public Long agentNum;
	public Long memberNum;
	public Double point;
	public Double commissionPer;
	public Double rebate;
	public Boolean onlyRead;
	public String parentId;
	public String moneyPwd;

	public Agent()
	{
		
	}
	
	public Agent(String uuid)
	{
		super(uuid);
	}
}
