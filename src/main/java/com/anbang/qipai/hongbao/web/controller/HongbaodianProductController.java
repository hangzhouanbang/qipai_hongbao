package com.anbang.qipai.hongbao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.hongbao.conf.IPVerifyConfig;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderHasAlreadyExistenceException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.TimeLimitException;
import com.anbang.qipai.hongbao.cqrs.c.domain.member.MemberNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.service.HongbaodianOrderCmdService;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberHongbaodianCmdService;
import com.anbang.qipai.hongbao.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianOrder;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianProduct;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianAccountDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianRecordDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.RewardType;
import com.anbang.qipai.hongbao.cqrs.q.service.HongbaodianOrderService;
import com.anbang.qipai.hongbao.cqrs.q.service.HongbaodianProductService;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberHongbaodianService;
import com.anbang.qipai.hongbao.msg.service.BlackListMsgService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianOrderMsgService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianProductMsgService;
import com.anbang.qipai.hongbao.msg.service.HongbaodianRecordMsgService;
import com.anbang.qipai.hongbao.plan.bean.BlackList;
import com.anbang.qipai.hongbao.plan.bean.IPVerifyDbo;
import com.anbang.qipai.hongbao.plan.bean.MemberLoginLimitRecord;
import com.anbang.qipai.hongbao.plan.bean.WhiteList;
import com.anbang.qipai.hongbao.plan.service.IPVerifyService;
import com.anbang.qipai.hongbao.plan.service.MemberLoginLimitRecordService;
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
	private IPVerifyService iPVerifyService;

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

	@Autowired
	private MemberLoginLimitRecordService memberLoginLimitRecordService;

	@Autowired
	private BlackListMsgService blackListMsgService;

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
	@RequestMapping("/query_rewardnum")
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
		MemberLoginLimitRecord loginLimitRecord = memberLoginLimitRecordService.findByMemberId(memberId, true);
		if (loginLimitRecord != null) {
			vo.setSuccess(false);
			vo.setMsg("login limited");
			return vo;
		}
		HongbaodianProduct product = hongbaodianProductService.findHongbaodianProductByProductId(productId);
		if (product == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid product");
			return vo;
		}
		MemberHongbaodianAccountDbo account = memberHongbaodianService.findAccountByMemberId(memberId);
		int price = product.getPrice();
		if (account == null || account.getBalance() < price) {
			vo.setSuccess(false);
			vo.setMsg("InsufficientBalanceException");
			return vo;
		}
		AuthorizationDbo openAthDbo = memberAuthQueryService.findAuthorizationDboByMemberIdAndPublisher(memberId,
				"open.weixin.app.qipai");
		if (openAthDbo == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid openid");
			return vo;
		}
		MemberDbo member = memberAuthQueryService.findByMemberId(memberId);
		WhiteList whitelist = whiteListService.findByPlayerId(memberId);
		String reqIP = IPUtil.getRealIp(request);
		if (whitelist == null) {// ip不在白名单
			if (StringUtils.isBlank(member.getPhone())) {// 是否绑定手机
				vo.setSuccess(false);
				vo.setMsg("invalid phone");
				return vo;
			}
			BlackList blackList = whiteListService.findBlackListByPlayerId(memberId);
			if (blackList != null) {// 是否在黑名名单
				vo.setSuccess(false);
				vo.setMsg("in blacklist");
				return vo;
			}
			if (!verifyReqIP(request, member.getReqIP(), memberId)) {// 同注册IP下是否有＞4个ID的账号
				vo.setSuccess(false);
				vo.setMsg("invalid ip");
				return vo;
			}
			// 检测该账号是否进行过＞3次的【羊毛检测】（次数每日0点重置）
			int verifyCount = iPVerifyService.countVerifyAmountTodayByMemberId(memberId);
			if (verifyCount > 3) {
				vo.setSuccess(false);
				vo.setMsg("verify limit");
				return vo;
			}
			// 羊毛检测
			boolean verify = verify(request, member.getReqIP(), memberId, member.getPhone());
			if (!verify) {
				vo.setSuccess(false);
				vo.setMsg("verify fail");
				return vo;
			}
		}
		try {
			// 创建订单
			HongbaodianOrder order = hongbaodianOrderService.createOrder("buy" + product.getName(), productId, memberId,
					memberId, reqIP);
			hongbaodianOrderCmdService.createOrder(order.getId(), memberId, System.currentTimeMillis());
			hongbaodianOrderMsgService.recordHongbaodianOrder(order);
			// 支付红包点
			AccountingRecord record = memberHongbaodianCmdService.withdraw(memberId, price, "buy" + product.getName(),
					System.currentTimeMillis());
			MemberHongbaodianRecordDbo dbo = memberHongbaodianService.withdraw(record, memberId);
			hongbaodianRecordMsgService.newRecord(dbo);
			// 返利
			if (order.getRewardType().equals(RewardType.HONGBAORMB)) {// 现金返利
				try {
					String reason = giveRewardRMBToMember(order);
					if (!StringUtil.isBlank(reason)) {
						vo.setSuccess(false);
						vo.setMsg(reason);
						return vo;
					}
				} catch (Exception e) {
					vo.setSuccess(false);
					vo.setMsg(e.getClass().getName());
					return vo;
				}
			}
			// 测试
			// Map<String, String> responseMap = new HashMap<>();
			// responseMap.put("result", "test");
			// hongbaodianOrderCmdService.finishOrder(order.getId());
			// HongbaodianOrder finishOrder = hongbaodianOrderService.finishOrder(order,
			// responseMap, "FINISH");
			// hongbaodianOrderMsgService.finishHongbaodianOrder(finishOrder);
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
		} catch (TimeLimitException e) {
			long limitTime = hongbaodianOrderCmdService.queryLimitTime(memberId);
			Map data = new HashMap<>();
			data.put("remain", System.currentTimeMillis() - limitTime);
			vo.setData(data);
			vo.setSuccess(false);
			vo.setMsg("TimeLimitException");
			return vo;
			// } catch (OrderNotFoundException e) {
			// vo.setSuccess(false);
			// vo.setMsg("OrderNotFoundException");
			// return vo;
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
		Map<String, String> queryMap = wxPayService.query(order);
		String return_code = responseMap.get("return_code");
		String return_msg = responseMap.get("return_msg");
		reason = return_msg;
		if ("SUCCESS".equals(return_code)) {
			String err_code = responseMap.get("err_code");
			reason = err_code;
			if ("NOTENOUGH".equals(reason)) {// 余额不足
				AccountingRecord record = memberHongbaodianCmdService.giveHongbaodianToMember(order.getPayerId(),
						order.getProductPrice(), "return hongbaodian", System.currentTimeMillis());
				MemberHongbaodianRecordDbo dbo = memberHongbaodianService.withdraw(record, order.getPayerId());
				hongbaodianRecordMsgService.newRecord(dbo);
				hongbaodianOrderCmdService.finishOrder(order.getId());
				HongbaodianOrder finishOrder = hongbaodianOrderService.finishOrder(order, responseMap, queryMap,
						status);
				hongbaodianOrderMsgService.finishHongbaodianOrder(finishOrder);
				return reason;
			}
		}
		String return_code_query = queryMap.get("return_code");
		String return_msg_query = queryMap.get("return_msg");
		reason = return_msg_query;
		if ("SUCCESS".equals(return_code_query)) {
			String result_code = queryMap.get("result_code");
			String err_code_des = queryMap.get("err_code_des");
			reason = err_code_des;
			if ("SUCCESS".equals(result_code)) {
				status = queryMap.get("status");
				reason = queryMap.get("reason");
			}
		}
		hongbaodianOrderCmdService.finishOrder(order.getId());
		HongbaodianOrder finishOrder = hongbaodianOrderService.finishOrder(order, responseMap, queryMap, status);
		hongbaodianOrderMsgService.finishHongbaodianOrder(finishOrder);
		return reason;
	}

	/**
	 * 验证ip
	 */
	private boolean verifyReqIP(HttpServletRequest request, String reqIP, String memberId) {
		if (reqIP == null) {
			return false;
		}
		if (!IPUtil.verifyIp(request)) {
			return false;
		}
		int num = memberLoginRecordService.countMemberNumByLoginIp(reqIP);
		if (num > 4) {// 有4个以上的账号用该IP做登录
			BlackList blackList = new BlackList();
			blackList.setPlayerId(memberId);
			blackList.setAddTime(System.currentTimeMillis());
			blackList.setRemark("有4个以上的账号用该IP做登录");
			blackList.setReqIP(reqIP);
			whiteListService.saveBlackList(blackList);
			blackListMsgService.addBlackList(blackList);
			return false;
		}
		return true;
	}

	/**
	 * 羊毛检测
	 */
	private boolean verify(HttpServletRequest request, String reqIP, String memberId, String phone) {
		String host = "https://api.253.com";
		String path = "/open/wool/wcheck";
		String method = "POST";
		Map<String, String> headers = new HashMap<String, String>();
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("appId", IPVerifyConfig.APPID);
		querys.put("appKey", IPVerifyConfig.APPKEY);
		querys.put("mobile", phone);
		String ip = IPUtil.getRealIp(request);
		if (!StringUtils.isBlank(ip)) {
			querys.put("ip", ip);
		}
		querys.put("type", "0");

		try {
			HttpResponse response = HttpUtil.doPost(host, path, method, headers, querys, new HashMap<String, String>());
			String entity = EntityUtils.toString(response.getEntity());
			Map map = gson.fromJson(entity, Map.class);
			String chargeStatus = (String) map.get("chargeStatus");
			String code = (String) map.get("code");
			String message = (String) map.get("message");
			Map data = (Map) map.get("data");
			IPVerifyDbo dbo = new IPVerifyDbo();
			dbo.setMemberId(memberId);
			dbo.setChargeStatus(chargeStatus);
			dbo.setCode(code);
			dbo.setMessage(message);
			dbo.setData(data);
			dbo.setCreateTime(System.currentTimeMillis());
			iPVerifyService.saveIPVerifyDbo(dbo);
			if (data != null) {
				String mobile = (String) data.get("mobile");
				String status = (String) data.get("status");
				if ("200000".equals(code) && phone.equals(mobile) && "W1".equals(status)) {
					return true;
				}
			}
			BlackList blackList = new BlackList();
			blackList.setPlayerId(memberId);
			blackList.setAddTime(System.currentTimeMillis());
			blackList.setRemark("羊毛检测不通过");
			blackList.setReqIP(reqIP);
			whiteListService.saveBlackList(blackList);
			blackListMsgService.addBlackList(blackList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
