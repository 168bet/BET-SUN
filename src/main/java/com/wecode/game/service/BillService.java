package com.wecode.game.service;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wecode.game.bean.Agent;
import com.wecode.game.bean.Bill;
import com.wecode.game.bean.ListResult;
import com.wecode.game.bean.Member;
import com.wecode.game.bean.User;
import com.wecode.game.dao.AgentDao;
import com.wecode.game.dao.BillDao;
import com.wecode.game.dao.MemberDao;
import com.wecode.game.dao.UserDao;
import com.wecode.game.exception.AcceptedBillModifyException;
import com.wecode.game.exception.BillNotExistException;
import com.wecode.game.exception.FBException;
import com.wecode.game.exception.MemberNotExistException;
import com.wecode.game.exception.MoneyPasswordIncorrectException;
import com.wecode.game.exception.NameInCorrectException;
import com.wecode.game.exception.PointNotEnoughException;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.exception.UserNotExistException;
import com.wecode.game.identified.BillState;
import com.wecode.game.identified.BillType;
import com.wecode.game.identified.Role;
import com.wecode.game.util.MD5;

@Service("billService")
public class BillService {
	@Resource(name = "billDao")
	private BillDao billDao;
	@Resource(name = "memberDao")
	private MemberDao memberDao;
	@Resource(name = "agentDao")
	private AgentDao agentDao;
	@Resource(name = "userDao")
	private UserDao userDao;

	public boolean deposite(Bill bill) throws Exception {
		bill.uuid = UUID.randomUUID().toString();
		Member member = memberDao.getMemberByUsername(bill.username);
		if (member == null) {
			throw new MemberNotExistException();
		}
		if (!member.name.equals(bill.name)) {
			throw new NameInCorrectException();
		}
		bill.createdTime = System.currentTimeMillis();
		bill.type = BillType.DEPOSITE;
		bill.userId = member.uuid;
		bill.state = BillState.CHECKED;
		if (member.privilegeNum > 0) {
			member.privilegeNum--;
			bill.point += bill.point * member.profit;
		}
		if (!billDao.deposit(bill)) {
			return false;
		}
		Member memberNew = new Member(member.uuid);
		memberNew.point = member.point + bill.point;
		memberNew.privilegeNum = member.privilegeNum;
		if (!memberDao.updateMember(memberNew)) {
			return false;
		}
		return true;
	}

	public boolean manualdeDosite(Bill bill) throws Exception {
		bill.uuid = UUID.randomUUID().toString();
		Member member = memberDao.getMemberByUsername(bill.username);
		if (member == null) {
			throw new MemberNotExistException();
		}
		bill.name = member.name;
		bill.type = BillType.DEPOSITE;
		bill.userId = member.uuid;
		bill.state = BillState.CHECKING;
		if (!billDao.deposit(bill)) {
			return false;
		}
		return true;
	}

	public boolean reject(String uuid) throws MemberNotExistException, NameInCorrectException,
			ResultToBeanException, BillNotExistException {
		Bill dbBill = billDao.getBillByUuid(uuid);
		Member member = memberDao.getMemberByUuid(dbBill.userId);
		if (dbBill == null || dbBill.type != BillType.DEPOSITE) {
			throw new BillNotExistException();
		}
		if (dbBill.state != BillState.CHECKING) {
			return false;
		}
		if (member == null) {
			throw new MemberNotExistException();
		}
		dbBill.state = BillState.REJECTED;
		if (!billDao.updateBillState(dbBill)) {
			return false;
		}
		return true;
	}

	public boolean check(String uuid) throws MemberNotExistException, NameInCorrectException,
			ResultToBeanException, BillNotExistException {
		Bill dbBill = billDao.getBillByUuid(uuid);
		Member member = memberDao.getMemberByUuid(dbBill.userId);
		if (dbBill == null || dbBill.type != BillType.DEPOSITE) {
			throw new BillNotExistException();
		}
		if (!(dbBill.state == BillState.CHECKING || dbBill.state == BillState.REJECTED)) {
			return false;
		}
		if (member == null) {
			throw new MemberNotExistException();
		}
		dbBill.state = BillState.CHECKED;
		if (member.privilegeNum > 0) {
			member.privilegeNum--;
			dbBill.point += dbBill.point * member.profit;
		}
		if (!billDao.updateBillState(dbBill)) {
			return false;
		}
		Member memberNew = new Member(member.uuid);
		memberNew.point = member.point + dbBill.point;
		memberNew.privilegeNum = member.privilegeNum;
		if (!memberDao.updateMember(memberNew)) {
			return false;
		}
		return true;
	}

	public boolean checking(String uuid) throws MemberNotExistException, NameInCorrectException,
			ResultToBeanException, BillNotExistException {
		Bill dbBill = billDao.getBillByUuid(uuid);
		Member member = memberDao.getMemberByUuid(dbBill.userId);
		if (dbBill == null || dbBill.type != BillType.DEPOSITE) {
			throw new BillNotExistException();
		}
		if (dbBill.state != BillState.REJECTED) {
			return false;
		}
		if (member == null) {
			throw new MemberNotExistException();
		}
		dbBill.state = BillState.CHECKING;
		if (!billDao.updateBillState(dbBill)) {
			return false;
		}
		return true;
	}

	public boolean withdrawals(Bill bill) throws FBException {
		bill.uuid = UUID.randomUUID().toString();
		User user = userDao.getUserByUuid(bill.userId);
		if (user == null) {
			throw new UserNotExistException();
		}
		if (bill.password == null) {
			throw new MoneyPasswordIncorrectException();
		}
		Double point = null;
		bill.password = MD5.GetMD5Code(bill.password);
		if (user.role == Role.MEMBER) {
			Member member = memberDao.getMemberByUuid(bill.userId);
			if (!bill.password.equals(member.moneyPwd)) {
				throw new MoneyPasswordIncorrectException();
			}
			point = member.point;
		} else if (user.role == Role.AGENT) {
			Agent agent = agentDao.getAgentByUuid(bill.userId);
			if (!bill.password.equals(agent.moneyPwd)) {
				throw new MoneyPasswordIncorrectException();
			}
			point = agent.point;
		}

		if (point == null || point < bill.point) {
			throw new PointNotEnoughException();
		}
		bill.type = BillType.WITHDRAWALS;
		bill.info = bill.info;
		bill.state = BillState.CHECKING;
		bill.createdTime = System.currentTimeMillis();
		if (!billDao.withdrawals(bill)) {
			return false;
		}
		return true;
	}

	public boolean checkResult(Bill bill) throws ResultToBeanException, AcceptedBillModifyException {
		Bill billDB = billDao.getBillByUuid(bill.uuid);
		if (billDB.state == BillState.CHECKED) {
			throw new AcceptedBillModifyException();
		}
		if (!billDao.updateBillState(bill)) {
			return false;
		}
		if (bill.state == BillState.CHECKED) {
			Member member = memberDao.getMemberByUuid(bill.userId);
			Member newMember = new Member(member.uuid);
			newMember.point = member.point - bill.point;
			if (!memberDao.updateMember(newMember)) {
				return false;
			}
			return true;
		}
		return true;
	}

	public ListResult<Bill> getBillListOfWithdrawals(Integer state, Integer role, Integer page,
			Integer num) {
		List<Bill> billList = billDao.getBillListOfWithdrawals(state, role, page, num);
		Long count = billDao.computeBillsOfWithdrawals(state);
		return new ListResult<Bill>(count, billList);
	}

	public ListResult<Bill> getBillListOfDeposite(String memberId, Integer page, Integer num) {
		List<Bill> billList = billDao.getBillListOfDeposite(memberId, page, num);
		Long count = billDao.computeBillsOfDeposite(memberId);
		return new ListResult<Bill>(count, billList);
	}

	public ListResult<Bill> getDepositesByState(Integer state, Integer page, Integer num) {
		List<Bill> billList = billDao.getDepositesByState(state, page, num);
		Long count = billDao.countDepositesByState(state);
		return new ListResult<Bill>(count, billList);
	}

	public ListResult<Bill> getMemberBills(String memberId, Integer page, Integer num) {
		List<Bill> billList = billDao.getMemberBills(memberId, page, num);
		Long count = billDao.countMemberBills(memberId);
		return new ListResult<Bill>(count, billList);
	}

}
