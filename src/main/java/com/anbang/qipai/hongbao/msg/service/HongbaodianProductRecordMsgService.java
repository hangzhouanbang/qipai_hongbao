package com.anbang.qipai.hongbao.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.hongbao.msg.channel.source.HongbaodianProductRecordSource;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.plan.bean.HongbaodianProductRecord;

@EnableBinding(HongbaodianProductRecordSource.class)
public class HongbaodianProductRecordMsgService {

	@Autowired
	private HongbaodianProductRecordSource hongbaodianProductRecordSource;

	public void addRecord(HongbaodianProductRecord record) {
		CommonMO mo = new CommonMO();
		mo.setMsg("add record");
		mo.setData(record);
		hongbaodianProductRecordSource.hongbaodianProductRecord().send(MessageBuilder.withPayload(mo).build());
	}

	public void finishRecord(HongbaodianProductRecord record) {
		CommonMO mo = new CommonMO();
		mo.setMsg("finish record");
		mo.setData(record);
		hongbaodianProductRecordSource.hongbaodianProductRecord().send(MessageBuilder.withPayload(mo).build());
	}
}
