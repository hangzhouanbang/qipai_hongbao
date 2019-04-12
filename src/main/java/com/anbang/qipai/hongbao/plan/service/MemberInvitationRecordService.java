package com.anbang.qipai.hongbao.plan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.hongbao.web.vo.InvitationQuery;
import com.anbang.qipai.hongbao.web.vo.MemberInvitationRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.conf.MemberInvitationRecordState;
import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;
import com.anbang.qipai.hongbao.plan.dao.MemberInvitationRecordDao;
import com.highto.framework.web.page.ListPage;

@Service
public class MemberInvitationRecordService {

	@Autowired
	private MemberInvitationRecordDao memberInvitationRecordDao;

	public void insertMemberInvitationRecord(MemberInvitationRecord record) {
		memberInvitationRecordDao.insert(record);
	}

	public MemberInvitationRecord updateMemberInvitationRecordState(String id, String state) {
		memberInvitationRecordDao.updateState(id, state);
		return memberInvitationRecordDao.findById(id);
	}

	public MemberInvitationRecord findMemberInvitationRecordByMemberIdAndInvitationMemberId(String memberId,
			String invitationMemberId) {
		return memberInvitationRecordDao.findByMemberIdAndInvitationMemberId(memberId, invitationMemberId);
	}

	public MemberInvitationRecord findMemberInvitationRecordByInvitationMemberId(String invitationMemberId) {
		return memberInvitationRecordDao.findByInvitationMemberId(invitationMemberId);
	}

	public ListPage findMemberInvitationRecordByMemberId(int page, int size, String memberId) {
		long amount = memberInvitationRecordDao.countByMemberId(memberId, MemberInvitationRecordState.SUCCESS);
		List<MemberInvitationRecord> records = memberInvitationRecordDao.findByMemberId(page, size, memberId,
				MemberInvitationRecordState.SUCCESS);
		ListPage listPage = new ListPage(records, page, size, (int) amount);
		return listPage;
	}

	public ListPage listMemberInvitationRecordByQuery(InvitationQuery invitationQuery) {
		long amount = memberInvitationRecordDao.countByQuery(invitationQuery);
		List<MemberInvitationRecord> records = memberInvitationRecordDao.listByQuery(invitationQuery);


		List<MemberInvitationRecordVO> voList = new ArrayList<>();
		for (MemberInvitationRecord list : records) {
			MemberInvitationRecordVO recordVO = new MemberInvitationRecordVO();
			recordVO.setInvitationMemberId(list.getInvitationMemberId());
			recordVO.setInvitationMemberNickname(list.getInvitationMemberNickname());
			recordVO.setState(list.getState());
			recordVO.setCreateTime(list.getCreateTime());
			recordVO.setActivationTime(list.getActivationTime());
			recordVO.setCause(list.getCause());

			String invitationMemberId = list.getInvitationMemberId();
			List<MemberInvitationRecord> temps = memberInvitationRecordDao.listByQuery(new InvitationQuery(invitationMemberId));
			int validNum = 0;
			for (MemberInvitationRecord temp : temps) {
				if (MemberInvitationRecordState.SUCCESS.equals(temp.getState())) {
					validNum ++;
				}
			}
			recordVO.setValidNum(validNum);
			recordVO.setInvalidNum(temps.size() - validNum);
			voList.add(recordVO);
		}

		ListPage listPage = new ListPage(voList, invitationQuery.getPage(), invitationQuery.getSize(), (int)amount);
		return listPage;
	}

	public long countMemberInvitationRecordByMemberId(String memberId) {
		return memberInvitationRecordDao.countByMemberId(memberId, null);
	}

	public MemberInvitationRecord updateMemberInvitationRecordCause(String id, String cause) {
		memberInvitationRecordDao.updateCause(id, cause);
		return memberInvitationRecordDao.findById(id);
	}
}
