package com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian;

import java.util.HashMap;
import java.util.Map;

import com.anbang.qipai.hongbao.cqrs.c.domain.member.MemberNotFoundException;
import com.dml.accounting.Account;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.AccountingSubject;
import com.dml.accounting.AccountingSummary;
import com.dml.accounting.InsufficientBalanceException;

/**
 * 玩家红包点账户管理
 * 
 * @author lsc
 *
 */
public class MemberHongbaodianAccountManager {

	private Map<String, Account> idAccountMap = new HashMap<>();

	private Map<String, String> memberIdAccountIdMap = new HashMap<>();

	/**
	 * 为玩家创建红包点账户
	 */
	public CreateHongbaodianAccountResult createHongbaodianAccountForNewMember(String memberId)
			throws MemberHasHongbaodianAccountAlreadyException {
		if (memberIdAccountIdMap.containsKey(memberId)) {
			throw new MemberHasHongbaodianAccountAlreadyException();
		}
		MemberHongbaodianAccountOwner mao = new MemberHongbaodianAccountOwner();
		mao.setMemberId(memberId);

		AccountingSubject subject = new AccountingSubject();
		subject.setName("wallet");

		Account account = new Account();
		account.setId(memberId + "_hongbaodian_wallet");
		account.setCurrency("hongbaodian");
		account.setOwner(mao);
		account.setSubject(subject);

		idAccountMap.put(account.getId(), account);
		memberIdAccountIdMap.put(memberId, account.getId());
		CreateHongbaodianAccountResult result = new CreateHongbaodianAccountResult(account.getId(), memberId);
		return result;
	}

	/**
	 * 移除玩家的红包点账户
	 */
	public Account removeChaguanYushiAccountOfMemberByAgent(String memberId, String agentId)
			throws MemberNotFoundException {
		if (!memberIdAccountIdMap.containsKey(memberId)) {
			throw new MemberNotFoundException();
		}
		String accountId = memberIdAccountIdMap.remove(memberId);
		return idAccountMap.remove(accountId);
	}

	/**
	 * 增加玩家的红包点账户余额
	 */
	public AccountingRecord giveHongbaodianToMember(String memberId, int giveHongbaodianAmount,
			AccountingSummary accountingSummary, long giveTime) throws MemberNotFoundException {
		if (!memberIdAccountIdMap.containsKey(memberId)) {
			throw new MemberNotFoundException();
		}
		Account account = idAccountMap.get(memberIdAccountIdMap.get(memberId));
		AccountingRecord record = account.deposit(giveHongbaodianAmount, accountingSummary, giveTime);
		return record;
	}

	/**
	 * 减少玩家的红包点账户余额
	 */
	public AccountingRecord withdraw(String memberId, int amount, AccountingSummary accountingSummary,
			long withdrawTime) throws MemberNotFoundException, InsufficientBalanceException {
		if (!memberIdAccountIdMap.containsKey(memberId)) {
			throw new MemberNotFoundException();
		}
		Account account = idAccountMap.get(memberIdAccountIdMap.get(memberId));
		AccountingRecord record = account.withdraw(amount, accountingSummary, withdrawTime);
		return record;
	}
}
