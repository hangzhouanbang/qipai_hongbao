package com.anbang.qipai.hongbao.msg.channel.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface HongbaodianAccountingSink {
	String MEMBERHONGBAODIANACCOUNTING = "memberHongbaodianAccounting";

	@Input
	SubscribableChannel memberHongbaodianAccounting();
}
