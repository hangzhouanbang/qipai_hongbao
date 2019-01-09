package com.anbang.qipai.hongbao.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianProduct;

public interface HongbaodianProductDao {

	void insert(HongbaodianProduct product);

	void update(HongbaodianProduct product);

	void remove(String id);

	int countAmount();

	List<HongbaodianProduct> findAllHongbaodianProducts(int page, int size);
}
