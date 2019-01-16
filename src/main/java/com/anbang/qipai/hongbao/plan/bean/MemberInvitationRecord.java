package com.anbang.qipai.hongbao.plan.bean;

/**
 * 玩家邀请记录
 * 
 * @author lsc
 *
 */
public class MemberInvitationRecord {
	private String id;
	private String memberId;// 邀请人id
	private String nickname;// 被邀请玩家id
	private String invitationMemberId;// 邀请人昵称
	private String invitationMemberNickname;// 被邀请玩家昵称
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

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

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
