package com.wecode.game.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wecode.bean.select.BetSelect;
import com.wecode.game.bean.Agent;
import com.wecode.game.bean.Bet;
import com.wecode.game.bean.Company;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.Member;
import com.wecode.game.bean.User;
import com.wecode.game.bean.WinLoseReport;
import com.wecode.game.dao.AgentDao;
import com.wecode.game.dao.CompanyDao;
import com.wecode.game.dao.GameDao;
import com.wecode.game.dao.MemberDao;
import com.wecode.game.dao.ReportDao;
import com.wecode.game.dao.UserDao;
import com.wecode.game.identified.Role;
import com.wecode.game.third.IThirdGame;
import com.wecode.game.third.ThirdGameFactory;

@Service("reportService")
public class ReportService
{	
	@Resource(name = "reportDao")
    private ReportDao reportDao;
	
	@Resource(name = "gameDao")
    private GameDao gameDao;
	
	@Resource(name = "companyDao")
    private CompanyDao companyDao;
	
	@Resource(name = "userDao")
    private UserDao userDao;
	
	@Resource(name = "memberDao")
    private MemberDao memberDao;
	
	@Resource(name = "agentDao")
    private AgentDao agentDao;
	
	public ListResult<WinLoseReport> getWinLostReport(BetSelect betSelect)
	{	
		updateBetData();
		List<Member> memberList = null;
		Long count ;
		if ( betSelect.username != null)
		{
			memberList = memberDao.getMemberListByUsername(betSelect);
			count =  memberDao.countMemberListByUsername(betSelect.username);;
		}
		else
		{
			ListResult<Member> memberListResult = getAllMemberOfUser(betSelect);
			count = memberListResult.count;
			memberList = memberListResult.list;
		}
		List<WinLoseReport> winLoseList = null;
		if ( memberList != null )
		{
			winLoseList = new ArrayList<WinLoseReport>();
			for (Member member : memberList)
			{
				betSelect.username = member.username;
				List<Bet> betList = reportDao.getBetList(betSelect);
				WinLoseReport winLose = computeWinLost(member, betSelect, betList);
				winLoseList.add(winLose);
			}
		}
		return new ListResult<WinLoseReport>(count, winLoseList);
	}

	private WinLoseReport computeWinLost(Member member, BetSelect betSelect, List<Bet> betList) 
	{
		WinLoseReport winLose = new WinLoseReport();
		String currentUuid = null;
		if ( betSelect.userId.equals("manager") )
		{
			currentUuid = "manager";
		}
		else
		{
			User user = userDao.getUserByUuid(betSelect.userId);
			if ( user.role == Role.SUPER || user.role == Role.MANAGER || user.role == Role.QRMANAGER )
			{
				currentUuid = "manager";
			}
			else if ( user.role == Role.AGENT )
			{
				currentUuid = betSelect.userId;
			}
		}
		winLose.userId = member.uuid;
		winLose.username = member.username;
		winLose.agentId = member.parentId;
		if ( member.parentId.equals("manager") )
		{
			winLose.agent = "manager";
		}
		else
		{
			Agent agent = agentDao.getAgentByUuid(member.parentId);
			winLose.agent = agent.username;
		}
		String parentId =  member.parentId;
		Agent currentAgent = null;
		if ( currentUuid.equals("manager") )
		{
			currentAgent = new Agent();
			currentAgent.commissionPer = 0.0;
			currentAgent.rebate = 100.0;
		}
		else
		{
			currentAgent = agentDao.getAgentByUuid(currentUuid);
		}
		Agent secondAgent = null;
		while ( !parentId.equals(currentUuid))
		{
			secondAgent = agentDao.getAgentByUuid(parentId);
			parentId = secondAgent.parentId;
		}
		winLose.winLose = 0.0;
		winLose.betAmount = 0.0;
		winLose.effectAmount = 0.0;
		winLose.agentProfit = 0.0;
		winLose.agentPay = 0.0;
		winLose.agentPureProfit = 0.0;
		winLose.companyProfit = 0.0;
		if ( betList != null )
		{
			for (Bet bet : betList)
			{
				winLose.winLose += bet.winLoseAmount;
				winLose.betAmount += bet.bettingAmount;
				winLose.effectAmount += (bet.bettingAmount > Math.abs(bet.winLoseAmount) ? Math.abs(bet.winLoseAmount) : bet.bettingAmount);
				winLose.agentProfit = ( winLose.betAmount * currentAgent.commissionPer + (-winLose.winLose) * currentAgent.rebate ) / 100;
				
				if ( secondAgent == null )
				{
					winLose.agentPay = 0.0;
				}
				else
				{
					winLose.agentPay = ( winLose.betAmount * secondAgent.commissionPer + (-winLose.winLose) * secondAgent.rebate ) / 100;
					
				}
				winLose.agentPureProfit = winLose.agentProfit - winLose.agentPay;
	//			winLose.companyProfit = winLose.winLose;
			}
		}
		
		return winLose;
	}

	private ListResult<Member> getAllMemberOfUser(BetSelect betSelect)
	{
		String uuid = null;
		if ( betSelect.userId.equals("manager") )
		{
			uuid = "manager";
		}
		else
		{
			User user = userDao.getUserByUuid(betSelect.userId);
			if ( user.role == Role.SUPER || user.role == Role.MANAGER || user.role == Role.QRMANAGER )
			{
				uuid = "manager";
			}
			else if ( user.role == Role.AGENT )
			{
				uuid = betSelect.userId;
			}
		}
		if ( uuid == null)
		{
			return null;
		}
		List<Member> memberList = new ArrayList<Member>();
		Queue<String> agentQueue = new LinkedList<String>();  
		agentQueue.offer(uuid);
		while ( !agentQueue.isEmpty())
		{
			String currentUserId = agentQueue.poll();
			List<Agent> agentList = agentDao.getAgentListByUser(currentUserId, null, null);
			if ( agentList != null )
			{
				for ( Agent agent : agentList )
				{
					agentQueue.offer(agent.uuid);
				}
			}
			List<Member> newMemberList = memberDao.getMemberListByUser(currentUserId, null, null);
			if ( newMemberList != null )
			{
				memberList.addAll(newMemberList);
			}
		}
		Long count = (long)memberList.size();
		List<Member> list = null;
		if ( betSelect.page * betSelect.num >  count )
		{
			list = memberList.subList((betSelect.page-1) * betSelect.num, memberList.size());
		}
		else 
		{
			list = memberList.subList((betSelect.page-1) * betSelect.num, betSelect.page * betSelect.num);
		}
		return new ListResult<Member>(count, list);
	}

	private void updateBetData()
	{
		Company company = companyDao.getCompanyInfo();
		IThirdGame thirdGame = ThirdGameFactory.create("east", null);
		List<Bet> betList = thirdGame.getGameData(company.vendorId);
		if ( betList != null && betList.size() > 0 )
		{
			for ( Bet bet : betList )
			{
				bet.uuid = UUID.randomUUID().toString();
				reportDao.insertBet(bet);
			}
			company.vendorId = betList.get(betList.size()-1).vendorId;
			companyDao.updateVendorId(company);
		}	
	}
	
	
}
