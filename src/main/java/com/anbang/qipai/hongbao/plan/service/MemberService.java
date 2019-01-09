package com.anbang.qipai.hongbao.plan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.plan.bean.MemberDbo;
import com.anbang.qipai.hongbao.plan.dao.MemberDboDao;

@Service
public class MemberService {

	@Autowired
	private MemberDboDao memberDboDao;

	public void insertMember(MemberDbo member) {
		memberDboDao.insert(member);
	}

	public MemberDbo findByMemberId(String memberId) {
		return memberDboDao.findByMemberId(memberId);
	}

	public List<MemberDbo> findAllMembers() {
		return memberDboDao.findAllMembers();
	}
}
