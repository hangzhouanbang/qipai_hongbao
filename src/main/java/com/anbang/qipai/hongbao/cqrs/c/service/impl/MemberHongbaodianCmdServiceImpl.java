package com.anbang.qipai.hongbao.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian.CreateHongbaodianAccountResult;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian.MemberHasHongbaodianAccountAlreadyException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian.MemberHongbaodianAccountManager;
import com.anbang.qipai.hongbao.cqrs.c.domain.member.MemberNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberHongbaodianCmdService;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.dml.accounting.TextAccountingSummary;

@Component
public class MemberHongbaodianCmdServiceImpl extends CmdServiceBase implements MemberHongbaodianCmdService {

	@Override
	public CreateHongbaodianAccountResult createHongbaodianAccountForNewMember(String memberId)
			throws MemberHasHongbaodianAccountAlreadyException {
		MemberHongbaodianAccountManager memberHongbaodianAccountManager = singletonEntityRepository
				.getEntity(MemberHongbaodianAccountManager.class);
		CreateHongbaodianAccountResult result = memberHongbaodianAccountManager
				.createHongbaodianAccountForNewMember(memberId);
		return result;
	}

	@Override
	public AccountingRecord giveHongbaodianToMember(String memberId, Integer giveHongbaodianAmount, String summary,
			Long giveTime) throws MemberNotFoundException {
		MemberHongbaodianAccountManager memberHongbaodianAccountManager = singletonEntityRepository
				.getEntity(MemberHongbaodianAccountManager.class);
		AccountingRecord record = memberHongbaodianAccountManager.giveHongbaodianToMember(memberId,
				giveHongbaodianAmount, new TextAccountingSummary(summary), giveTime);
		return record;
	}

	@Override
	public AccountingRecord withdraw(String memberId, Integer amount, String summary, Long withdrawTime)
			throws MemberNotFoundException, InsufficientBalanceException {
		MemberHongbaodianAccountManager memberHongbaodianAccountManager = singletonEntityRepository
				.getEntity(MemberHongbaodianAccountManager.class);
		AccountingRecord record = memberHongbaodianAccountManager.withdraw(memberId, amount,
				new TextAccountingSummary(summary), withdrawTime);
		return record;
	}

}
