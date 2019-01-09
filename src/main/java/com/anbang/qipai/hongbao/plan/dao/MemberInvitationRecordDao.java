package com.anbang.qipai.hongbao.plan.dao;

import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;

public interface MemberInvitationRecordDao {

	void insert(MemberInvitationRecord record);

	MemberInvitationRecord findByMemberIdAndInvitationMemberId(String memberId, String invitationMemberId);
}
