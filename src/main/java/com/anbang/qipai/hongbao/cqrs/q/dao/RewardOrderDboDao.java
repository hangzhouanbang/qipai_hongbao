package com.anbang.qipai.hongbao.cqrs.q.dao;

import com.anbang.qipai.hongbao.cqrs.q.dbo.RewardOrderDbo;

public interface RewardOrderDboDao {
	void insert(RewardOrderDbo order);

	RewardOrderDbo findById(String orderId);

	void updateStatus(String orderId, String status);

	void updateFinishTime(String orderId, long finishTime);
}
