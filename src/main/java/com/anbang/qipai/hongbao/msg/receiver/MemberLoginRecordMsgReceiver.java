package com.anbang.qipai.hongbao.msg.receiver;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.hongbao.conf.MemberInvitationRecordState;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.hongbao.msg.channel.sink.MemberLoginRecordSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.msg.service.MemberInvitationRecordMsgService;
import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;
import com.anbang.qipai.hongbao.plan.bean.MemberLoginRecord;
import com.anbang.qipai.hongbao.plan.service.MemberInvitationRecordService;
import com.anbang.qipai.hongbao.plan.service.MemberLoginRecordService;
import com.google.gson.Gson;

import javafx.util.Pair;

@EnableBinding(MemberLoginRecordSink.class)
public class MemberLoginRecordMsgReceiver {

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private MemberLoginRecordService memberLoginRecordService;

	@Autowired
	private MemberInvitationRecordService memberInvitationRecordService;

	@Autowired
	private MemberInvitationRecordMsgService memberInvitationRecordMsgService;

	private ExecutorService executorService = Executors.newCachedThreadPool();

	private Gson gson = new Gson();

	@StreamListener(MemberLoginRecordSink.MEMBERLOGINRECORD)
	public void memberLoginRecord(CommonMO mo) {
		String msg = mo.getMsg();
		Map map = (Map) mo.getData();
		if ("member login".equals(msg)) {
			String json = gson.toJson(map.get("record"));
			MemberLoginRecord record = gson.fromJson(json, MemberLoginRecord.class);
			memberLoginRecordService.save(record);
			executorService.submit(() -> {
				invitation(record);
			});
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

	private void invitation(MemberLoginRecord record) {
		MemberInvitationRecord invitation = memberInvitationRecordService
				.findMemberInvitationRecordByInvitationMemberId(record.getMemberId());
		MemberDbo invitateMember = memberAuthQueryService.findByMemberId(record.getMemberId());
		if (invitateMember != null && !StringUtil.isBlank(invitateMember.getReqIP()) && invitation != null
				&& !invitation.getState().equals(MemberInvitationRecordState.SUCCESS)) {
			Pair<Integer, String> pair = verifyReqIP(invitateMember.getReqIP(), record.getIpAddress());
			int flag = pair.getKey();
			switch (flag) {
			case 0:
				invitation = memberInvitationRecordService.updateMemberInvitationRecordState(invitation.getId(),
						MemberInvitationRecordState.SUCCESS);
			case 1:
				invitation = memberInvitationRecordService.updateMemberInvitationRecordCause(invitation.getId(),
						"账号异常");
			case 2:
				invitation = memberInvitationRecordService.updateMemberInvitationRecordCause(invitation.getId(),
						"非活动区域");
			}

			invitation.setLoginIp(record.getLoginIp());
			invitation.setIpAddress(pair.getValue());
			memberInvitationRecordMsgService.updateRecord(invitation);
		}
	}

	/**
	 * 验证ip
	 */
	private Pair<Integer, String> verifyReqIP(String reqIP, String ipAddress) {
		int num = memberLoginRecordService.countMemberNumByLoginIp(reqIP);
		if (num > 2) {// 有2个以上的账号用该IP做登录
			return new Pair<>(1, "有2个以上的账号用该IP做登录");
		}
		if (ipAddress.equals("浙江")) {
			return new Pair<>(0, ipAddress);
		}
		return new Pair<>(2, "");
	}
}
