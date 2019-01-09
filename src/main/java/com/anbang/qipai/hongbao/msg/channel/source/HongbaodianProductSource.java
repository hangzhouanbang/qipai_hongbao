package com.anbang.qipai.hongbao.msg.channel.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface HongbaodianProductSource {
	@Output
	MessageChannel hongbaodianProduct();
}
