package com.anbang.qipai.hongbao.plan.bean;

/**
 * 玩家邀请记录
 * 
 * @author lsc
 *
 */
public class MemberInvitationRecord {
	private String id;
	private String memberId;
	private String invitationMemberId;
	private long createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getInvitationMemberId() {
		return invitationMemberId;
	}

	public void setInvitationMemberId(String invitationMemberId) {
		this.invitationMemberId = invitationMemberId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
