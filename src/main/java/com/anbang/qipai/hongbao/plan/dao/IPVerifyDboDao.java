package com.anbang.qipai.hongbao.plan.dao;

import com.anbang.qipai.hongbao.plan.bean.IPVerifyDbo;

public interface IPVerifyDboDao {

	void save(IPVerifyDbo dbo);

	long countByTime(Long startTime, Long endTime, String memberId);
}
