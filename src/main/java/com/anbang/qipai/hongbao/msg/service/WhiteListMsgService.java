package com.anbang.qipai.hongbao.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.hongbao.msg.channel.source.WhiteListSource;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.plan.bean.WhiteList;

@EnableBinding(WhiteListSource.class)
public class WhiteListMsgService {
	@Autowired
	private WhiteListSource whiteListSource;

	public void addWhiteList(WhiteList wl) {
		CommonMO mo = new CommonMO();
		mo.setMsg("add whitelist");
		mo.setData(wl);
		whiteListSource.whitelist().send(MessageBuilder.withPayload(mo).build());
	}

	public void updateWhiteList(WhiteList wl) {
		CommonMO mo = new CommonMO();
		mo.setMsg("update whitelist");
		mo.setData(wl);
		whiteListSource.whitelist().send(MessageBuilder.withPayload(mo).build());
	}

	public void removeWhiteList(String[] ids) {
		CommonMO mo = new CommonMO();
		mo.setMsg("remove whitelist");
		mo.setData(ids);
		whiteListSource.whitelist().send(MessageBuilder.withPayload(mo).build());
	}
}
