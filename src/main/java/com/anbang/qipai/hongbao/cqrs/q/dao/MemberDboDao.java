package com.anbang.qipai.hongbao.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberDbo;

public interface MemberDboDao {

	void insert(MemberDbo dbo);

	MemberDbo findByMemberId(String memberId);

	List<MemberDbo> findAllMembers();

	void updateMemberBaseInfo(String memberId, String nickname, String headimgurl, String gender);

	void updateMemberReqIP(String memberId, String reqIP);

	void updateMemberPhone(String memberId, String phone);
}
