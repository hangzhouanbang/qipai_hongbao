package com.anbang.qipai.hongbao.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.c.service.HongbaodianProductCmdService;
import com.anbang.qipai.hongbao.cqrs.c.service.impl.HongbaodianProductCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;
import com.highto.framework.ddd.NullObj;

@Component(value = "hongbaodianProductCmdService")
public class DisruptorHongbaodianProductCmdService extends DisruptorCmdServiceBase
		implements HongbaodianProductCmdService {

	@Autowired
	private HongbaodianProductCmdServiceImpl hongbaodianProductCmdServiceImpl;

	@Override
	public Integer buyProduct(String id, Integer amount) throws Exception {
		CommonCommand cmd = new CommonCommand(HongbaodianProductCmdServiceImpl.class.getName(), "buyProduct", id,
				amount);
		DeferredResult<Integer> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			Integer remain = hongbaodianProductCmdServiceImpl.buyProduct(cmd.getParameter(), cmd.getParameter());
			return remain;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Integer addProduct(String id, Integer amount) throws Exception {
		CommonCommand cmd = new CommonCommand(HongbaodianProductCmdServiceImpl.class.getName(), "addProduct", id,
				amount);
		DeferredResult<Integer> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			Integer remain = hongbaodianProductCmdServiceImpl.addProduct(cmd.getParameter(), cmd.getParameter());
			return remain;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void clear() throws Exception {
		CommonCommand cmd = new CommonCommand(HongbaodianProductCmdServiceImpl.class.getName(), "clear");
		DeferredResult<NullObj> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			hongbaodianProductCmdServiceImpl.clear();
			return new NullObj();
		});
		try {
			result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void fill(String id, Integer remain) throws Exception {
		CommonCommand cmd = new CommonCommand(HongbaodianProductCmdServiceImpl.class.getName(), "fill", id, remain);
		DeferredResult<NullObj> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			hongbaodianProductCmdServiceImpl.fill(cmd.getParameter(), cmd.getParameter());
			return new NullObj();
		});
		try {
			result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

}
