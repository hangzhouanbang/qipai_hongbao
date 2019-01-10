package com.anbang.qipai.hongbao.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberDbo;

public interface MemberDboDao {

	void insert(MemberDbo dbo);

	MemberDbo findByMemberId(String memberId);

	List<MemberDbo> findAllMembers();

	void updateNickname(String memebrId, String nickname);

	void updateHeadimgurl(String memberId, String headimgurl);
}
