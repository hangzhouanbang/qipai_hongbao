package com.anbang.qipai.hongbao.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderHasAlreadyExistenceException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.TimeLimitException;
import com.anbang.qipai.hongbao.cqrs.c.service.HongbaodianOrderCmdService;
import com.anbang.qipai.hongbao.cqrs.c.service.impl.HongbaodianOrderCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "hongbaodianOrderCmdService")
public class DisruptorHongbaodianOrderCmdService extends DisruptorCmdServiceBase implements HongbaodianOrderCmdService {

	@Autowired
	private HongbaodianOrderCmdServiceImpl hongbaodianOrderCmdServiceImpl;

	@Override
	public String createOrder(String orderId, String payerId, Long currentTime)
			throws OrderHasAlreadyExistenceException, TimeLimitException {
		CommonCommand cmd = new CommonCommand(HongbaodianOrderCmdServiceImpl.class.getName(), "createOrder", orderId,
				payerId, currentTime);
		DeferredResult<String> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			String id = hongbaodianOrderCmdServiceImpl.createOrder(cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter());
			return id;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof OrderHasAlreadyExistenceException) {
				throw (OrderHasAlreadyExistenceException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public String finishOrder(String orderId) throws OrderNotFoundException {
		CommonCommand cmd = new CommonCommand(HongbaodianOrderCmdServiceImpl.class.getName(), "finishOrder", orderId);
		DeferredResult<String> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			String id = hongbaodianOrderCmdServiceImpl.finishOrder(cmd.getParameter());
			return id;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof OrderNotFoundException) {
				throw (OrderNotFoundException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public Long queryLimitTime(String payerId) {
		CommonCommand cmd = new CommonCommand(HongbaodianOrderCmdServiceImpl.class.getName(), "queryLimitTime",
				payerId);
		DeferredResult<Long> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			Long limitTime = hongbaodianOrderCmdServiceImpl.queryLimitTime(cmd.getParameter());
			return limitTime;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
