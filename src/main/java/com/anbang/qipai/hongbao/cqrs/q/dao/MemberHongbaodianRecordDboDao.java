package com.anbang.qipai.hongbao.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianRecordDbo;

public interface MemberHongbaodianRecordDboDao {

	void insert(MemberHongbaodianRecordDbo record);

	void remove(String id);

	long countAmountByMemberId(String memberId);

	List<MemberHongbaodianRecordDbo> findByMemberId(int page, int size, String memberId);

	List<MemberHongbaodianRecordDbo> findByMemberId(String memberId);

}
