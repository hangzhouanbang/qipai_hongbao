package com.anbang.qipai.hongbao.web.controller;

import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.hongbao.plan.bean.MemberDbo;
import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;
import com.anbang.qipai.hongbao.plan.service.MemberInvitationRecordService;
import com.anbang.qipai.hongbao.plan.service.MemberService;
import com.anbang.qipai.hongbao.web.vo.CommonVO;

@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberInvitationRecordService memberInvitationRecordService;

	@RequestMapping("/createnewmember")
	public CommonVO createNewMember(String oldMemberId, String memberId, String openid, String nickname,
			String headimgurl) {
		CommonVO vo = new CommonVO();
		if (memberService.findByMemberId(memberId) != null) {
			return vo;
		}
		MemberDbo member = new MemberDbo();
		member.setId(memberId);
		member.setNickname(nickname);
		member.setHeadimgurl(headimgurl);
		member.setOpenid(openid);
		memberService.insertMember(member);
		if (!StringUtil.isBlank(oldMemberId)) {
			// 邀请记录
			MemberInvitationRecord record = new MemberInvitationRecord();
			record.setMemberId(oldMemberId);
			record.setInvitationMemberId(memberId);
			record.setCreateTime(System.currentTimeMillis());
			memberInvitationRecordService.insertMemberInvitationRecord(record);
		}
		return vo;
	}
}
