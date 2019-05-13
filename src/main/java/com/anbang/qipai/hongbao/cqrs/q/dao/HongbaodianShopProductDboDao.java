package com.anbang.qipai.hongbao.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianShopProductDbo;

public interface HongbaodianShopProductDboDao {

	void save(HongbaodianShopProductDbo dbo);

	void update(HongbaodianShopProductDbo dbo);

	void removeByIds(String[] ids);

	void removeAll();

	void saveAll(List<HongbaodianShopProductDbo> products);

	HongbaodianShopProductDbo findById(String id);

	long countByType(String type);

	List<HongbaodianShopProductDbo> findByType(int page, int size, String type);

	void incRemainById(String id, int amount);

	void updateRemainById(String id, int remain);
}
