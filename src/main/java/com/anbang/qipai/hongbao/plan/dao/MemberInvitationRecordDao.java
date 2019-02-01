package com.anbang.qipai.hongbao.plan.dao;

import java.util.List;

import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;

public interface MemberInvitationRecordDao {

	void insert(MemberInvitationRecord record);

	void updateState(String id, String state);

	MemberInvitationRecord findByMemberIdAndInvitationMemberId(String memberId, String invitationMemberId);

	MemberInvitationRecord findByInvitationMemberId(String invitationMemberId);

	MemberInvitationRecord findById(String id);

	List<MemberInvitationRecord> findByMemberId(int page, int size, String memberId, String state);

	long countByMemberId(String memberId, String state);
}
