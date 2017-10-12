package com.wecode.game.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wecode.game.bean.Game;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.Member;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.service.GameService;
import com.wecode.game.util.HTTP_RESPONCES_INFO;
import com.wecode.game.util.SrvCommonOpr;

@Controller
public class GameController {

	private Gson gson = new GsonBuilder().serializeNulls().create();
	@Resource(name = "gameService")
	private GameService gameService;

	@RequestMapping(value = "/game/{type}/{id}", method = RequestMethod.GET)
	public void getGameById(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String id, @PathVariable String type) {
		Game game = null;
		try {
			game = gameService.getGameById(type, id);
		} catch (ResultToBeanException e) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
			return;
		}
		if (game == null) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.NOT_FIND_AGENT_MSG,
					HTTP_RESPONCES_INFO.NOT_FIND_AGENT);
			return;
		}
		SrvCommonOpr.setRspResultForJson(response, game, HTTP_RESPONCES_INFO.SUCCESS);
	}

	@RequestMapping(value = "/game/{type}/all", method = RequestMethod.GET)
	public void getAllGames(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String type) {
		String numStr = request.getParameter("num");
		Integer num = null;
		if (numStr != null) {
			num = Integer.parseInt(numStr);
		}

		String pageStr = request.getParameter("page");
		Integer page = null;
		if (pageStr != null) {
			page = Integer.parseInt(pageStr);
		}

		try {
			ListResult<Game> result = gameService.getAllGames(type, num, page);

			SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
		} catch (PermissionNotEnoughException e1) {
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);
		} catch (ResultToBeanException e1) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
	@RequestMapping(value = "/game/{type}/enable", method = RequestMethod.GET)
	public void getEnableGames(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String type) {
		String numStr = request.getParameter("num");
		Integer num = null;
		if (numStr != null) {
			num = Integer.parseInt(numStr);
		}

		String pageStr = request.getParameter("page");
		Integer page = null;
		if (pageStr != null) {
			page = Integer.parseInt(pageStr);
		}

		try {
			ListResult<Game> result = gameService.getEnableGames(type, num, page);

			SrvCommonOpr.setRspResultForJson(response, result, HTTP_RESPONCES_INFO.SUCCESS);
		} catch (PermissionNotEnoughException e1) {
			SrvCommonOpr.setRspResultForError(response,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH_MSG,
					HTTP_RESPONCES_INFO.PERMISSION_NOT_ENOUGH);
		} catch (ResultToBeanException e1) {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
	
	@RequestMapping(value = "/game/{type}", method = RequestMethod.POST)
	public void newGame(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String json, @PathVariable String type) {
		Game game = gson.fromJson(json, Game.class);

		if (gameService.newGame(type, game)) {
			SrvCommonOpr.setRspResultForJson(response, game, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	@RequestMapping(value = "/game/{type}/{id}", method = RequestMethod.DELETE)
	public void deleteGame(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String id, @PathVariable String type) {
		if (gameService.deleteGame(type, id)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}

	@RequestMapping(value = "/game/{type}/{id}", method = RequestMethod.PUT)
	public void modifyGame(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String type, @PathVariable String id, @RequestBody String json) {
		Game game = gson.fromJson(json, Game.class);
		game.id = Long.parseLong(id);

		if (gameService.modifyGame(type, game)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
	
	@RequestMapping(value = "/game/{type}/{id}/disable", method = RequestMethod.PUT)
	public void disableGame(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String type, @PathVariable String id) {
		if (gameService.disableGame(type, id)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
	@RequestMapping(value = "/game/{type}/{id}/enable", method = RequestMethod.PUT)
	public void enableGame(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String type, @PathVariable String id) {
		if (gameService.enableGame(type, id)) {
			SrvCommonOpr.setRspResultForJson(response, null, HTTP_RESPONCES_INFO.SUCCESS);
		} else {
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
	@RequestMapping(value = "/game/{id}/open", method = RequestMethod.GET)
	public void openGame(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id)
	{
		String userId = request.getHeader("uuid");
		Game game;
		try 
		{
			game = gameService.openGame(id, userId);
			if ( game != null)
			{
				SrvCommonOpr.setRspResultForJson(response, game, HTTP_RESPONCES_INFO.SUCCESS);
			}
			else
			{
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (Exception e) 
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
	
	@RequestMapping(value = "/game/{id}/balance", method = RequestMethod.GET)
	public void getBalance(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id)
	{
		String userId = request.getHeader("uuid");
		Member member;
		try 
		{
			member = gameService.getBalance(id, userId);
			if ( member != null)
			{
				SrvCommonOpr.setRspResultForJson(response, member, HTTP_RESPONCES_INFO.SUCCESS);
			}
			else
			{
				SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
						HTTP_RESPONCES_INFO.UNKOWN);
			}
		} catch (Exception e) 
		{
			SrvCommonOpr.setRspResultForError(response, HTTP_RESPONCES_INFO.UNKNOWN_MSG,
					HTTP_RESPONCES_INFO.UNKOWN);
		}
	}
}
