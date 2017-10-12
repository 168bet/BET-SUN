package com.wecode.game.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wecode.bean.select.BetSelect;
import com.wecode.game.bean.Child;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.WinLoseReport;
import com.wecode.game.service.ChildService;
import com.wecode.game.service.ReportService;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;

@Controller
public class ReportController
{
	private Gson gson = new GsonBuilder().serializeNulls().create();

	@Resource(name = "reportService")
	private ReportService reportService;

	@Resource(name = "childService")
	private ChildService childService;

	@RequestMapping(value = "/report/{uuid}/winlose", method = RequestMethod.GET)
	public void getBetList(HttpServletRequest request, HttpServletResponse response, @PathVariable String uuid) throws Exception
	{
		BetSelect betSelect = new BetSelect();
		String pageStr = request.getParameter("page");
		if ( pageStr != null && !"".equals(pageStr))
		{
			betSelect.page = Integer.parseInt(pageStr);
		}
		String numStr = request.getParameter("num");
		if ( numStr != null && !"".equals(numStr))
		{
			betSelect.num = Integer.parseInt(numStr);
		}
		String startTimeStr = request.getParameter("startTime");
		if ( startTimeStr != null && !"".equals(startTimeStr))
		{
			betSelect.startTime = Long.parseLong(startTimeStr);
		}
		String finishTimeStr = request.getParameter("finishTime");
		if ( finishTimeStr != null && !"".equals(finishTimeStr))
		{
			betSelect.finishTime = Long.parseLong(finishTimeStr);
		}
		String username = request.getParameter("username");
		if ( username != null && !"".equals(username))
		{
			betSelect.username = username;
		}
		Child child = childService.getChildByUuid(uuid);
		if(child != null){
			betSelect.userId = child.parentId;
		}else{
			betSelect.userId = uuid;
		}

		ListResult<WinLoseReport> result = reportService.getWinLostReport(betSelect);
		SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
	}
}
