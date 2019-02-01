package com.anbang.qipai.hongbao.cqrs.q.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.cqrs.q.dao.AuthorizationDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dao.HongbaodianOrderDao;
import com.anbang.qipai.hongbao.cqrs.q.dao.HongbaodianProductDao;
import com.anbang.qipai.hongbao.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dao.PayInfoDao;
import com.anbang.qipai.hongbao.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianOrder;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianProduct;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.PayInfo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.RewardType;
import com.anbang.qipai.hongbao.util.IPUtil;

@Service
public class HongbaodianOrderService {

	@Autowired
	private HongbaodianOrderDao hongbaodianOrderDao;

	@Autowired
	private PayInfoDao payInfoDao;

	@Autowired
	private MemberDboDao memberDboDao;

	@Autowired
	private HongbaodianProductDao hongbaodianProductDao;

	@Autowired
	private AuthorizationDboDao authorizationDboDao;

	public HongbaodianOrder createOrder(String desc, String productId, String payerId, String receiverId,
			String reqIP) {
		HongbaodianOrder order = new HongbaodianOrder();
		String id = UUID.randomUUID().toString().replace("-", "");
		order.setId(id);
		order.setReqIP(reqIP);
		// 支付人
		MemberDbo payer = memberDboDao.findByMemberId(payerId);
		order.setPayerId(payer.getId());
		order.setPayerName(payer.getNickname());
		AuthorizationDbo payerAuthDbo = authorizationDboDao.findAuthorizationDboByMemberIdAndPublisher(true, payerId,
				"open.weixin.app.qipai");
		order.setPayerOpenId(payerAuthDbo.getUuid());
		// 商品
		HongbaodianProduct product = hongbaodianProductDao.findById(productId);
		order.setProductId(product.getId());
		order.setProduceName(product.getName());
		order.setProductPrice(product.getPrice());
		order.setRewardType(product.getRewardType());
		order.setRewardNum(product.getRewardNum());
		// 收货人
		MemberDbo receiver = memberDboDao.findByMemberId(receiverId);
		order.setReceiverId(receiver.getId());
		order.setReceiverName(receiver.getNickname());
		AuthorizationDbo receiverAuthDbo = authorizationDboDao.findAuthorizationDboByMemberIdAndPublisher(true,
				receiverId, "open.weixin.app.qipai");
		order.setReceiverOpenId(receiverAuthDbo.getUuid());
		String spbill_create_ip = null;
		try {
			spbill_create_ip = IPUtil.getLocalHostRelIP();
		} catch (Exception e) {
		}
		order.setSpbill_create_ip(spbill_create_ip);
		order.setStatus("PROCESSING");
		order.setCreateTime(System.currentTimeMillis());
		order.setDesc(desc);
		hongbaodianOrderDao.insert(order);

		if (order.getRewardType().equals(RewardType.HONGBAORMB)) {
			PayInfo info = new PayInfo();
			info.setOrderId(order.getId());
			info.setDesc(order.getDesc());
			info.setAmount(order.getRewardNum());
			info.setCreateTime(System.currentTimeMillis());
			info.setSpbill_create_ip(spbill_create_ip);
			payInfoDao.insert(info);
		}
		return order;
	}

	public HongbaodianOrder finishOrder(HongbaodianOrder order, Map<String, String> responseMap, String status) {
		hongbaodianOrderDao.updateFinishTime(order.getId(), System.currentTimeMillis());
		hongbaodianOrderDao.updateStatus(order.getId(), status);
		if (order.getRewardType().equals(RewardType.HONGBAORMB)) {
			payInfoDao.updateReturnParamsByOrderId(order.getId(), responseMap);
			payInfoDao.updateFinishTime(order.getId(), System.currentTimeMillis());
		}
		return hongbaodianOrderDao.findById(order.getId());
	}

	public double countTotalRewardNumByReceiverId(String receiverId) {
		return hongbaodianOrderDao.countTotalRewardNumByReceiverId(receiverId);
	}
}
