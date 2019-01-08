package com.anbang.qipai.hongbao.cqrs.q.dbo;

import com.dml.accounting.AccountingSummary;

/**
 * 红包点流水记录
 * 
 * @author lsc
 *
 */
public class MemberHongbaodianRecordDbo {
	private String id;
	private int accountingNo;// 流水号
	private String accountId;// 账户id
	private String memberId;// 玩家id
	private int balanceAfter;// 余额
	private int accountingAmount;// 交易额
	private AccountingSummary summary;// 记账摘要
	private long accountingTime;// 交易时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getAccountingNo() {
		return accountingNo;
	}

	public void setAccountingNo(int accountingNo) {
		this.accountingNo = accountingNo;
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

	public int getBalanceAfter() {
		return balanceAfter;
	}

	public void setBalanceAfter(int balanceAfter) {
		this.balanceAfter = balanceAfter;
	}

	public int getAccountingAmount() {
		return accountingAmount;
	}

	public void setAccountingAmount(int accountingAmount) {
		this.accountingAmount = accountingAmount;
	}

	public AccountingSummary getSummary() {
		return summary;
	}

	public void setSummary(AccountingSummary summary) {
		this.summary = summary;
	}

	public long getAccountingTime() {
		return accountingTime;
	}

	public void setAccountingTime(long accountingTime) {
		this.accountingTime = accountingTime;
	}

}
