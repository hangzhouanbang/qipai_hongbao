package com.anbang.qipai.hongbao.web.vo;

public class MemberInvitationRecordVO {
    private String invitationMemberId;// 被邀请人id
    private String invitationMemberNickname;// 被邀请玩家昵称
    private String state;// 邀请状态
    private long createTime;

    private long activationTime;// 生效时间
    private String cause;//导致当前状态的原因

    private int validNum;//下级有效数
    private int invalidNum;//下级无效数

    public String getInvitationMemberId() {
        return invitationMemberId;
    }

    public void setInvitationMemberId(String invitationMemberId) {
        this.invitationMemberId = invitationMemberId;
    }

    public String getInvitationMemberNickname() {
        return invitationMemberNickname;
    }

    public void setInvitationMemberNickname(String invitationMemberNickname) {
        this.invitationMemberNickname = invitationMemberNickname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(long activationTime) {
        this.activationTime = activationTime;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public int getValidNum() {
        return validNum;
    }

    public void setValidNum(int validNum) {
        this.validNum = validNum;
    }

    public int getInvalidNum() {
        return invalidNum;
    }

    public void setInvalidNum(int invalidNum) {
        this.invalidNum = invalidNum;
    }
}
