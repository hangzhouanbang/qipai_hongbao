package com.anbang.qipai.hongbao.cqrs.q.dao;

import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianAccountDbo;

public interface MemberHongbaodianAccountDboDao {

	void insert(MemberHongbaodianAccountDbo dbo);

	void updateBalance(String accountId, int balance);

	void remove(String accountId);

	MemberHongbaodianAccountDbo findByMemberId(String memberId);
}
