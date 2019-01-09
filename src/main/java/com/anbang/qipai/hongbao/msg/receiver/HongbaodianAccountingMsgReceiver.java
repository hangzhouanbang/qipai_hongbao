package com.anbang.qipai.hongbao.msg.receiver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.hongbao.cqrs.c.domain.member.MemberNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberHongbaodianCmdService;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianRecordDbo;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberHongbaodianService;
import com.anbang.qipai.hongbao.msg.channel.sink.HongbaodianAccountingSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.msg.service.HongbaodianRecordMsgService;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.google.gson.Gson;

@EnableBinding(HongbaodianAccountingSink.class)
public class HongbaodianAccountingMsgReceiver {

	@Autowired
	private MemberHongbaodianCmdService memberHongbaodianCmdService;

	@Autowired
	private MemberHongbaodianService memberHongbaodianService;

	@Autowired
	private HongbaodianRecordMsgService hongbaodianRecordMsgService;

	private Gson gson = new Gson();

	@StreamListener(HongbaodianAccountingSink.HONGBAODIANACCOUNTING)
	public void recordMembers(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("give_hongbaodian_to_member".equals(msg)) {
			Map data = gson.fromJson(json, Map.class);
			String memberId = (String) data.get("memberId");
			int amount = ((Double) data.get("amount")).intValue();
			String summary = (String) data.get("summary");
			try {
				AccountingRecord ar = memberHongbaodianCmdService.giveHongbaodianToMember(memberId, amount, summary,
						System.currentTimeMillis());
				MemberHongbaodianRecordDbo dbo = memberHongbaodianService.withdraw(ar, memberId);
				hongbaodianRecordMsgService.newRecord(dbo);
			} catch (MemberNotFoundException e) {
				e.printStackTrace();
			}
		}
		if ("withdraw".equals(msg)) {
			Map data = gson.fromJson(json, Map.class);
			String memberId = (String) data.get("memberId");
			int amount = ((Double) data.get("amount")).intValue();
			String summary = (String) data.get("summary");
			try {
				AccountingRecord ar = memberHongbaodianCmdService.withdraw(memberId, amount, summary,
						System.currentTimeMillis());
				MemberHongbaodianRecordDbo dbo = memberHongbaodianService.withdraw(ar, memberId);
				hongbaodianRecordMsgService.newRecord(dbo);
			} catch (MemberNotFoundException e) {
				e.printStackTrace();
			} catch (InsufficientBalanceException e) {
				e.printStackTrace();
			}
		}
	}
}
