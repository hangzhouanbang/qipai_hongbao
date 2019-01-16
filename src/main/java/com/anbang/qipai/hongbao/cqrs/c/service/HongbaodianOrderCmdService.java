package com.anbang.qipai.hongbao.cqrs.c.service;

import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderHasAlreadyExistenceException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.TimeLimitException;

public interface HongbaodianOrderCmdService {

	String createOrder(String orderId, String payerId, Long currentTime)
			throws OrderHasAlreadyExistenceException, TimeLimitException;

	String finishOrder(String orderId) throws OrderNotFoundException;

	Long queryLimitTime(String payerId);
}
