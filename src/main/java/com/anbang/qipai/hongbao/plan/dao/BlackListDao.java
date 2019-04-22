package com.anbang.qipai.hongbao.plan.dao;

import com.anbang.qipai.hongbao.plan.bean.BlackList;

public interface BlackListDao {

	void save(BlackList blackList);

	BlackList findByPlayerId(String playerId);

	BlackList findById(String id);

	void removeByIds(String[] ids);
}
