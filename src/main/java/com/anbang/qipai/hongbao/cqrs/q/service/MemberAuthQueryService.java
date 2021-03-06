package com.anbang.qipai.hongbao.cqrs.q.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.q.dao.AuthorizationDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberDbo;

@Component
public class MemberAuthQueryService {

	@Autowired
	private AuthorizationDboDao authorizationDboDao;

	@Autowired
	private MemberDboDao memberDboDao;

	public AuthorizationDbo findThirdAuthorizationDbo(String publisher, String uuid) {
		return authorizationDboDao.find(true, publisher, uuid);
	}

	public AuthorizationDbo findAuthorizationDboByMemberIdAndPublisher(String agentId, String publisher) {
		return authorizationDboDao.findAuthorizationDboByMemberIdAndPublisher(true, agentId, publisher);
	}

	public void addThirdAuth(AuthorizationDbo authDbo) {
		authorizationDboDao.save(authDbo);
	}

	public void updateMemberBaseInfo(MemberDbo member) {
		memberDboDao.updateMemberBaseInfo(member.getId(), member.getNickname(), member.getHeadimgurl(),
				member.getGender());
	}

	public void updateMemberPhone(MemberDbo member) {
		memberDboDao.updateMemberPhone(member.getId(), member.getPhone());
	}

	public void updateMemberReqIP(MemberDbo member) {
		memberDboDao.updateMemberReqIP(member.getId(), member.getReqIP());
	}

	public void insertMember(MemberDbo member) {
		memberDboDao.insert(member);
	}

	public MemberDbo findByMemberId(String memberId) {
		return memberDboDao.findByMemberId(memberId);
	}

	public List<MemberDbo> findAllMembers() {
		authorizationDboDao.updateClass();
		return memberDboDao.findAllMembers();
	}
}
