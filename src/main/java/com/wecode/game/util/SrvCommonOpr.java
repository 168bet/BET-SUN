package com.wecode.game.util;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


//final class ResponseBean
//{
//	public Object data;
//	public String info;
//	
//	public ResponseBean(Object data, String info)
//	{
//		super();
//		this.data = data;
//		this.info = info;
//	}
//}

public class SrvCommonOpr
{
    private static Logger log = Logger.getLogger(SrvCommonOpr.class);
    private static Gson gson = new GsonBuilder().serializeNulls().create();
       
    static public void setRspResultForJson(HttpServletResponse response, Object object, int status) 
    {
        response.setContentType("text/x-json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(status);
        String json = gson.toJson(object);
        try
        {
            if ( json != null )
            {
                response.getWriter().write(json);
            }
        }
        catch ( Exception e )
        {
            log.error(null, e);
        }
    }

	public static void setRspResultForError(HttpServletResponse response, String info, int status) 
	{
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
	    response.setStatus(status);
	    String json = gson.toJson(info);
	    try
	    {
	    	if ( json != null )
	        {
	    		response.getWriter().write(json);
	        }
	    }
	    catch ( Exception e )
	    {
	        log.error(null, e);
	    }
	}
}
