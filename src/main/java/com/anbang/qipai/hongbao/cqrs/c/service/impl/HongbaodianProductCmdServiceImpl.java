package com.anbang.qipai.hongbao.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianproduct.HongbaodianProductManager;
import com.anbang.qipai.hongbao.cqrs.c.service.HongbaodianProductCmdService;

@Component
public class HongbaodianProductCmdServiceImpl extends CmdServiceBase implements HongbaodianProductCmdService {

	@Override
	public Integer buyProduct(String id, Integer amount) throws Exception {
		HongbaodianProductManager hongbaodianProductManager = this.singletonEntityRepository
				.getEntity(HongbaodianProductManager.class);
		return hongbaodianProductManager.buyProduct(id, amount);
	}

	@Override
	public Integer addProduct(String id, Integer amount) throws Exception {
		HongbaodianProductManager hongbaodianProductManager = this.singletonEntityRepository
				.getEntity(HongbaodianProductManager.class);
		return hongbaodianProductManager.addProduct(id, amount);
	}

	@Override
	public void clear() throws Exception {
		HongbaodianProductManager hongbaodianProductManager = this.singletonEntityRepository
				.getEntity(HongbaodianProductManager.class);
		hongbaodianProductManager.clear();
	}

	@Override
	public void fill(String id, Integer remain) throws Exception {
		HongbaodianProductManager hongbaodianProductManager = this.singletonEntityRepository
				.getEntity(HongbaodianProductManager.class);
		hongbaodianProductManager.fill(id, remain);
	}

}
