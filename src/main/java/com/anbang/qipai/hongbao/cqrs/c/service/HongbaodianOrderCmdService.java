package com.anbang.qipai.hongbao.cqrs.c.service;

import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderHasAlreadyExistenceException;

public interface HongbaodianOrderCmdService {

	String createOrder(String orderId) throws OrderHasAlreadyExistenceException;

	String finishOrder(String orderId);
}
