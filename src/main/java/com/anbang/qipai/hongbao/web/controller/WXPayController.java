package com.anbang.qipai.hongbao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.hongbao.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberHongbaodianCmdService;
import com.anbang.qipai.hongbao.cqrs.q.service.HongbaodianProductService;
import com.anbang.qipai.hongbao.web.vo.CommonVO;

@RestController
@RequestMapping("/wxpay")
public class WXPayController {

	@Autowired
	private MemberAuthService memberAuthService;

	@Autowired
	private HongbaodianProductService hongbaodianProductService;

	@Autowired
	private MemberHongbaodianCmdService memberHongbaodianCmdService;

	@RequestMapping("/buy_hongbaodianproduct")
	public CommonVO buyHongbaodianProduct(String token, String productId) {
		CommonVO vo = new CommonVO();

		return vo;
	}
}
