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
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberDbo;
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
	public CommonVO giveHongbaoToMember(String memberId, double amount, String textSummary) {
		CommonVO vo = new CommonVO();
		AuthorizationDbo openAthDbo = memberAuthQueryService.findAuthorizationDboByMemberIdAndPublisher(memberId,
				"open.weixin.app.qipai");
		if (openAthDbo == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid openid");
			return vo;
		}
		MemberDbo member = memberAuthQueryService.findByMemberId(memberId);
		WhiteList whitelist = whiteListService.findByPlayerId(memberId);
		if (whitelist == null && !verifyReqIP(member.getReqIP())) {// ip不在白名单并且无效
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
			// long limitTime = hongbaodianOrderCmdService.queryLimitTime(memberId);
			// Map data = new HashMap<>();
			// data.put("remain", System.currentTimeMillis() - limitTime);
			// vo.setData(data);
			// vo.setSuccess(false);
			// vo.setMsg("TimeLimitException");
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
	private String giveRewardRMBToMember(RewardOrderDbo order) throws Exception {
		String reason = null;
		String status = "FINISH";
		Map<String, String> responseMap = wxPayService.reward(order);
		Map<String, String> queryMap = wxPayService.query(order);
		String return_code = queryMap.get("return_code");
		String return_msg = queryMap.get("return_msg");
		reason = return_msg;
		if ("SUCCESS".equals(return_code)) {
			String result_code = queryMap.get("result_code");
			String err_code_des = queryMap.get("err_code_des");
			reason = err_code_des;
			if ("SUCCESS".equals(result_code)) {
				status = queryMap.get("status");
				reason = queryMap.get("reason");
			}
		}
		hongbaodianOrderCmdService.finishOrder(order.getId());
		RewardOrderDbo finishOrder = rewardOrderService.finishOrder(order, responseMap, queryMap, status);
		rewardOrderDboMsgService.finishRewardOrderDbo(finishOrder);
		return reason;
	}

	/**
	 * 验证ip
	 */
	private boolean verifyReqIP(String reqIP) {
		if (reqIP == null) {
			return false;
		}
		int num = memberLoginRecordService.countMemberNumByLoginIp(reqIP);
		if (num > 4) {// 有4个以上的账号用该IP做登录
			return false;
		}
		String host = "http://iploc.market.alicloudapi.com";
		String path = "/v3/ip";
		String method = "GET";
		String appcode = IPVerifyConfig.APPCODE;
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("ip", reqIP);

		try {
			HttpResponse response = HttpUtil.doGet(host, path, method, headers, querys);
			String entity = EntityUtils.toString(response.getEntity());
			Map map = gson.fromJson(entity, Map.class);
			String status = (String) map.get("status");
			String info = (String) map.get("info");
			String infocode = (String) map.get("infocode");
			String province = (String) map.get("province");
			String adcode = (String) map.get("adcode");
			String city = (String) map.get("city");
			if (status.equals("1") && info.equals("OK") && province.equals("浙江省") && infocode.equals("10000")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
