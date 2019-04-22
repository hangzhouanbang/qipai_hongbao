package com.anbang.qipai.hongbao.msg.channel.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface BlackListSource {
	@Output
	MessageChannel blackList();
}
