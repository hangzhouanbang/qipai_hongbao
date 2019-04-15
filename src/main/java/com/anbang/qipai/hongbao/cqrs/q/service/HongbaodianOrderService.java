package com.anbang.qipai.hongbao.cqrs.q.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.conf.IPVerifyConfig;
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
import com.anbang.qipai.hongbao.util.HttpUtil;
import com.anbang.qipai.hongbao.util.IPUtil;
import com.google.gson.Gson;

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
		location(reqIP, order);
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

	/**
	 * 获取物理地址
	 */
	private boolean location(String reqIP, HongbaodianOrder order) {
		String host = "http://iploc.market.alicloudapi.com";
		String path = "/v3/ip";
		String method = "GET";
		String appcode = IPVerifyConfig.APPCODE;
		Map<String, String> headers = new HashMap<String, String>();
		// // 最后在header中的格式(中间是英文空格)为Authorization:APPCODE
		// 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("ip", reqIP);

		try {
			HttpResponse response = HttpUtil.doGet(host, path, method, headers, querys);
			String entity = EntityUtils.toString(response.getEntity());
			Gson gson = new Gson();
			Map map = gson.fromJson(entity, Map.class);
			String status = (String) map.get("status");
			String info = (String) map.get("info");
			String infocode = (String) map.get("infocode");
			String province = (String) map.get("province");
			String adcode = (String) map.get("adcode");
			String city = (String) map.get("city");
			order.setProvince(province);
			order.setCity(city);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public HongbaodianOrder finishOrder(HongbaodianOrder order, Map<String, String> responseMap,
			Map<String, String> queryMap, String status) {
		hongbaodianOrderDao.updateFinishTime(order.getId(), System.currentTimeMillis());
		hongbaodianOrderDao.updateStatus(order.getId(), status);
		if (order.getRewardType().equals(RewardType.HONGBAORMB)) {
			payInfoDao.updateReturnParamsByOrderId(order.getId(), responseMap);
			payInfoDao.updateQueryParamsByOrderId(order.getId(), queryMap);
			payInfoDao.updateFinishTime(order.getId(), System.currentTimeMillis());
		}
		return hongbaodianOrderDao.findById(order.getId());
	}

	public double countTotalRewardNumByReceiverId(String receiverId) {
		return hongbaodianOrderDao.countTotalRewardNumByReceiverId(receiverId);
	}
}
