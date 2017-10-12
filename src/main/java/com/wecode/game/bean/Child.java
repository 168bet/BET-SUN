package com.wecode.game.bean;

public class Child extends User 
{	
	public String parentId;
	
	public Child()
	{
		
	}
	
	public Child(String uuid)
	{
		super(uuid);
	}
}
