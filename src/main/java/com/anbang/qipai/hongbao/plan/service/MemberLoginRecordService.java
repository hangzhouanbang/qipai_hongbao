package com.anbang.qipai.hongbao.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.plan.bean.MemberLoginRecord;
import com.anbang.qipai.hongbao.plan.dao.MemberLoginRecordDao;

@Service
public class MemberLoginRecordService {

	@Autowired
	private MemberLoginRecordDao memberLoginRecordDao;

	public void insert(MemberLoginRecord record) {
		memberLoginRecordDao.insert(record);
	}

	public void updateOnlineTime(String id, long onlineTime) {
		memberLoginRecordDao.updateOnlineTime(id, onlineTime);
	}

	public MemberLoginRecord findByMemberId(String memberId) {
		return memberLoginRecordDao.findByMemberId(memberId);
	}
}
