package com.anbang.qipai.hongbao.plan.service;

import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;
import com.anbang.qipai.hongbao.plan.dao.MemberInvitationRecordDao;

@Service
public class MemberInvitationRecordService {

	private MemberInvitationRecordDao memberInvitationRecordDao;

	public void insertMemberInvitationRecord(MemberInvitationRecord record) {
		memberInvitationRecordDao.insert(record);
	}

	public MemberInvitationRecord findMemberInvitationRecordByMemberId(String memberId, String invitationMemberId) {
		return memberInvitationRecordDao.findByMemberIdAndInvitationMemberId(memberId, invitationMemberId);
	}
}
