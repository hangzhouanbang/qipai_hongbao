package com.anbang.qipai.hongbao.msg.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.hongbao.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.hongbao.msg.channel.sink.AuthorizationSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.google.gson.Gson;

@EnableBinding(AuthorizationSink.class)
public class AuthorizationMsgReceiver {

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	private Gson gson = new Gson();

	@StreamListener(AuthorizationSink.AUTHORIZATION)
	public void authorization(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("new authorization".equals(msg)) {
			AuthorizationDbo authDbo = gson.fromJson(json, AuthorizationDbo.class);
			memberAuthQueryService.addThirdAuth(authDbo);
		}
	}
}
