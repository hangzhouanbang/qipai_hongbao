package com.anbang.qipai.hongbao.plan.dao;

import com.anbang.qipai.hongbao.plan.bean.MemberLoginRecord;

public interface MemberLoginRecordDao {

	void save(MemberLoginRecord record);

	void updateOnlineTimeById(String id, long onlineTime);

	int countMemberNumByLoginIp(String loginIp);
}
