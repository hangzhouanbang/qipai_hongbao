package com.anbang.qipai.hongbao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian.CreateHongbaodianAccountResult;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodian.MemberHasHongbaodianAccountAlreadyException;
import com.anbang.qipai.hongbao.cqrs.c.domain.member.MemberNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberHongbaodianCmdService;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianRecordDbo;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberHongbaodianService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianRecordMsgService;
import com.anbang.qipai.hongbao.plan.bean.MemberDbo;
import com.anbang.qipai.hongbao.plan.service.MemberService;
import com.anbang.qipai.hongbao.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.highto.framework.web.page.ListPage;

@RestController
@RequestMapping("/hongbaodian")
public class MemberHongbaodianController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberHongbaodianCmdService memberHongbaodianCmdService;

	@Autowired
	private MemberHongbaodianService memberHongbaodianService;

	@Autowired
	private HongbaodianRecordMsgService hongbaodianRecordMsgService;

	@Autowired
	private MemberAuthService memberAuthService;

	/**
	 * 查询玩家红包点流水
	 */
	@RequestMapping("/query_hongbaodian_record")
	public CommonVO queryHongbaodianRecord(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		ListPage listPage = memberHongbaodianService.findMemberHongbaodianRecordByMemberId(page, size, memberId);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 赠送玩家红包点
	 */
	@RequestMapping("/give_hongbaodian_to_member")
	public CommonVO giveHongbaodianToMember(String memberId, int amount, String summary) {
		CommonVO vo = new CommonVO();
		try {
			AccountingRecord ar = memberHongbaodianCmdService.giveHongbaodianToMember(memberId, amount, summary,
					System.currentTimeMillis());
			MemberHongbaodianRecordDbo dbo = memberHongbaodianService.withdraw(ar, memberId);
			hongbaodianRecordMsgService.newRecord(dbo);
		} catch (MemberNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("MemberNotFoundException");
			return vo;
		}
		return vo;
	}

	/**
	 * 减少玩家红包点
	 */
	@RequestMapping("/withdraw")
	public CommonVO withdraw(String memberId, int amount, String summary) {
		CommonVO vo = new CommonVO();
		try {
			AccountingRecord ar = memberHongbaodianCmdService.withdraw(memberId, amount, summary,
					System.currentTimeMillis());
			MemberHongbaodianRecordDbo dbo = memberHongbaodianService.withdraw(ar, memberId);
			hongbaodianRecordMsgService.newRecord(dbo);
		} catch (MemberNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("MemberNotFoundException");
			return vo;
		} catch (InsufficientBalanceException e) {
			vo.setSuccess(false);
			vo.setMsg("InsufficientBalanceException");
			return vo;
		}
		return vo;
	}

	/**
	 * 临时用
	 */
	@RequestMapping("/createaccount")
	public CommonVO createAccount() {
		CommonVO vo = new CommonVO();
		List<MemberDbo> memberList = memberService.findAllMembers();
		try {
			for (MemberDbo member : memberList) {
				CreateHongbaodianAccountResult result = memberHongbaodianCmdService
						.createHongbaodianAccountForNewMember(member.getId());
				memberHongbaodianService.createHongbaodianAccountForNewMember(result.getAccountId(), member.getId());
			}
		} catch (MemberHasHongbaodianAccountAlreadyException e) {
			e.printStackTrace();
		}
		return vo;
	}
}
