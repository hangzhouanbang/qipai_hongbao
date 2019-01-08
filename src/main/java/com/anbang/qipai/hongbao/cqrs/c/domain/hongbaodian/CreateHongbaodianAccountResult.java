package com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian;

/**
 * 创建红包点账户结果
 * 
 * @author lsc
 *
 */
public class CreateHongbaodianAccountResult {

	private String accountId;
	private String memberId;

	public CreateHongbaodianAccountResult(String accountId, String memberId) {
		this.accountId = accountId;
		this.memberId = memberId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

}
