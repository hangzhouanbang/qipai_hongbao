package com.anbang.qipai.hongbao.cqrs.q.dbo;

/**
 * 玩家红包点账户
 * 
 * @author lsc
 *
 */
public class MemberHongbaodianAccountDbo {

	private String id;// 账户id
	private String memberId;// 玩家id
	private int balance;// 余额

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

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

}
