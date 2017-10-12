package com.wecode.game.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import com.wecode.game.bean.Game;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.Member;
import com.wecode.game.dao.GameDao;
import com.wecode.game.dao.MemberDao;
import com.wecode.game.exception.FileExistException;
import com.wecode.game.exception.GameNotExistException;
import com.wecode.game.exception.MemberNotExistException;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.third.IThirdGame;
import com.wecode.game.third.ThirdGameFactory;
import com.wecode.game.util.FileOperator;
import com.wecode.game.util.MD5;

@Service("gameService")
public class GameService {

	private static Logger log = Logger.getLogger(GameService.class);

	@Resource(name = "gameDao")
	private GameDao gameDao;

	@Resource(name = "memberDao")
	private MemberDao memberDao;

	public Game getGameById(String type, String id) throws ResultToBeanException {
		return gameDao.getGameByTypeAndID(type, id);
	}

	public boolean newGame(String type, Game game) {
		if (game.image == null) {
			game.image = "img/game-preview.png";
		} else {
			game.image = this.saveGameImage(game.image);
			if (game.image == null) {
				log.error("newGame：保存游戏\"" + game.name + "\"图片文件的时候发生错误");
				return false;
			}
		}
		return gameDao.addGame(type, game);
	}

	public boolean deleteGame(String type, String id) {
		return gameDao.deleteGame(type, id);
	}

	public boolean modifyGame(String type, Game game) {
		if (game.image != null) {
			game.image = this.saveGameImage(game.image);
			if (game.image == null) {
				log.error("modifyGame：编辑游戏\"" + game.name + "\"时发生错误");
				return false;
			}
		}
		return gameDao.modifyGame(type, game);
	}

	public ListResult<Game> getAllGames(String type, Integer num, Integer page)
			throws PermissionNotEnoughException, ResultToBeanException {
		List<Game> games = gameDao.getAllGames(type, num, page);
		Long count = gameDao.countGames(type);
		return new ListResult<Game>(count, games);
	}

	public ListResult<Game> getEnableGames(String type, Integer num, Integer page)
			throws PermissionNotEnoughException, ResultToBeanException {
		List<Game> games = gameDao.getEnableGames(type, num, page);
		Long count = gameDao.countEnableGames(type);
		return new ListResult<Game>(count, games);
	}

	public boolean disableGame(String type, String id) {
		return gameDao.enableGame(type, id, false);
	}

	public boolean enableGame(String type, String id) {
		return gameDao.enableGame(type, id, true);
	}

	private String saveGameImage(String image) {
		String md5 = MD5.GetMD5Code(image);
		byte[] content = null;
		Base64 base64 = new Base64();
		content = base64.decode(image);
		String path = "uploads/gameimages/" + md5;
		try {
			return FileOperator.saveFile(path, content);
		} catch (IOException e) {
			log.error("saveGameImage：保存图片文件\"" + md5 + "\"时发生错误", e);
			return null;
		} catch (FileExistException e) {
			return path;
		}
	}

	public Game openGame(Long id, String userId) throws Exception
	{
		Member member = memberDao.getMemberByUuid(userId);
		if ( member == null )
		{
			throw new MemberNotExistException();
		}
		IThirdGame thirdGame  = null;
		if ( member.lastGameId != null && member.lastGameId != -1 )
		{
			Game oldGame = gameDao.getGameById(member.lastGameId);
			thirdGame = ThirdGameFactory.create(oldGame.name, member);
			if ( thirdGame == null )
			{
				throw new GameNotExistException();
			}
			Double point = thirdGame.close();
			
			Member memberNew = new Member(member.uuid);
			memberNew.point = point;
			memberNew.lastGameId = -1L;
			memberDao.updateMember(memberNew);
		}
		Game game = gameDao.getGameById(id);
		thirdGame = ThirdGameFactory.create(game.name, member);
		if ( thirdGame == null )
		{
			throw new GameNotExistException();
		}
		String link = thirdGame.open();
		Member memberNew = new Member(member.uuid);
		memberNew.point = 0.0;
		memberNew.lastGameId = id;
		if( memberDao.updateMember(memberNew))
		{
			game.link = link;
			return game;
		}
		return null;
	}

	public Member getBalance(Long id, String userId) throws Exception
	{
		Member member = memberDao.getMemberByUuid(userId);
		Game game = gameDao.getGameById(id);
		IThirdGame thirdGame = ThirdGameFactory.create(game.name, member);
		if ( thirdGame == null )
		{
			throw new GameNotExistException();
		}
		Double point = thirdGame.getBalance();
		Member memberNew = new Member(member.uuid);
		memberNew.point = point;
		return memberNew;
	}
}
