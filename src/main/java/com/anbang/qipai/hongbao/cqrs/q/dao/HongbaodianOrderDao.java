package com.anbang.qipai.hongbao.cqrs.q.dao;

import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianOrder;

public interface HongbaodianOrderDao {

	void insert(HongbaodianOrder order);

	HongbaodianOrder findById(String orderId);

	void updateStatus(String orderId, String status);

	void updateFinishTime(String orderId, long finishTime);

	double countTotalRewardNumByReceiverId(String receiverId);
}
