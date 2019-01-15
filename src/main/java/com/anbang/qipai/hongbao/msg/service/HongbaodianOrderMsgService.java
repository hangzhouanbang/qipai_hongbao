package com.anbang.qipai.hongbao.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianOrder;
import com.anbang.qipai.hongbao.msg.channel.source.HongbaodianOrderSource;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;

@EnableBinding(HongbaodianOrderSource.class)
public class HongbaodianOrderMsgService {
	@Autowired
	private HongbaodianOrderSource hongbaodianOrderSource;

	public void recordHongbaodianOrder(HongbaodianOrder order) {
		CommonMO mo = new CommonMO();
		mo.setMsg("add order");
		mo.setData(order);
		hongbaodianOrderSource.hongbaodianOrder().send(MessageBuilder.withPayload(mo).build());
	}

	public void finishHongbaodianOrder(HongbaodianOrder order) {
		CommonMO mo = new CommonMO();
		mo.setMsg("finish order");
		mo.setData(order);
		hongbaodianOrderSource.hongbaodianOrder().send(MessageBuilder.withPayload(mo).build());
	}
}
