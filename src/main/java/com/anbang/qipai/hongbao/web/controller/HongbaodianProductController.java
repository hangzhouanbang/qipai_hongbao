package com.anbang.qipai.hongbao.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.hongbao.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianProduct;
import com.anbang.qipai.hongbao.cqrs.q.service.HongbaodianProductService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianProductMsgService;
import com.anbang.qipai.hongbao.web.vo.CommonVO;
import com.highto.framework.web.page.ListPage;

@RestController
@RequestMapping("/hongbaodianproduct")
public class HongbaodianProductController {

	@Autowired
	private HongbaodianProductService hongbaodianProductService;

	@Autowired
	private HongbaodianProductMsgService hongbaodianProductMsgService;

	@Autowired
	private MemberAuthService memberAuthService;

	@RequestMapping("add_product")
	public CommonVO addHongbaodianProduct(HongbaodianProduct product) {
		CommonVO vo = new CommonVO();
		hongbaodianProductService.insertHongbaodianProduct(product);
		hongbaodianProductMsgService.addHongbaodianProduct(product);
		return vo;
	}

	@RequestMapping("update_product")
	public CommonVO updateHongbaodianProduct(HongbaodianProduct product) {
		CommonVO vo = new CommonVO();
		hongbaodianProductService.updateHongbaodianProduct(product);
		hongbaodianProductMsgService.updateHongbaodianProduct(product);
		return vo;
	}

	@RequestMapping("remove_product_by_id")
	public CommonVO removeHongbaodianProductById(String productId) {
		CommonVO vo = new CommonVO();
		hongbaodianProductService.removeHongbaodianProductById(productId);
		hongbaodianProductMsgService.removeHongbaodianProduct(productId);
		return vo;
	}

	@RequestMapping("show_products")
	public CommonVO showProducts(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
		}
		ListPage listPage = hongbaodianProductService.showHongbaodianProducts(page, size);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}
}
