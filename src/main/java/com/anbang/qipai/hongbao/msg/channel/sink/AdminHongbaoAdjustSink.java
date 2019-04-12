package com.anbang.qipai.hongbao.msg.channel.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface AdminHongbaoAdjustSink {

    String ADMINHONGBAOADJUST = "adminHongbaoAdjust";

    @Input
    SubscribableChannel adminHongbaoAdjust();
}
