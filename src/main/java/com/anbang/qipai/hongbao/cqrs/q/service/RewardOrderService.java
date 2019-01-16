package com.anbang.qipai.hongbao.cqrs.q.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.cqrs.q.dao.AuthorizationDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dao.PayInfoDao;
import com.anbang.qipai.hongbao.cqrs.q.dao.RewardOrderDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.PayInfo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.RewardOrderDbo;
import com.anbang.qipai.hongbao.util.IPUtil;

@Service
public class RewardOrderService {
	@Autowired
	private RewardOrderDboDao rewardOrderDboDao;

	@Autowired
	private PayInfoDao payInfoDao;

	@Autowired
	private MemberDboDao memberDboDao;

	@Autowired
	private AuthorizationDboDao authorizationDboDao;

	public RewardOrderDbo createOrder(String desc, double rewardAmount, String receiverId) {
		RewardOrderDbo order = new RewardOrderDbo();
		String id = UUID.randomUUID().toString().replace("-", "");
		order.setId(id);
		// 收货人
		MemberDbo receiver = memberDboDao.findByMemberId(receiverId);
		order.setReceiverId(receiver.getId());
		order.setReceiverName(receiver.getNickname());
		AuthorizationDbo receiverAuthDbo = authorizationDboDao.findAuthorizationDboByMemberIdAndPublisher(true,
				receiverId, "open.weixin.app.qipai");
		order.setReceiverOpenId(receiverAuthDbo.getUuid());
		order.setRewardRMB(rewardAmount);
		order.setStatus("PROCESSING");
		order.setCreateTime(System.currentTimeMillis());
		order.setDesc(desc);
		String spbill_create_ip = null;
		try {
			spbill_create_ip = IPUtil.getLocalHostRelIP();
		} catch (Exception e) {
		}
		order.setSpbill_create_ip(spbill_create_ip);
		rewardOrderDboDao.insert(order);

		PayInfo info = new PayInfo();
		info.setOrderId(order.getId());
		info.setDesc(order.getDesc());
		info.setAmount(order.getRewardRMB());
		info.setCreateTime(System.currentTimeMillis());
		info.setSpbill_create_ip(spbill_create_ip);
		payInfoDao.insert(info);
		return order;
	}

	public RewardOrderDbo finishOrder(RewardOrderDbo order, Map<String, String> responseMap, String status) {
		rewardOrderDboDao.updateFinishTime(order.getId(), System.currentTimeMillis());
		rewardOrderDboDao.updateStatus(order.getId(), status);
		payInfoDao.updateReturnParamsByOrderId(order.getId(), responseMap);
		payInfoDao.updateFinishTime(order.getId(), System.currentTimeMillis());
		return rewardOrderDboDao.findById(order.getId());
	}
}
