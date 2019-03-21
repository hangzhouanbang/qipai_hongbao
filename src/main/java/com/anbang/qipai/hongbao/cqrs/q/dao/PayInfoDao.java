package com.anbang.qipai.hongbao.cqrs.q.dao;

import java.util.Map;

import com.anbang.qipai.hongbao.cqrs.q.dbo.PayInfo;

public interface PayInfoDao {

	void insert(PayInfo info);

	PayInfo findByOrderId(String orderId);

	void updateQueryParamsByOrderId(String orderId, Map queryParams);

	void updateReturnParamsByOrderId(String orderId, Map returnParams);

	void updateFinishTime(String orderId, long finishTime);
}
