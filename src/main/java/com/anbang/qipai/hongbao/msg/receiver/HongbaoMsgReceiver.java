package com.anbang.qipai.hongbao.msg.receiver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderHasAlreadyExistenceException;
import com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder.OrderNotFoundException;
import com.anbang.qipai.hongbao.cqrs.c.service.HongbaodianOrderCmdService;
import com.anbang.qipai.hongbao.cqrs.q.dbo.RewardOrderDbo;
import com.anbang.qipai.hongbao.cqrs.q.service.RewardOrderService;
import com.anbang.qipai.hongbao.msg.channel.sink.HongbaoSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.msg.service.RewardOrderDboMsgService;
import com.anbang.qipai.hongbao.plan.service.WXPayService;
import com.google.gson.Gson;

@EnableBinding(HongbaoSink.class)
public class HongbaoMsgReceiver {

	@Autowired
	private RewardOrderService rewardOrderService;

	@Autowired
	private HongbaodianOrderCmdService hongbaodianOrderCmdService;

	@Autowired
	private WXPayService wxPayService;

	@Autowired
	private RewardOrderDboMsgService rewardOrderDboMsgService;

	private Gson gson = new Gson();

	private ExecutorService executorService = Executors.newCachedThreadPool();

	/**
	 * 任务完成发放红包
	 */
	@StreamListener(HongbaoSink.MEMBERHONGBAORMBACCOUNTING)
	public void recordMembers(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("give_hongbaormb_to_member".equals(msg)) {
			Map data = gson.fromJson(json, Map.class);
			String memberId = (String) data.get("memberId");
			double amount = (Double) data.get("amount");
			String textSummary = (String) data.get("textSummary");
			// 创建订单
			RewardOrderDbo order = rewardOrderService.createOrder(textSummary, amount, memberId);
			try {
				hongbaodianOrderCmdService.createOrder(order.getId());
				rewardOrderDboMsgService.recordRewardOrderDbo(order);
				// 测试
				Map<String, String> responseMap = new HashMap<>();
				responseMap.put("result", "test");
				hongbaodianOrderCmdService.finishOrder(order.getId());
				RewardOrderDbo finishOrder = rewardOrderService.finishOrder(order, responseMap, "FINISH");
				rewardOrderDboMsgService.finishRewardOrderDbo(finishOrder);
			} catch (OrderHasAlreadyExistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OrderNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 返利
			// if (order.getRewardRMB() > 0) {// 现金返利
			// // 实际仍是单线程
			// executorService.submit(() -> {
			// try {
			// String status = "FINISH";
			// Map<String, String> responseMap = wxPayService.reward(order);
			// String return_code = responseMap.get("return_code");
			// if ("SUCCESS".equals(return_code)) {
			// String result_code = responseMap.get("result_code");
			// if ("SUCCESS".equals(result_code)) {
			// status = responseMap.get("status");
			// }
			// }
			// hongbaodianOrderCmdService.finishOrder(order.getId());
			// RewardOrderDbo finishOrder = rewardOrderService.finishOrder(order,
			// responseMap, status);
			// rewardOrderDboMsgService.finishRewardOrderDbo(finishOrder);
			// } catch (Exception e) {
			// // 奖励失败时由后台客服补偿
			// e.printStackTrace();
			// }
			// });
			// }
		}
	}
}
