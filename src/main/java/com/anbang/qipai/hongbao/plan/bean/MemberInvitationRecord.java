package com.anbang.qipai.hongbao.plan.bean;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

/**
 * 玩家邀请记录
 * 
 * @author lsc
 *
 */
@CompoundIndexes({ @CompoundIndex(name = "memberId", def = "{'memberId': 1}") })
public class MemberInvitationRecord {
	private String id;
	private String memberId;// 邀请玩家id
	private String nickname;// 邀请玩家昵称
	private String invitationMemberId;// 被邀请人id
	private String invitationMemberNickname;// 被邀请玩家昵称
	private String state;// 邀请状态
	private long createTime;

	private long activationTime;// 生效时间
	private String cause;//导致当前状态的原因
	private String loginIp;
	private String ipAddress;

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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
