package com.wecode.game.bean;

public class WinLoseReport 
{
	public String userId;
	
	public String username; //会员名

	public String agentId;
	
	public String agent; //所属代理

	public Double winLose; //输赢汇总    （最初点数 - 最后剩余点数）

	public Double betAmount;// 投注金额    （所有下注的钱，包括平局下注的钱）

	public Double effectAmount;//有效金额     （所有下注的钱，不包括平局下注的钱，假设该盘为平局，你们有效金额为0，但是投注金额为当前下注的钱）

	public Double agentProfit; //代理优惠汇总  （计算如上所示，分成3列，代理返点或洗码盈利，代理支付下级代理钱，代理纯盈利）
	
	public Double agentPay;
	
	public Double agentPureProfit;
	
	public Double companyProfit;
}
