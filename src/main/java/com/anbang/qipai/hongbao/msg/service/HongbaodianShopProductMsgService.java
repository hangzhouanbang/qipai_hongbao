package com.anbang.qipai.hongbao.msg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianShopProductDbo;
import com.anbang.qipai.hongbao.msg.channel.source.HongbaodianShopProductDboSource;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;

@EnableBinding(HongbaodianShopProductDboSource.class)
public class HongbaodianShopProductMsgService {

	@Autowired
	private HongbaodianShopProductDboSource hongbaodianShopProductDboSource;

	public void release(List<HongbaodianShopProductDbo> list) {
		CommonMO mo = new CommonMO();
		mo.setMsg("release");
		mo.setData(list);
		hongbaodianShopProductDboSource.hongbaodianShopProductDbo().send(MessageBuilder.withPayload(mo).build());
	}

	public void update(HongbaodianShopProductDbo dbo) {
		CommonMO mo = new CommonMO();
		mo.setMsg("update");
		mo.setData(dbo);
		hongbaodianShopProductDboSource.hongbaodianShopProductDbo().send(MessageBuilder.withPayload(mo).build());
	}
}
