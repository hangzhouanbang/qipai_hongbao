package com.anbang.qipai.hongbao.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.plan.bean.IPVerifyDbo;
import com.anbang.qipai.hongbao.plan.dao.IPVerifyDboDao;
import com.anbang.qipai.hongbao.util.TimeUtil;

@Service
public class IPVerifyService {

	@Autowired
	private IPVerifyDboDao iPVerifyDboDao;

	public void saveIPVerifyDbo(IPVerifyDbo dbo) {
		iPVerifyDboDao.save(dbo);
	}

	public int countVerifyAmountTodayByMemberId(String memberId) {
		long date = System.currentTimeMillis();
		long startTime = TimeUtil.getDayStartTime(date);
		long endTime = TimeUtil.getDayEndTime(date);
		return (int) iPVerifyDboDao.countByTime(startTime, endTime, memberId);
	}
}
