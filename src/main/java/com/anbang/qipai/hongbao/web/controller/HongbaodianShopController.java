package com.anbang.qipai.hongbao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.hongbao.conf.HongbaodianProductRecordStatus;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianproduct.ProductNotEnoughException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianproduct.ProductNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.domain.member.MemberNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.service.HongbaodianProductCmdService;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberHongbaodianCmdService;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianShopProductDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianRecordDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.ReceiverInfoDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.RewardType;
import com.anbang.qipai.hongbao.cqrs.q.service.HongbaodianShopProductDboService;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberHongbaodianService;
import com.anbang.qipai.hongbao.cqrs.q.service.ReceiverInfoQueryService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianProductRecordMsgService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianRecordMsgService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianShopProductMsgService;
import com.anbang.qipai.hongbao.msg.service.MemberGoldsMsgService;
import com.anbang.qipai.hongbao.plan.bean.HongbaodianProductRecord;
import com.anbang.qipai.hongbao.plan.bean.ProductType;
import com.anbang.qipai.hongbao.util.IPUtil;
import com.anbang.qipai.hongbao.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.highto.framework.web.page.ListPage;

@CrossOrigin
@RestController
@RequestMapping("/hongbaodianshop")
public class HongbaodianShopController {

	@Autowired
	private MemberAuthService memberAuthService;

	@Autowired
	private MemberHongbaodianCmdService memberHongbaodianCmdService;

	@Autowired
	private MemberHongbaodianService memberHongbaodianService;

	@Autowired
	private HongbaodianRecordMsgService hongbaodianRecordMsgService;

	@Autowired
	private HongbaodianProductCmdService hongbaodianProductCmdService;

	@Autowired
	private HongbaodianShopProductDboService hongbaodianShopProductDboService;

	@Autowired
	private HongbaodianProductRecordMsgService hongbaodianProductRecordMsgService;

	@Autowired
	private HongbaodianShopProductMsgService hongbaodianShopProductMsgService;

	@Autowired
	private MemberGoldsMsgService memberGoldsMsgService;

	@Autowired
	private ReceiverInfoQueryService receiverInfoQueryService;

	/**
	 * 所有类目
	 */
	@RequestMapping("/alltype")
	public CommonVO allType(String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		List<ProductType> list = hongbaodianShopProductDboService.findAllProductType();
		vo.setSuccess(true);
		vo.setMsg("type list");
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("list", list);
		return vo;
	}

	/**
	 * 发布红包点商品
	 */
	@RequestMapping("/release")
	public CommonVO release(@RequestBody List<HongbaodianShopProductDbo> products) {
		CommonVO vo = new CommonVO();
		if (!products.isEmpty()) {
			hongbaodianShopProductDboService.saveAllHongbaodianShopProductDbo(products);
			try {
				hongbaodianProductCmdService.clear();
				for (HongbaodianShopProductDbo dbo : products) {
					hongbaodianProductCmdService.addProduct(dbo.getId(), dbo.getRemain());
				}
			} catch (Exception e) {
				hongbaodianShopProductDboService.clearAllHongbaodianShopProductDbo();
				e.printStackTrace();
			}
		}
		// scoreShopProductMsgService.release(products);
		return vo;
	}

	/**
	 * 根据类目查询商品
	 */
	@RequestMapping("/queryproduct")
	public CommonVO queryScoreProduct(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "20") int size, String type, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ListPage listPage = hongbaodianShopProductDboService.findHongbaodianShopProductDboByType(page, size, type);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("list", listPage);
		return vo;
	}

	/**
	 * 购买红包点商品
	 */
	@RequestMapping("/buyproduct")
	public CommonVO buyScoreProduct(HttpServletRequest request, String token, String productId,
			@RequestParam(defaultValue = "1") int amount) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		HongbaodianShopProductDbo product = hongbaodianShopProductDboService
				.findHongbaodianShopProductDboById(productId);
		if (product == null) {
			vo.setSuccess(false);
			vo.setMsg("product not found");
			return vo;
		}
		ReceiverInfoDbo receiverInfo = receiverInfoQueryService.findReceiverByMemberId(memberId);
		if (receiverInfo == null) {
			vo.setSuccess(false);
			vo.setMsg("receiverInfo not found");
			return vo;
		}
		try {
			AccountingRecord rcd = memberHongbaodianCmdService.withdraw(memberId, product.getPrice(),
					"buy " + product.getDesc(), System.currentTimeMillis());
			MemberHongbaodianRecordDbo dbo = memberHongbaodianService.withdraw(rcd, memberId);
			hongbaodianRecordMsgService.newRecord(dbo);
			int remain = hongbaodianProductCmdService.buyProduct(productId, amount);
			product = hongbaodianShopProductDboService.upateHongbaodianShopProductDboRemainById(productId, remain);
			hongbaodianShopProductMsgService.update(product);
			String ip = IPUtil.getRealIp(request);
			reward(ip, product, memberId, amount);
		} catch (InsufficientBalanceException e) {
			vo.setSuccess(false);
			vo.setMsg("InsufficientBalanceException");
			return vo;
		} catch (MemberNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("MemberNotFoundException");
			return vo;
		} catch (Exception e) {
			try {
				if (e instanceof ProductNotFoundException || e instanceof ProductNotEnoughException) {
					AccountingRecord rcd = memberHongbaodianCmdService.giveHongbaodianToMember(memberId,
							product.getPrice(), "refund " + product.getDesc(), System.currentTimeMillis());
					MemberHongbaodianRecordDbo dbo = memberHongbaodianService.withdraw(rcd, memberId);
					hongbaodianRecordMsgService.newRecord(dbo);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		return vo;
	}

	private void reward(String requestIP, HongbaodianShopProductDbo product, String memberId, int amount) {
		HongbaodianProductRecord record = hongbaodianShopProductDboService.saveHongbaodianProductRecord(requestIP,
				product, memberId, amount);
		hongbaodianProductRecordMsgService.addRecord(record);
		if (RewardType.YUSHI.equals(product.getRewardType())) {
			memberGoldsMsgService.giveGoldToMember(memberId, amount, "hongbaodian exchange");
		} else if (RewardType.ENTITY.equals(product.getRewardType())) {

		}
	}

	@RequestMapping("/pass")
	public CommonVO passScoreProductRecord(String id) {
		CommonVO vo = new CommonVO();
		HongbaodianProductRecord record = hongbaodianShopProductDboService.finishHongbaodianProductRecord(id,
				System.currentTimeMillis(), HongbaodianProductRecordStatus.SUCCESS);
		hongbaodianProductRecordMsgService.finishRecord(record);
		return vo;
	}

	@RequestMapping("/refuse")
	public CommonVO refuseScoreProductRecord(String id) {
		CommonVO vo = new CommonVO();
		HongbaodianProductRecord record = hongbaodianShopProductDboService.finishHongbaodianProductRecord(id,
				System.currentTimeMillis(), HongbaodianProductRecordStatus.FAIL);
		hongbaodianProductRecordMsgService.finishRecord(record);
		return vo;
	}
}
