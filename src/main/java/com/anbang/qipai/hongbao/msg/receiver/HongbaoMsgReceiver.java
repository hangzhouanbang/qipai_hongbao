package com.anbang.qipai.hongbao.msg.receiver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.hongbao.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.hongbao.msg.channel.sink.HongbaoSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.plan.service.WXPayService;
import com.anbang.qipai.hongbao.remote.service.QipaiMembersRemoteService;
import com.google.gson.Gson;

@EnableBinding(HongbaoSink.class)
public class HongbaoMsgReceiver {

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private WXPayService wxPayService;

	@Autowired
	private QipaiMembersRemoteService qipaiMembersRemoteService;

	private Gson gson = new Gson();

	/**
	 * 任务完成发放红包
	 */
	@StreamListener(HongbaoSink.MEMBERHONGBAORMBACCOUNTING)
	public void recordMembers(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("give_hongbao_to_member".equals(msg)) {
			Map data = gson.fromJson(json, Map.class);
			String memberId = (String) data.get("memberId");
			double amount = (Double) data.get("amount");
			String textSummary = (String) data.get("textSummary");
		}
	}
}
