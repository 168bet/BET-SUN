package com.wecode.game.third;

import java.util.List;

import com.wecode.game.bean.Bet;

public interface IThirdGame 
{

	public Double close() throws Exception;
	public String open() throws Exception;
	public Double getBalance();
	List<Bet> getGameData(Long vendorid);
}
