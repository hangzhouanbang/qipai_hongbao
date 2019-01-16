package com.anbang.qipai.hongbao.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.HongbaodianOrderManager;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderHasAlreadyExistenceException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.TimeLimitException;
import com.anbang.qipai.hongbao.cqrs.c.service.HongbaodianOrderCmdService;

@Component
public class HongbaodianOrderCmdServiceImpl extends CmdServiceBase implements HongbaodianOrderCmdService {

	@Override
	public String createOrder(String orderId, String payerId, Long currentTime)
			throws OrderHasAlreadyExistenceException, TimeLimitException {
		HongbaodianOrderManager hongbaodianOrderManager = singletonEntityRepository
				.getEntity(HongbaodianOrderManager.class);
		String id = hongbaodianOrderManager.createOrder(orderId, payerId, currentTime);
		return id;
	}

	@Override
	public String finishOrder(String orderId) throws OrderNotFoundException {
		HongbaodianOrderManager hongbaodianOrderManager = singletonEntityRepository
				.getEntity(HongbaodianOrderManager.class);
		String id = hongbaodianOrderManager.finishOrder(orderId);
		return id;
	}

	@Override
	public Long queryLimitTime(String payerId) {
		HongbaodianOrderManager hongbaodianOrderManager = singletonEntityRepository
				.getEntity(HongbaodianOrderManager.class);
		long limitTime = hongbaodianOrderManager.getPayerLimitTime(payerId);
		return limitTime;
	}

}
