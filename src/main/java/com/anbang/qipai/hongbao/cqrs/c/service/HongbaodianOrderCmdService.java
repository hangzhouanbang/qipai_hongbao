package com.anbang.qipai.hongbao.cqrs.c.service;

import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderHasAlreadyExistenceException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderNotFoundException;

public interface HongbaodianOrderCmdService {

	String createOrder(String orderId) throws OrderHasAlreadyExistenceException;

	String finishOrder(String orderId) throws OrderNotFoundException;
}
