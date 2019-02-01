package com.anbang.qipai.hongbao.plan.service;

import java.util.List;

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
}
