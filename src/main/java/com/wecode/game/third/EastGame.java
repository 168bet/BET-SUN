package com.wecode.game.third;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.wecode.game.bean.Bet;
import com.wecode.game.bean.Member;
import com.wecode.game.exception.UnknownException;
import com.wecode.game.util.MD5;
import com.wecode.game.util.yjpay.Base64;

public class EastGame implements IThirdGame 
{
	
	
	private static Logger log = Logger.getLogger(EastGame.class);
	
//	private MemberDao memberDao = new MemberDao();
	
	private Member member;
	private String agent = "dailishang";
    private String password = "aaa111222";

//    private String ComKey = "5F1F3C537DEFFE58";
    private String UserKey = "1111";
    private String testUrl = "http://cashapi.ss838.com/cashapi/DoBusiness.aspx";
//    private String reRS ="";
    
	public EastGame(Member member)
	{
		this.member = member;
	}
	
	@Override
	public Double close() throws Exception
	{
		Double point = getBalance();
		if ( point == null )
		{
			throw new UnknownException();
		}
		if ( !downPoint(point) )
		{
			throw new UnknownException();			
		}
		return point;
	}
	private boolean downPoint(Double point) 
	{
		try
		{
			Random rd = new Random();
			String now = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
	        String ordernum = "O" + now + (rd.nextInt(8999)+1000);
	        String param = "agent=" + agent + "$username=" + member.username + "$password=" + password + "$billno=" + point + "$type=OUT$credit=" + member.point + "$method=ptc";
	        param = new String(Base64.encode(param.getBytes("UTF-8")));
			String key = MD5.GetMD5Code(param + UserKey);
			String url = testUrl + "?params=" + param + "&key=" + key;
			String text = getResponse(url);
			String result = getResult(text);
			if ( result.equals("1") )
			{
				param = "agent=" + agent + "$username=" + member.username + "$password=" + password + "$billno=" + ordernum + "$type=OUT$credit="+ point +"$flag=1" + "$method=ctc";
				param = new String(Base64.encode(param.getBytes("UTF-8")));	
				key = MD5.GetMD5Code(param + UserKey);
				url = testUrl + "?params=" + param + "&key=" + key;
				text = getResponse(url);
				result = getResult(text);
				if ( result.equals("1") || result.equals("0") )
				{
					return true;
				}
			}
		}
		catch(Exception e )
		{
			log.error("东方游戏: 下分失败", e);			
		}
		return false;
	}

	@Override
	public Double getBalance() 
	{
		try
		{
			String param = "agent=" + agent + "$username=" + member.username + "$password=" + password + "$method=gb";
			param = new String(Base64.encode(param.getBytes("UTF-8")));
			String key = MD5.GetMD5Code(param + UserKey);
	        String url = testUrl + "?params=" + param + "&key=" + key;
	        String text = getResponse(url);
	        String result = getResult(text);
	        return Double.parseDouble(result); 
		}
		catch(Exception e)
		{
			log.error("东方游戏 : 查询余额失败");
		}
		return null;
	}

	@Override
	public String open() throws Exception
	{
		if ( !CheckAndCreateAccount() )
		{
			throw new UnknownException();
		}
		if ( !upperPoint() )
		{
			throw new UnknownException();
		}
		String param = "agent=" + agent + "$username=" + member.username + "$password=" + password + "$domain=video.ss838.com$gametype=1$gamekind=0$method=tg$platformname=oriental";
        param = new String(Base64.encode(param.getBytes("UTF-8"))); 
        String key = MD5.GetMD5Code(param + UserKey);  
        String url = testUrl + "?params=" + param + "&key=" + key;
		return url;
	}
	
	@Override
	public List<Bet> getGameData(Long vendorid)
	{
		try
		{
	        String testUrl = "http://cashapi.ss838.com/cashapi/GetData.aspx";
	        List<Bet> betList = new ArrayList<Bet>();
	        while (true)
	        {
		        String param = "agent=" + agent + "$vendorid=" + vendorid + "$method=gbrbv";
		        param = new String(Base64.encode(param.getBytes("UTF-8")));
		        String key = MD5.GetMD5Code(param + UserKey);
		        String url = testUrl + "?params=" + param + "&key=" + key;
		        String text = getResponse(url);
		        List<Bet> newBetList = getBetList(text);
		        betList.addAll(newBetList);
		        if ( newBetList == null || newBetList.size() < 200)
		        {
		        	break;
		        }
		        vendorid = betList.get(betList.size()-1).vendorId;
			}
	        return betList;
		}
		catch (Exception e)
		{
			
		}
		return null;
	}
	
	

	public boolean CheckAndCreateAccount() 
	{
		try
		{
			String param = "agent=" + agent + "$username=" + member.username + "$password=" + password + "$limit=1,1,1,1,1,1,1,1,1,1,1,1,0$limitvideo=3$limitroulette=5$method=caca";
			param = new String(Base64.encode(param.getBytes("UTF-8")));
			String key = MD5.GetMD5Code(param + UserKey);
	        String url = testUrl + "?params=" + param + "&key=" + key;
	        String text = getResponse(url);
	        String result = getResult(text);
	        if ( result.equals("1") || result.equals("0") )
	        {
	        	return true;
	        }
		}
		catch (Exception e)
		{
			log.error("东方游戏：创建账号失败", e);
		}
		return false;
	}
	
	public boolean upperPoint() throws Exception
	{
		try
		{
			Random rd = new Random();
			String now = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
	        String ordernum = "I" + now + (rd.nextInt(8999)+1000);
	        String param = "agent=" + agent + "$username=" + member.username + "$password=" + password + "$billno=" + ordernum + "$type=IN$credit=" + member.point + "$method=ptc";
	        param = new String(Base64.encode(param.getBytes("UTF-8")));
			String key = MD5.GetMD5Code(param + UserKey);
			String url = testUrl + "?params=" + param + "&key=" + key;
			String text = getResponse(url);
			String result = getResult(text);
			if ( result.equals("1") )
			{
				param = "agent=" + agent + "$username=" + member.username + "$password=" + password + "$billno=" + ordernum + "$type=IN$credit="+ member.point +"$flag=1" + "$method=ctc";
				param = new String(Base64.encode(param.getBytes("UTF-8")));	
				key = MD5.GetMD5Code(param + UserKey);
				url = testUrl + "?params=" + param + "&key=" + key;
		
				text = getResponse(url);
				result = getResult(text);
				if ( result.equals("1") || result.equals("0") )
				{
					return true;
				}
			}
		}
		catch(Exception e )
		{
			log.error("东方游戏: 上分失败", e);			
		}
		return false;
	}
	
	private String  getResponse(String path) throws IOException
	{
		HttpClient client = new HttpClient();
		StringBuilder sb = new StringBuilder();
		InputStream ins = null;
		// Create a method instance.
		GetMethod method = new GetMethod(path);
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);
			if (statusCode == 200) {
				ins = method.getResponseBodyAsStream();
				byte[] b = new byte[1024];
				int r_len = 0;
				while ((r_len = ins.read(b)) > 0) {
					sb.append(new String(b, 0, r_len, method.getResponseCharSet()));
				}
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
		} finally {
			method.releaseConnection();
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					System.err.println("Fatal IOException: " + e.getMessage());
				}
			}
		}
    
		return sb.toString();
	}
	
	private String getResult(String text) throws DocumentException
	{
		Document document = DocumentHelper.parseText(text);
		Element rootElm = document.getRootElement();
		String result = rootElm.getText();
		return result;
	}
	
	private List<Bet> getBetList(String text) throws DocumentException
	{
		Document document = DocumentHelper.parseText(text);
		Element rootElm = document.getRootElement();
		List<Bet> betList = new ArrayList<Bet>();
		int i = 0;
		for ( Iterator resultIt = rootElm.elementIterator(); resultIt.hasNext(); )
		{
			Element data = (Element)resultIt.next();
			betList.add(stringArrayToBet(data));
		}
		return betList;
	}
	
	private Bet stringArrayToBet(Element data)
	{
		Bet bet = new Bet();
		Iterator it = data.elementIterator();
		bet.productId = Long.parseLong(((Element)it.next()).getText());
		bet.username = ((Element)it.next()).getText();
		bet.gameRecordId = Long.parseLong(((Element)it.next()).getText());
		bet.orderNumber = Long.parseLong(((Element)it.next()).getText());
		bet.tableId = Integer.parseInt(((Element)it.next()).getText());
		bet.stage = Integer.parseInt(((Element)it.next()).getText());
		bet.inning =  Integer.parseInt(((Element)it.next()).getText());
		bet.gameNameId =  Integer.parseInt(((Element)it.next()).getText());
		bet.gameBettingKind =  Integer.parseInt(((Element)it.next()).getText());
		bet.resultType =  Integer.parseInt(((Element)it.next()).getText());
		bet.bettingAmount = Double.parseDouble(((Element) it.next()).getText());
		bet.compensateRate = Double.parseDouble(((Element) it.next()).getText());
		bet.winLoseAmount = Double.parseDouble(((Element) it.next()).getText());
		bet.balance = Double.parseDouble(((Element) it.next()).getText());
		bet.addTime = changeTimeToLong(((Element) it.next()).getText());
		bet.platformId = Integer.parseInt(((Element)it.next()).getText());
		bet.vendorId = Long.parseLong(((Element)it.next()).getText());
		return bet;
	}

	private Long changeTimeToLong(String text)
	{
		try
		{
			SimpleDateFormat sim=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			Date d=sim.parse(text);
			return d.getTime();
		}
		catch (Exception e)
		{
			log.error("change date to long fail:", e);
		}
		return null;
	}

	//	private String inputStream2String(InputStream in) throws IOException
//	{
//		StringBuffer out = new StringBuffer();
//		byte[] b = new byte[4096];
//		for (int n; (n = in.read(b)) != -1;)
//		{
//			out.append(new String(b, 0, n));
//		}
//		return out.toString();
//	}
	public static void main(String[] args) 
	{
		IThirdGame thirdGame = ThirdGameFactory.create("east", null);
	//	thirdGame.getGameData();
	}
	}