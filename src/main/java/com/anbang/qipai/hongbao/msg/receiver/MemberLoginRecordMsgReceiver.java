package com.anbang.qipai.hongbao.msg.receiver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.util.Pair;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.hongbao.conf.IPVerifyConfig;
import com.anbang.qipai.hongbao.conf.MemberInvitationRecordState;
import com.anbang.qipai.hongbao.msg.channel.sink.MemberLoginRecordSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.msg.service.MemberInvitationRecordMsgService;
import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;
import com.anbang.qipai.hongbao.plan.bean.MemberLoginRecord;
import com.anbang.qipai.hongbao.plan.service.MemberInvitationRecordService;
import com.anbang.qipai.hongbao.plan.service.MemberLoginRecordService;
import com.anbang.qipai.hongbao.util.HttpUtil;
import com.google.gson.Gson;

@EnableBinding(MemberLoginRecordSink.class)
public class MemberLoginRecordMsgReceiver {

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
		if (invitation != null && !invitation.getState().equals(MemberInvitationRecordState.SUCCESS)) {
			Pair<Integer, String> pair = verifyReqIP(record.getLoginIp());
			int flag = pair.getKey();
			switch (flag) {
				case 0:
					invitation = memberInvitationRecordService.updateMemberInvitationRecordState(invitation.getId(),
							MemberInvitationRecordState.SUCCESS);
				case 1:
					invitation = memberInvitationRecordService.updateMemberInvitationRecordCause(invitation.getId(),"账号异常");
				case 2:
					invitation = memberInvitationRecordService.updateMemberInvitationRecordCause(invitation.getId(),"非活动区域");
			}

			invitation.setLoginIp(record.getLoginIp());
			invitation.setIpAddress(pair.getValue());
			memberInvitationRecordMsgService.updateRecord(invitation);
		}
	}

	/**
	 * 验证ip
	 */
	private Pair<Integer,String> verifyReqIP(String reqIP) {
		int num = memberLoginRecordService.countMemberNumByLoginIp(reqIP);
		if (num > 2) {// 有2个以上的账号用该IP做登录
			return new Pair<>(1,"");
		}
		String host = "http://iploc.market.alicloudapi.com";
		String path = "/v3/ip";
		String method = "GET";
		String appcode = IPVerifyConfig.APPCODE;
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("ip", reqIP);

		try {
			HttpResponse response = HttpUtil.doGet(host, path, method, headers, querys);
			String entity = EntityUtils.toString(response.getEntity());
			Map map = gson.fromJson(entity, Map.class);
			String status = (String) map.get("status");
			String info = (String) map.get("info");
			String infocode = (String) map.get("infocode");
			String province = (String) map.get("province");
			String adcode = (String) map.get("adcode");
			String city = (String) map.get("city");
			if (status.equals("1") && info.equals("OK") && province.equals("浙江省") && infocode.equals("10000")
					&& city.equals("温州市") && adcode.equals("330300")) {
				String ipAddress = province + city;
				return new Pair<>(0,ipAddress);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Pair<>(2,"");
	}
}
