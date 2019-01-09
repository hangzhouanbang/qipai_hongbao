package com.anbang.qipai.hongbao.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianRecordDbo;
import com.anbang.qipai.hongbao.msg.channel.source.MemberHongbaodianRecordSource;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;

@EnableBinding(MemberHongbaodianRecordSource.class)
public class HongbaodianRecordMsgService {

	@Autowired
	private MemberHongbaodianRecordSource memberHongbaodianRecordSource;

	public void newRecord(MemberHongbaodianRecordDbo dbo) {
		CommonMO mo = new CommonMO();
		mo.setMsg("new record");
		mo.setData(dbo);
		memberHongbaodianRecordSource.memberHongbaodianRecord().send(MessageBuilder.withPayload(mo).build());
	}
}
