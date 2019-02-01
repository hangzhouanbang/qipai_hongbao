package com.anbang.qipai.hongbao.msg.receiver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.hongbao.conf.MemberInvitationRecordState;
import com.anbang.qipai.hongbao.msg.channel.sink.MemberLoginRecordSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.msg.service.MemberInvitationRecordMsgService;
import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;
import com.anbang.qipai.hongbao.plan.bean.MemberLoginRecord;
import com.anbang.qipai.hongbao.plan.service.MemberInvitationRecordService;
import com.anbang.qipai.hongbao.plan.service.MemberLoginRecordService;
import com.google.gson.Gson;

@EnableBinding(MemberLoginRecordSink.class)
public class MemberLoginRecordMsgReceiver {

	@Autowired
	private MemberLoginRecordService memberLoginRecordService;

	@Autowired
	private MemberInvitationRecordService memberInvitationRecordService;

	@Autowired
	private MemberInvitationRecordMsgService memberInvitationRecordMsgService;

	private Gson gson = new Gson();

	@StreamListener(MemberLoginRecordSink.MEMBERLOGINRECORD)
	public void memberLoginRecord(CommonMO mo) {
		String msg = mo.getMsg();
		Map map = (Map) mo.getData();
		if ("member login".equals(msg)) {
			String json = gson.toJson(map.get("record"));
			MemberLoginRecord record = gson.fromJson(json, MemberLoginRecord.class);
			memberLoginRecordService.save(record);

			MemberInvitationRecord invitation = memberInvitationRecordService
					.findMemberInvitationRecordByInvitationMemberId(record.getMemberId());
			if (invitation != null && !invitation.getState().equals(MemberInvitationRecordState.SUCCESS)) {
				invitation = memberInvitationRecordService.updateMemberInvitationRecordState(invitation.getId(),
						MemberInvitationRecordState.SUCCESS);
				memberInvitationRecordMsgService.updateRecord(invitation);
			}
		}
		if ("update member onlineTime".equals(msg)) {
			String json = gson.toJson(mo.getData());
			MemberLoginRecord record = gson.fromJson(json, MemberLoginRecord.class);
			memberLoginRecordService.updateOnlineTimeById(record.getId(), record.getOnlineTime());
		}
		if ("member logout".equals(msg)) {
			// String memberId = (String) map.get("memberId");
			// MemberLoginRecord record =
			// memberLoginRecordService.findRecentRecordByMemberId(memberId);
			// long onlineTime = record.getOnlineTime();
		}
	}
}
