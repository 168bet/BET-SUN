package com.wecode.game.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.wecode.game.util.MD5;
import com.wecode.game.util.yjpay.Base64;

public class GameTest
{
	private static String agent = "dailishang";
	private static String username = "testMember1"; // 如果创建成功就换一个账号
	private static String password = "aaa111222";

//	private static String ComKey = "5F1F3C537DEFFE58";
	private static String UserKey = "1111";
	private static String testUrl = "http://cashapi.ss838.com/cashapi/DoBusiness.aspx";
//	private static String reRS = "";
	private static String paramstr = "";
	private static String key = "";

	public static void main(String[] args) throws Exception
	{
		/*String param = "agent="
				+ agent
				+ "$username="
				+ username
				+ "$password="
				+ password
				+ "$limit=1,1,1,1,1,1,1,1,1,1,1,1,0$limitvideo=3$limitroulette=5$method=caca";
		// String param = "agent=" + agent + "$username=" + username +
		// "$password=" + password +
		// "$domain=video.ss838.com$gametype=1$gamekind=0$method=tg$platformname=oriental";
		paramstr = new String(Base64.encode(param.getBytes("UTF-8")));
		key = MD5.GetMD5Code(paramstr + UserKey);
		String abc = testUrl + "?params=" + paramstr + "&key=" + key;
		
		URL url = new URL(abc);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		System.out.println(connection.getResponseCode());
		InputStream in = connection.getInputStream();
		String xmlText = inputStream2String(in);
		Document document = DocumentHelper.parseText(xmlText);
		Element rootElm = document.getRootElement();
//		Element memberElm = document.elementByID("result");
		String result = rootElm.getText();
		System.out.println("result : " + result);
		// 断开连接

		connection.disconnect();*/
		String testUrl = "http://cashapi.ss838.com/cashapi/GetData.aspx";
        String param = "agent=" + agent + "$vendorid=1$method=gbrbv";
        param = new String(Base64.encode(param.getBytes("UTF-8")));
        String key = MD5.GetMD5Code(param + UserKey);
        String url = testUrl + "?params=" + param + "&key=" + key;
        String text = getResponse(url);
        System.out.println(text);
        Document document = DocumentHelper.parseText(text);
		Element rootElm = document.getRootElement();
		int i = 0;
		for ( Iterator resultIt = rootElm.elementIterator(); resultIt.hasNext(); )
		{
			Element data = (Element)resultIt.next();

		}
	}
	
	private static String  getResponse(String path) throws IOException
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


	public static String inputStream2String(InputStream in) throws IOException
	{
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;)
		{
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}
}
