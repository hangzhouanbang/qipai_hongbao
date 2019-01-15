package com.anbang.qipai.hongbao.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.hongbao.cqrs.q.dbo.RewardOrderDbo;
import com.anbang.qipai.hongbao.msg.channel.source.RewardOrderDboSource;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;

@EnableBinding(RewardOrderDboSource.class)
public class RewardOrderDboMsgService {
	@Autowired
	private RewardOrderDboSource rewardOrderDboSource;

	public void recordRewardOrderDbo(RewardOrderDbo order) {
		CommonMO mo = new CommonMO();
		mo.setMsg("add order");
		mo.setData(order);
		rewardOrderDboSource.rewardOrderDbo().send(MessageBuilder.withPayload(mo).build());
	}

	public void finishRewardOrderDbo(RewardOrderDbo order) {
		CommonMO mo = new CommonMO();
		mo.setMsg("finish order");
		mo.setData(order);
		rewardOrderDboSource.rewardOrderDbo().send(MessageBuilder.withPayload(mo).build());
	}
}
