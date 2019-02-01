package com.anbang.qipai.hongbao.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.hongbao.msg.channel.source.MemberInvitationRecordSource;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;

@EnableBinding(MemberInvitationRecordSource.class)
public class MemberInvitationRecordMsgService {
	@Autowired
	private MemberInvitationRecordSource memberInvitationRecordSource;

	public void newRecord(MemberInvitationRecord record) {
		CommonMO mo = new CommonMO();
		mo.setMsg("new record");
		mo.setData(record);
		memberInvitationRecordSource.memberInvitationRecord().send(MessageBuilder.withPayload(mo).build());
	}

	public void updateRecord(MemberInvitationRecord record) {
		CommonMO mo = new CommonMO();
		mo.setMsg("update record");
		mo.setData(record);
		memberInvitationRecordSource.memberInvitationRecord().send(MessageBuilder.withPayload(mo).build());
	}
}
