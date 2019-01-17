package com.anbang.qipai.hongbao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.hongbao.msg.service.WhiteListMsgService;
import com.anbang.qipai.hongbao.plan.bean.WhiteList;
import com.anbang.qipai.hongbao.plan.service.WhiteListService;
import com.anbang.qipai.hongbao.web.vo.CommonVO;

@RestController
@RequestMapping("/whitelist")
public class WhiteListController {

	@Autowired
	private WhiteListService whiteListService;

	@Autowired
	private WhiteListMsgService whiteListMsgService;

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

}
