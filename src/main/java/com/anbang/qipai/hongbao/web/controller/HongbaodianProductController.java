package com.anbang.qipai.hongbao.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderHasAlreadyExistenceException;
import com.anbang.qipai.hongbao.cqrs.c.domain.member.MemberNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.service.HongbaodianOrderCmdService;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberHongbaodianCmdService;
import com.anbang.qipai.hongbao.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianOrder;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianProduct;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianRecordDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.RewardType;
import com.anbang.qipai.hongbao.cqrs.q.service.HongbaodianOrderService;
import com.anbang.qipai.hongbao.cqrs.q.service.HongbaodianProductService;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberHongbaodianService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianOrderMsgService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianProductMsgService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianRecordMsgService;
import com.anbang.qipai.hongbao.plan.service.WXPayService;
import com.anbang.qipai.hongbao.util.IPUtil;
import com.anbang.qipai.hongbao.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
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

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private HongbaodianOrderService hongbaodianOrderService;

	@Autowired
	private MemberHongbaodianService memberHongbaodianService;

	@Autowired
	private MemberHongbaodianCmdService memberHongbaodianCmdService;

	@Autowired
	private HongbaodianOrderCmdService hongbaodianOrderCmdService;

	@Autowired
	private WXPayService wxPayService;

	@Autowired
	private HongbaodianRecordMsgService hongbaodianRecordMsgService;

	@Autowired
	private HongbaodianOrderMsgService hongbaodianOrderMsgService;

	private ExecutorService executorService = Executors.newCachedThreadPool();

	/**
	 * 添加红包点商品
	 */
	@RequestMapping("add_product")
	public CommonVO addHongbaodianProduct(@RequestBody HongbaodianProduct product) {
		CommonVO vo = new CommonVO();
		hongbaodianProductService.insertHongbaodianProduct(product);
		hongbaodianProductMsgService.addHongbaodianProduct(product);
		return vo;
	}

	/**
	 * 修改红包点商品
	 */
	@RequestMapping("update_product")
	public CommonVO updateHongbaodianProduct(@RequestBody HongbaodianProduct product) {
		CommonVO vo = new CommonVO();
		hongbaodianProductService.updateHongbaodianProduct(product);
		hongbaodianProductMsgService.updateHongbaodianProduct(product);
		return vo;
	}

	/**
	 * 删除红包点商品
	 */
	@RequestMapping("remove_product_by_id")
	public CommonVO removeHongbaodianProductById(@RequestBody String[] productIds) {
		CommonVO vo = new CommonVO();
		hongbaodianProductService.removeHongbaodianProductById(productIds);
		hongbaodianProductMsgService.removeHongbaodianProduct(productIds);
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

	/**
	 * 红包点兑换
	 */
	@RequestMapping("/buy_hongbaodianproduct")
	public CommonVO buyHongbaodianProduct(String token, String productId, HttpServletRequest request) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		HongbaodianProduct product = hongbaodianProductService.findHongbaodianProductByProductId(productId);
		if (product == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid product");
			return vo;
		}
		int price = product.getPrice();
		AuthorizationDbo openAthDbo = memberAuthQueryService.findAuthorizationDboByMemberIdAndPublisher(memberId,
				"open.weixin.app.qipai");
		if (openAthDbo == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid openid");
			return vo;
		}
		String reqIP = IPUtil.getRealIp(request);
		try {
			// 创建订单
			HongbaodianOrder order = hongbaodianOrderService.createOrder("buy" + product.getName(), productId, memberId,
					memberId, reqIP);
			hongbaodianOrderCmdService.createOrder(order.getId());
			hongbaodianOrderMsgService.recordHongbaodianOrder(order);
			// 支付红包点
			AccountingRecord record = memberHongbaodianCmdService.withdraw(memberId, price, "buy hongbaodianproduct",
					System.currentTimeMillis());
			MemberHongbaodianRecordDbo dbo = memberHongbaodianService.withdraw(record, memberId);
			hongbaodianRecordMsgService.newRecord(dbo);
			// 返利
			if (order.getRewardType().equals(RewardType.HONGBAORMB)) {// 现金返利
				giveRewardRMBToMember(order, price);
			}
		} catch (MemberNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("MemberNotFoundException");
			return vo;
		} catch (InsufficientBalanceException e) {
			vo.setSuccess(false);
			vo.setMsg("InsufficientBalanceException");
			return vo;
		} catch (OrderHasAlreadyExistenceException e) {
			vo.setSuccess(false);
			vo.setMsg("OrderHasAlreadyExistenceException");
			return vo;
		}
		return vo;
	}

	/**
	 * 红包奖励
	 */
	private void giveRewardRMBToMember(HongbaodianOrder order, int price) {
		// 实际仍是单线程
		executorService.submit(() -> {
			try {
				Map<String, String> responseMap = wxPayService.rewardAgent(order);
				hongbaodianOrderCmdService.finishOrder(order.getId());
				HongbaodianOrder finishOrder = hongbaodianOrderService.finishOrder(order, responseMap, "FINISH");
				hongbaodianOrderMsgService.finishHongbaodianOrder(finishOrder);
			} catch (Exception e) {
				// 奖励失败时由后台客服补偿
				e.printStackTrace();
			}
		});
	}
}
