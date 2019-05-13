package com.anbang.qipai.hongbao.msg.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.hongbao.cqrs.q.dbo.ReceiverInfoDbo;
import com.anbang.qipai.hongbao.cqrs.q.service.ReceiverInfoQueryService;
import com.anbang.qipai.hongbao.msg.channel.sink.ReceiverInfoSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.google.gson.Gson;

@EnableBinding(ReceiverInfoSink.class)
public class ReceiverInfoReceiver {

	@Autowired
	private ReceiverInfoQueryService receiverInfoQueryService;

	private Gson gson = new Gson();

	@StreamListener(ReceiverInfoSink.RECEIVERINFO)
	public void recordMembers(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("add info".equals(msg)) {
			ReceiverInfoDbo dbo = gson.fromJson(json, ReceiverInfoDbo.class);
			receiverInfoQueryService.addReceiverInfo(dbo);
		}
		if ("update info".equals(msg)) {
			ReceiverInfoDbo dbo = gson.fromJson(json, ReceiverInfoDbo.class);
			receiverInfoQueryService.save(dbo);
		}
	}
}
