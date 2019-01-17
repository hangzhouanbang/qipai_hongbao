package com.anbang.qipai.hongbao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.hongbao.conf.IPVerifyConfig;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderHasAlreadyExistenceException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.TimeLimitException;
import com.anbang.qipai.hongbao.cqrs.c.domain.member.MemberNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.service.HongbaodianOrderCmdService;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberHongbaodianCmdService;
import com.anbang.qipai.hongbao.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianOrder;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianProduct;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianRecordDbo;
import com.anbang.qipai.hongbao.cqrs.q.service.HongbaodianOrderService;
import com.anbang.qipai.hongbao.cqrs.q.service.HongbaodianProductService;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberHongbaodianService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianOrderMsgService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianProductMsgService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianRecordMsgService;
import com.anbang.qipai.hongbao.plan.bean.WhiteList;
import com.anbang.qipai.hongbao.plan.service.MemberLoginRecordService;
import com.anbang.qipai.hongbao.plan.service.WXPayService;
import com.anbang.qipai.hongbao.plan.service.WhiteListService;
import com.anbang.qipai.hongbao.util.HttpUtil;
import com.anbang.qipai.hongbao.util.IPUtil;
import com.anbang.qipai.hongbao.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.google.gson.Gson;
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
	private MemberLoginRecordService memberLoginRecordService;

	@Autowired
	private WhiteListService whiteListService;

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

	private Gson gson = new Gson();

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
	 * 查询共兑换红包
	 */
	@RequestMapping("query_rewardnum")
	public CommonVO queryRewardNum(String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
		}
		double totalRewardNum = hongbaodianOrderService.countTotalRewardNumByReceiverId(memberId);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("totalRewardNum", totalRewardNum);
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
		WhiteList whitelist = whiteListService.findByPlayerIdAndLoginIP(memberId, reqIP);
		if (whitelist == null && !verifyReqIP(reqIP)) {// ip不在白名单并且无效
			vo.setSuccess(false);
			vo.setMsg("invalid ip");
			return vo;
		}
		try {
			// 创建订单
			HongbaodianOrder order = hongbaodianOrderService.createOrder("buy" + product.getName(), productId, memberId,
					memberId, reqIP);
			hongbaodianOrderCmdService.createOrder(order.getId(), memberId, System.currentTimeMillis());
			hongbaodianOrderMsgService.recordHongbaodianOrder(order);
			// 支付红包点
			AccountingRecord record = memberHongbaodianCmdService.withdraw(memberId, price, "buy hongbaodianproduct",
					System.currentTimeMillis());
			MemberHongbaodianRecordDbo dbo = memberHongbaodianService.withdraw(record, memberId);
			hongbaodianRecordMsgService.newRecord(dbo);
			// 返利
			// if (order.getRewardType().equals(RewardType.HONGBAORMB)) {// 现金返利
			// giveRewardRMBToMember(order);
			// }
			// 测试
			Map<String, String> responseMap = new HashMap<>();
			responseMap.put("result", "test");
			hongbaodianOrderCmdService.finishOrder(order.getId());
			HongbaodianOrder finishOrder = hongbaodianOrderService.finishOrder(order, responseMap, "FINISH");
			hongbaodianOrderMsgService.finishHongbaodianOrder(finishOrder);
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
		} catch (OrderNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("OrderNotFoundException");
			return vo;
		} catch (TimeLimitException e) {
			long limitTime = hongbaodianOrderCmdService.queryLimitTime(memberId);
			Map data = new HashMap<>();
			data.put("remain", System.currentTimeMillis() - limitTime);
			vo.setData(data);
			vo.setSuccess(false);
			vo.setMsg("TimeLimitException");
			return vo;
		}
		return vo;
	}

	/**
	 * 红包奖励
	 */
	private String giveRewardRMBToMember(HongbaodianOrder order) throws Exception {
		String reason = null;
		String status = "FINISH";
		Map<String, String> responseMap = wxPayService.reward(order);
		String return_code = responseMap.get("return_code");
		String return_msg = responseMap.get("return_msg");
		reason = return_msg;
		if ("SUCCESS".equals(return_code)) {
			String result_code = responseMap.get("result_code");
			String err_code_des = responseMap.get("err_code_des");
			reason = err_code_des;
			if ("SUCCESS".equals(result_code)) {
				status = responseMap.get("status");
				reason = responseMap.get("reason");
			}
		}
		hongbaodianOrderCmdService.finishOrder(order.getId());
		HongbaodianOrder finishOrder = hongbaodianOrderService.finishOrder(order, responseMap, status);
		hongbaodianOrderMsgService.finishHongbaodianOrder(finishOrder);
		return reason;
	}

	/**
	 * 验证ip
	 */
	private boolean verifyReqIP(String reqIP) {
		int num = memberLoginRecordService.countMemberNumByLoginIp(reqIP);
		if (num > 4) {// 有4个以上的账号用该IP做登录
			return false;
		}
		String host = "https://ali-ip.showapi.com";
		String path = "/ip";
		String method = "GET";
		String appcode = IPVerifyConfig.APPCODE;
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("ip", reqIP);

		try {
			HttpResponse response = HttpUtil.doGet(host, path, method, headers, querys);
			Map map = gson.fromJson(EntityUtils.toString(response.getEntity()), Map.class);
			String showapi_res_body = (String) map.get("showapi_res_body");
			Map data = gson.fromJson(showapi_res_body, Map.class);
			String city = (String) data.get("city");
			if (city.equals("温州")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
