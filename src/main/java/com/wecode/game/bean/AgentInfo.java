package com.wecode.game.bean;

public class AgentInfo
{
	public String uuid;
	public String username;
	public Boolean hasAgent;
	
	public AgentInfo(String uuid, String username, Long agentNum)
	{
		this.uuid = uuid;
		this.username = username;
		this.hasAgent = (agentNum > 0 ? true : false);
	}
}
