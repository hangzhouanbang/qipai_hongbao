package com.anbang.qipai.hongbao.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianProduct;
import com.anbang.qipai.hongbao.msg.channel.source.HongbaodianProductSource;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;

@EnableBinding(HongbaodianProductSource.class)
public class HongbaodianProductMsgService {

	@Autowired
	private HongbaodianProductSource hongbaodianProductSource;

	public void addHongbaodianProduct(HongbaodianProduct product) {
		CommonMO mo = new CommonMO();
		mo.setMsg("add product");
		mo.setData(product);
		hongbaodianProductSource.hongbaodianProduct().send(MessageBuilder.withPayload(mo).build());
	}

	public void updateHongbaodianProduct(HongbaodianProduct product) {
		CommonMO mo = new CommonMO();
		mo.setMsg("update product");
		mo.setData(product);
		hongbaodianProductSource.hongbaodianProduct().send(MessageBuilder.withPayload(mo).build());
	}

	public void removeHongbaodianProduct(String productId) {
		CommonMO mo = new CommonMO();
		mo.setMsg("remove product");
		mo.setData(productId);
		hongbaodianProductSource.hongbaodianProduct().send(MessageBuilder.withPayload(mo).build());
	}
}
