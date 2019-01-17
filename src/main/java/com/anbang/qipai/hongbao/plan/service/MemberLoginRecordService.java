package com.anbang.qipai.hongbao.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.plan.bean.MemberLoginRecord;
import com.anbang.qipai.hongbao.plan.dao.MemberLoginRecordDao;

@Service
public class MemberLoginRecordService {

	@Autowired
	private MemberLoginRecordDao memberLoginRecordDao;

	public void save(MemberLoginRecord record) {
		memberLoginRecordDao.save(record);
	}

	public void updateOnlineTimeById(String id, long onlineTime) {
		memberLoginRecordDao.updateOnlineTimeById(id, onlineTime);
	}

	public int countMemberNumByLoginIp(String loginIp) {
		return memberLoginRecordDao.countMemberNumByLoginIp(loginIp);
	}
}
