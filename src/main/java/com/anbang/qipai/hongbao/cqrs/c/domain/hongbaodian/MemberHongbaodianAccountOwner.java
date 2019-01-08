package com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian;

import com.dml.accounting.AccountOwner;

/**
 * 红包点持有者
 * 
 * @author lsc
 *
 */
public class MemberHongbaodianAccountOwner implements AccountOwner {

	private String memberId;// 玩家

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

}
