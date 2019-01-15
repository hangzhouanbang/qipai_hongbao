package com.anbang.qipai.hongbao.plan.dao;

import java.util.List;

import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;

public interface MemberInvitationRecordDao {

	void insert(MemberInvitationRecord record);

	MemberInvitationRecord findByMemberIdAndInvitationMemberId(String memberId, String invitationMemberId);

	MemberInvitationRecord findByInvitationMemberId(String invitationMemberId);

	List<MemberInvitationRecord> findByMemberId(int page, int size, String memberId);

	long countByMemberId(String memberId);
}
