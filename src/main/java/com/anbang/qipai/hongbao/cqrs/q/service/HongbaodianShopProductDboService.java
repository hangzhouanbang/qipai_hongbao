package com.anbang.qipai.hongbao.cqrs.q.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.conf.HongbaodianProductRecordStatus;
import com.anbang.qipai.hongbao.cqrs.q.dao.HongbaodianShopProductDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dao.ReceiverInfoDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianShopProductDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.ReceiverInfoDbo;
import com.anbang.qipai.hongbao.plan.bean.HongbaodianProductRecord;
import com.anbang.qipai.hongbao.plan.bean.ProductType;
import com.anbang.qipai.hongbao.plan.dao.HongbaodianProductRecordDao;
import com.anbang.qipai.hongbao.plan.dao.ProductTypeDao;
import com.anbang.qipai.hongbao.util.IPAddressUtil;
import com.highto.framework.web.page.ListPage;

@Service
public class HongbaodianShopProductDboService {

	@Autowired
	private ReceiverInfoDboDao receiverInfoDboDao;

	@Autowired
	private MemberDboDao memberDboDao;

	@Autowired
	private HongbaodianShopProductDboDao hongbaodianShopProductDboDao;

	@Autowired
	private ProductTypeDao productTypeDao;

	@Autowired
	private HongbaodianProductRecordDao hongbaodianProductRecordDao;

	public ProductType findProductTypeById(String id) {
		return productTypeDao.findById(id);
	}

	public void saveProductType(ProductType type) {
		productTypeDao.save(type);
	}

	public void removeProductTypeByIds(String[] ids) {
		productTypeDao.removeByIds(ids);
	}

	public ProductType updateDescProductTypeById(String id, String desc) {
		productTypeDao.updateDescById(id, desc);
		return productTypeDao.findById(id);
	}

	public List<ProductType> findAllProductType() {
		return productTypeDao.findAll();
	}

	public void saveHongbaodianShopProductDbo(HongbaodianShopProductDbo dbo) {
		hongbaodianShopProductDboDao.save(dbo);
	}

	public void updateHongbaodianShopProductDbo(HongbaodianShopProductDbo dbo) {
		hongbaodianShopProductDboDao.update(dbo);
	}

	public void removeHongbaodianShopProductDboByIds(String[] ids) {
		hongbaodianShopProductDboDao.removeByIds(ids);
	}

	public void saveAllHongbaodianShopProductDbo(List<HongbaodianShopProductDbo> products) {
		hongbaodianShopProductDboDao.removeAll();
		hongbaodianShopProductDboDao.saveAll(products);
	}

	public void clearAllHongbaodianShopProductDbo() {
		hongbaodianShopProductDboDao.removeAll();
	}

	public HongbaodianShopProductDbo findHongbaodianShopProductDboById(String id) {
		return hongbaodianShopProductDboDao.findById(id);
	}

	public ListPage findHongbaodianShopProductDboByType(int page, int size, String type) {
		int amount = (int) hongbaodianShopProductDboDao.countByType(type);
		List<HongbaodianShopProductDbo> list = hongbaodianShopProductDboDao.findByType(page, size, type);
		return new ListPage(list, page, size, amount);
	}

	public void incHongbaodianShopProductDboRemainById(String id, int amount) {
		hongbaodianShopProductDboDao.incRemainById(id, amount);
	}

	public HongbaodianShopProductDbo upateHongbaodianShopProductDboRemainById(String id, int remain) {
		hongbaodianShopProductDboDao.updateRemainById(id, remain);
		return hongbaodianShopProductDboDao.findById(id);
	}

	public HongbaodianProductRecord saveHongbaodianProductRecord(String requestIP, HongbaodianShopProductDbo product,
			String memberId, int amount) {
		MemberDbo member = memberDboDao.findByMemberId(memberId);
		ReceiverInfoDbo info = receiverInfoDboDao.findByMemberId(memberId);
		HongbaodianProductRecord record = new HongbaodianProductRecord();
		record.setAdress(info.getAddress());
		record.setAmount(amount);
		record.setCreateTime(System.currentTimeMillis());
		record.setHeadimgurl(member.getHeadimgurl());
		record.setIpLocation(IPAddressUtil.getIPAddress2(requestIP));
		record.setMemberId(memberId);
		record.setName(info.getName());
		record.setPhone(info.getTelephone());
		record.setNickname(member.getNickname());
		record.setProduct(product.getDesc());
		record.setReqIP(requestIP);
		record.setStatus(HongbaodianProductRecordStatus.PROCESS);
		hongbaodianProductRecordDao.save(record);
		return record;
	}

	public HongbaodianProductRecord finishHongbaodianProductRecord(String id, long deliverTime, String status) {
		hongbaodianProductRecordDao.updateDeliverTimeById(id, deliverTime);
		hongbaodianProductRecordDao.updateStatusById(id, status);
		return hongbaodianProductRecordDao.findById(id);
	}

	public ListPage findHongbaodianProductRecordByMemberId(int page, int size, String memberId) {
		int amount = (int) hongbaodianProductRecordDao.countByMemberIdAndStatus(memberId, null);
		List<HongbaodianProductRecord> list = hongbaodianProductRecordDao.findByMemberIdAndStatus(page, size, memberId,
				null);
		return new ListPage(list, page, size, amount);
	}
}
