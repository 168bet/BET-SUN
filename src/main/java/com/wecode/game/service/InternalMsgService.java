package com.wecode.game.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wecode.game.bean.InternalMsg;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.User;
import com.wecode.game.dao.InternalMsgDao;
import com.wecode.game.dao.UserDao;
import com.wecode.game.exception.PermissionNotEnoughException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.identified.InternalMsgState;

@Service("internalMsgService")
public class InternalMsgService {

	@Resource(name = "internalMsgDao")
	private InternalMsgDao internalMsgDao;
	@Resource(name = "userDao")
	private UserDao userDao;

	public InternalMsg getMsgById(String id) throws ResultToBeanException {
		return internalMsgDao.getMsgByID(id);
	}

	public boolean newMsg(InternalMsg msg) {
		User user = userDao.getUserByUuid(msg.userId);
		if (user == null) {
			return false;
		}
		msg.username = user.username;
		msg.createTime = System.currentTimeMillis();
		msg.state = InternalMsgState.UNREAD;
		return internalMsgDao.addMsg(msg);
	}

	public boolean deleteMsg(String id) {
		return internalMsgDao.deleteMsg(id);
	}

	public boolean modifyMsg(InternalMsg msg) {
		return internalMsgDao.modifyMsg(msg);
	}

	public ListResult<InternalMsg> getAllMsgs(Integer num, Integer page)
			throws PermissionNotEnoughException, ResultToBeanException {
		List<InternalMsg> msgs = internalMsgDao.getAllMsgs(num, page);
		Long count = internalMsgDao.countAllMsgs();
		return new ListResult<InternalMsg>(count, msgs);
	}

	public ListResult<InternalMsg> getUserMsgs(String userId, Integer num, Integer page)
			throws PermissionNotEnoughException, ResultToBeanException {
		List<InternalMsg> msgs = internalMsgDao.getUserMsgs(userId, num, page);
		Long count = internalMsgDao.countUserMsgs(userId);
		return new ListResult<InternalMsg>(count, msgs);
	}

	public Long getNewMsgs(String userId) {
		return internalMsgDao.countNewMsgs(userId);
	}
}
