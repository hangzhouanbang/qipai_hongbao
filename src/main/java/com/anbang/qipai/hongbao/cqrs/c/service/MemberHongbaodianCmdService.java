package com.anbang.qipai.hongbao.cqrs.c.service;

import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian.CreateHongbaodianAccountResult;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian.MemberHasHongbaodianAccountAlreadyException;
import com.anbang.qipai.hongbao.cqrs.c.domain.member.MemberNotFoundException;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;

public interface MemberHongbaodianCmdService {

	CreateHongbaodianAccountResult createHongbaodianAccountForNewMember(String memberId)
			throws MemberHasHongbaodianAccountAlreadyException;

	AccountingRecord giveHongbaodianToMember(String memberId, Integer giveHongbaodianAmount, String summary,
			Long giveTime) throws MemberNotFoundException;

	AccountingRecord withdraw(String memberId, Integer amount, String summary, Long withdrawTime)
			throws MemberNotFoundException, InsufficientBalanceException;
}
