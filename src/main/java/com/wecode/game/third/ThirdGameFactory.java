package com.wecode.game.third;

import com.wecode.game.bean.Member;

public class ThirdGameFactory 
{	
	static public IThirdGame create(String name, Member member)
	{
		if ( name.equals("east"))
		{
			return new EastGame(member);
		}
		return null;
	}
}
