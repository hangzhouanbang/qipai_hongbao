package com.anbang.qipai.hongbao.msg.receiver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.hongbao.msg.channel.sink.MemberLoginRecordSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.plan.bean.MemberLoginRecord;
import com.anbang.qipai.hongbao.plan.service.MemberLoginRecordService;
import com.google.gson.Gson;

@EnableBinding(MemberLoginRecordSink.class)
public class MemberLoginRecordMsgReceiver {

	@Autowired
	private MemberLoginRecordService memberLoginRecordService;

	private Gson gson = new Gson();

	@StreamListener(MemberLoginRecordSink.MEMBERLOGINRECORD)
	public void memberLoginRecord(CommonMO mo) {
		String msg = mo.getMsg();
		Map map = (Map) mo.getData();
		if ("member login".equals(msg)) {
			String json = gson.toJson(map.get("record"));
			MemberLoginRecord record = gson.fromJson(json, MemberLoginRecord.class);
			memberLoginRecordService.insert(record);
		}
		if ("update member onlineTime".equals(msg)) {
			String json = gson.toJson(mo.getData());
			MemberLoginRecord record = gson.fromJson(json, MemberLoginRecord.class);
			memberLoginRecordService.updateOnlineTime(record.getId(), record.getOnlineTime());
		}
	}
}
