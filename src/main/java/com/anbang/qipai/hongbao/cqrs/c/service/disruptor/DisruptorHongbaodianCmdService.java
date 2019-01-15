package com.anbang.qipai.hongbao.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian.CreateHongbaodianAccountResult;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian.MemberHasHongbaodianAccountAlreadyException;
import com.anbang.qipai.hongbao.cqrs.c.domain.member.MemberNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberHongbaodianCmdService;
import com.anbang.qipai.hongbao.cqrs.c.service.impl.MemberHongbaodianCmdServiceImpl;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "memberHongbaodianCmdService")
public class DisruptorHongbaodianCmdService extends DisruptorCmdServiceBase implements MemberHongbaodianCmdService {

	@Autowired
	private MemberHongbaodianCmdServiceImpl memberHongbaodianCmdServiceImpl;

	@Override
	public CreateHongbaodianAccountResult createHongbaodianAccountForNewMember(String memberId)
			throws MemberHasHongbaodianAccountAlreadyException {
		CommonCommand cmd = new CommonCommand(MemberHongbaodianCmdServiceImpl.class.getName(),
				"createHongbaodianAccountForNewMember", memberId);
		DeferredResult<CreateHongbaodianAccountResult> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(),
				cmd, () -> {
					CreateHongbaodianAccountResult cr = memberHongbaodianCmdServiceImpl
							.createHongbaodianAccountForNewMember(cmd.getParameter());
					return cr;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public AccountingRecord giveHongbaodianToMember(String memberId, Integer giveHongbaodianAmount, String summary,
			Long giveTime) throws MemberNotFoundException {
		CommonCommand cmd = new CommonCommand(MemberHongbaodianCmdServiceImpl.class.getName(),
				"giveHongbaodianToMember", memberId, giveHongbaodianAmount, summary, giveTime);
		DeferredResult<AccountingRecord> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			AccountingRecord ar = memberHongbaodianCmdServiceImpl.giveHongbaodianToMember(cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return ar;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public AccountingRecord withdraw(String memberId, Integer amount, String summary, Long withdrawTime)
			throws MemberNotFoundException, InsufficientBalanceException {
		CommonCommand cmd = new CommonCommand(MemberHongbaodianCmdServiceImpl.class.getName(), "withdraw", memberId,
				amount, summary, withdrawTime);
		DeferredResult<AccountingRecord> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			AccountingRecord ar = memberHongbaodianCmdServiceImpl.withdraw(cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter());
			return ar;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
