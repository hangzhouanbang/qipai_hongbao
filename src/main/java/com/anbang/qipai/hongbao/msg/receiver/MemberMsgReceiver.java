package com.anbang.qipai.hongbao.msg.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.hongbao.msg.channel.sink.MembersSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.google.gson.Gson;

@EnableBinding(MembersSink.class)
public class MemberMsgReceiver {

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	private Gson gson = new Gson();

	@StreamListener(MembersSink.MEMBERS)
	public void recordMembers(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("newMember".equals(msg)) {
			MemberDbo member = gson.fromJson(json, MemberDbo.class);
			memberAuthQueryService.insertMember(member);
		}
	}
}
