package com.anbang.qipai.hongbao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.hongbao.msg.service.BlackListMsgService;
import com.anbang.qipai.hongbao.msg.service.WhiteListMsgService;
import com.anbang.qipai.hongbao.plan.bean.BlackList;
import com.anbang.qipai.hongbao.plan.bean.WhiteList;
import com.anbang.qipai.hongbao.plan.service.WhiteListService;
import com.anbang.qipai.hongbao.web.vo.CommonVO;

@RestController
@RequestMapping("/whitelist")
public class WhiteListController {

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private WhiteListService whiteListService;

	@Autowired
	private WhiteListMsgService whiteListMsgService;

	@Autowired
	private BlackListMsgService blackListMsgService;

	/**
	 * 添加白名单
	 */
	@RequestMapping("add_whitelist")
	public CommonVO addWhiteList(@RequestBody WhiteList wl) {
		CommonVO vo = new CommonVO();
		whiteListService.insert(wl);
		whiteListMsgService.addWhiteList(wl);
		return vo;
	}

	/**
	 * 修改白名单
	 */
	@RequestMapping("update_whitelist")
	public CommonVO updateWhiteList(@RequestBody WhiteList wl) {
		CommonVO vo = new CommonVO();
		whiteListService.update(wl);
		whiteListMsgService.updateWhiteList(wl);
		return vo;
	}

	/**
	 * 删除白名单
	 */
	@RequestMapping("remove_whitelist_by_id")
	public CommonVO removeWhiteListById(@RequestBody String[] ids) {
		CommonVO vo = new CommonVO();
		whiteListService.remove(ids);
		whiteListMsgService.removeWhiteList(ids);
		return vo;
	}

	/**
	 * 添加黑名单
	 */
	@RequestMapping("add_blacklist")
	public CommonVO addBlackList(@RequestBody BlackList blackList) {
		CommonVO vo = new CommonVO();
		MemberDbo member = memberAuthQueryService.findByMemberId(blackList.getPlayerId());
		blackList.setAddTime(System.currentTimeMillis());
		blackList.setReqIP(member.getReqIP());
		whiteListService.saveBlackList(blackList);
		blackListMsgService.addBlackList(blackList);
		return vo;
	}

	/**
	 * 黑名单移入白名单
	 */
	@RequestMapping("change_blacklist_to_whitelist")
	public CommonVO updateBlackList(String blackListId, String operator) {
		CommonVO vo = new CommonVO();
		BlackList blackList = whiteListService.findBlackListById(blackListId);
		String[] ids = { blackListId };
		WhiteList whiteList = new WhiteList();
		whiteList.setPlayerId(blackList.getPlayerId());
		whiteList.setRemark("黑名单转白名单");
		whiteList.setAddTime(System.currentTimeMillis());
		whiteList.setOperator(operator);
		whiteListService.insert(whiteList);
		whiteListMsgService.addWhiteList(whiteList);
		whiteListService.removeBlackListByIds(ids);
		blackListMsgService.removeBlackList(ids);
		return vo;
	}

	/**
	 * 删除黑名单
	 */
	@RequestMapping("remove_blacklist_by_id")
	public CommonVO removeBlackListById(@RequestBody String[] ids) {
		CommonVO vo = new CommonVO();
		whiteListService.removeBlackListByIds(ids);
		blackListMsgService.removeBlackList(ids);
		return vo;
	}

}
