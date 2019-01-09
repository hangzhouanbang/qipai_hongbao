package com.anbang.qipai.hongbao.msg.channel.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface HongbaoSink {
	String HONGBAO = "hongbao";

	@Input
	SubscribableChannel hongbao();
}
