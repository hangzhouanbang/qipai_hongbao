package com.anbang.qipai.hongbao.msg.receiver;

import com.anbang.qipai.hongbao.conf.MemberInvitationRecordState;
import com.anbang.qipai.hongbao.msg.channel.sink.AdminHongbaoAdjustSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;
import com.anbang.qipai.hongbao.plan.service.MemberInvitationRecordService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(AdminHongbaoAdjustSink.class)
public class AdminHongbaoAdjustReceiver {

    @Autowired
    private MemberInvitationRecordService memberInvitationRecordService;

    private Gson gson = new Gson();

    @StreamListener(AdminHongbaoAdjustSink.ADMINHONGBAOADJUST)
    public void authorization(CommonMO mo) {
        String msg = mo.getMsg();
        String json = gson.toJson(mo.getData());
        if ("update_invitation_state".equals(msg)) {
            String invitationMemberId = (String)mo.getData();
            MemberInvitationRecord invitation = memberInvitationRecordService
                    .findMemberInvitationRecordByInvitationMemberId(invitationMemberId);
            if (invitation != null && !invitation.getState().equals(MemberInvitationRecordState.SUCCESS)) {
                invitation = memberInvitationRecordService.updateMemberInvitationRecordState(invitation.getId(),
                        MemberInvitationRecordState.SUCCESS);
            }
        }
    }
}
