package com.anbang.qipai.hongbao.msg.receiver;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian.CreateHongbaodianAccountResult;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian.MemberHasHongbaodianAccountAlreadyException;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberHongbaodianCmdService;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberHongbaodianService;
import com.anbang.qipai.hongbao.msg.channel.sink.MembersSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.google.gson.Gson;

@EnableBinding(MembersSink.class)
public class MemberMsgReceiver {

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private MemberHongbaodianCmdService memberHongbaodianCmdService;

	@Autowired
	private MemberHongbaodianService memberHongbaodianService;

	private Gson gson = new Gson();

	@StreamListener(MembersSink.MEMBERS)
	public void recordMembers(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		MemberDbo member = gson.fromJson(json, MemberDbo.class);
		if ("newMember".equals(msg)) {
			memberAuthQueryService.insertMember(member);
			try {
				// 创建账户
				CreateHongbaodianAccountResult result = memberHongbaodianCmdService
						.createHongbaodianAccountForNewMember(member.getId());
				memberHongbaodianService.createHongbaodianAccountForNewMember(result.getAccountId(), member.getId());
			} catch (MemberHasHongbaodianAccountAlreadyException e) {
				e.printStackTrace();
			}
		}
		if ("update member info".equals(msg)) {
			memberAuthQueryService.updateMemberBaseInfo(member);
			if (!StringUtils.isBlank(member.getReqIP())) {
				memberAuthQueryService.updateMemberReqIP(member);
			}
		}
		if ("update member phone".equals(msg)) {
			if (!StringUtils.isBlank(member.getPhone())) {
				memberAuthQueryService.updateMemberPhone(member);
			}
		}
	}
}
