package com.wecode.game.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.PublicMsg;
import com.wecode.game.dao.PublicMsgDao;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.ResultToBeanException;

@Service("publicMsgService")
public class PublicMsgService {

	@Resource(name = "publicMsgDao")
	private PublicMsgDao publicMsgDao;

	public PublicMsg getMsgById(String type, String id) throws ResultToBeanException {
		return publicMsgDao.getMsgByTypeAndID(type, id);
	}

	public PublicMsg getLatestMsgs(String type) throws ResultToBeanException {
		return publicMsgDao.getLatestMsgs(type);
	}

	public boolean newMsg(String type, PublicMsg msg) {
		msg.createTime = System.currentTimeMillis();
		return publicMsgDao.addMsg(type, msg);
	}

	public boolean deleteMsg(String type, String id) {
		return publicMsgDao.deleteMsg(type, id);
	}

	public boolean modifyMsg(String type, PublicMsg msg) {
		return publicMsgDao.modifyMsg(type, msg);
	}

	public ListResult<PublicMsg> getAllMsgs(String type, Integer num, Integer page)
			throws PermissionNotEnoughException, ResultToBeanException {
		List<PublicMsg> msgs = publicMsgDao.getAllMsgs(type, num, page);
		Long count = publicMsgDao.countMsgs(type);
		return new ListResult<PublicMsg>(count, msgs);
	}
}
