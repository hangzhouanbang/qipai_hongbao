package com.anbang.qipai.hongbao.msg.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.hongbao.msg.channel.sink.HongbaoSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.plan.service.MemberService;
import com.anbang.qipai.hongbao.plan.service.WXPayService;
import com.anbang.qipai.hongbao.remote.service.QipaiMembersRemoteService;
import com.google.gson.Gson;

@EnableBinding(HongbaoSink.class)
public class HongbaoMsgReceiver {
	@Autowired
	private MemberService memberService;

	@Autowired
	private WXPayService wxPayService;

	@Autowired
	private QipaiMembersRemoteService qipaiMembersRemoteService;

	private Gson gson = new Gson();

	@StreamListener(HongbaoSink.HONGBAO)
	public void recordMembers(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("newMember".equals(msg)) {

		}
	}
}
