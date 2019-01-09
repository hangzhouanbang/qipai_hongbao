package com.anbang.qipai.hongbao.plan.dao;

import com.anbang.qipai.hongbao.plan.bean.MemberLoginRecord;

public interface MemberLoginRecordDao {

	void insert(MemberLoginRecord record);

	void updateOnlineTime(String id, long onlineTime);

	MemberLoginRecord findByMemberId(String memberId);
}
