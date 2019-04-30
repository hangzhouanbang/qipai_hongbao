package com.anbang.qipai.hongbao.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.hongbao.msg.channel.source.BlackListSource;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.plan.bean.BlackList;

@EnableBinding(BlackListSource.class)
public class BlackListMsgService {
	@Autowired
	private BlackListSource blackListSource;

	public void addBlackList(BlackList bl) {
		CommonMO mo = new CommonMO();
		mo.setMsg("add blackList");
		mo.setData(bl);
		blackListSource.blackList().send(MessageBuilder.withPayload(mo).build());
	}

	public void removeBlackList(String[] ids) {
		CommonMO mo = new CommonMO();
		mo.setMsg("remove blackList");
		mo.setData(ids);
		blackListSource.blackList().send(MessageBuilder.withPayload(mo).build());
	}
}
