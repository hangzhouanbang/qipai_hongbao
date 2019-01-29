package com.anbang.qipai.hongbao.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.hongbao.conf.IPVerifyConfig;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderHasAlreadyExistenceException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.TimeLimitException;
import com.anbang.qipai.hongbao.cqrs.c.service.HongbaodianOrderCmdService;
import com.anbang.qipai.hongbao.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.RewardOrderDbo;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.hongbao.cqrs.q.service.RewardOrderService;
import com.anbang.qipai.hongbao.msg.service.RewardOrderDboMsgService;
import com.anbang.qipai.hongbao.plan.bean.WhiteList;
import com.anbang.qipai.hongbao.plan.service.MemberLoginRecordService;
import com.anbang.qipai.hongbao.plan.service.WXPayService;
import com.anbang.qipai.hongbao.plan.service.WhiteListService;
import com.anbang.qipai.hongbao.util.HttpUtil;
import com.anbang.qipai.hongbao.web.vo.CommonVO;
import com.google.gson.Gson;

@RestController
@RequestMapping("/hongbao")
public class HongbaoController {

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private MemberLoginRecordService memberLoginRecordService;

	@Autowired
	private WhiteListService whiteListService;

	@Autowired
	private HongbaodianOrderCmdService hongbaodianOrderCmdService;

	@Autowired
	private RewardOrderService rewardOrderService;

	@Autowired
	private RewardOrderDboMsgService rewardOrderDboMsgService;

	@Autowired
	private WXPayService wxPayService;

	private Gson gson = new Gson();

	/**
	 * 红包返现
	 */
	@RequestMapping("/give_hongbao_to_member")
	public CommonVO giveHongbaoToMember(String memberId, double amount, String textSummary, String reqIP) {
		CommonVO vo = new CommonVO();
		AuthorizationDbo openAthDbo = memberAuthQueryService.findAuthorizationDboByMemberIdAndPublisher(memberId,
				"open.weixin.app.qipai");
		if (openAthDbo == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid openid");
			return vo;
		}
		WhiteList whitelist = whiteListService.findByPlayerId(memberId);
		if (whitelist == null && !verifyReqIP(reqIP)) {// ip不在白名单并且无效
			vo.setSuccess(false);
			vo.setMsg("invalid ip");
			return vo;
		}
		// 创建订单
		RewardOrderDbo order = rewardOrderService.createOrder(textSummary, amount, memberId);
		try {
			hongbaodianOrderCmdService.createOrder(order.getId(), memberId, System.currentTimeMillis());
			rewardOrderDboMsgService.recordRewardOrderDbo(order);
			// 返利
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
			// 测试
			// Map<String, String> responseMap = new HashMap<>();
			// responseMap.put("result", "test");
			// hongbaodianOrderCmdService.finishOrder(order.getId());
			// RewardOrderDbo finishOrder = rewardOrderService.finishOrder(order,
			// responseMap, "FINISH");
			// rewardOrderDboMsgService.finishRewardOrderDbo(finishOrder);
		} catch (OrderHasAlreadyExistenceException e) {
			vo.setSuccess(false);
			vo.setMsg("OrderHasAlreadyExistenceException");
			return vo;
		} catch (TimeLimitException e) {
			long limitTime = hongbaodianOrderCmdService.queryLimitTime(memberId);
			Map data = new HashMap<>();
			data.put("remain", System.currentTimeMillis() - limitTime);
			vo.setSuccess(false);
			vo.setMsg("TimeLimitException");
			return vo;
		}
		return vo;
	}

	/**
	 * 红包奖励
	 */
	private String giveRewardRMBToMember(RewardOrderDbo order) throws Exception {
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
		RewardOrderDbo finishOrder = rewardOrderService.finishOrder(order, responseMap, status);
		rewardOrderDboMsgService.finishRewardOrderDbo(finishOrder);
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
