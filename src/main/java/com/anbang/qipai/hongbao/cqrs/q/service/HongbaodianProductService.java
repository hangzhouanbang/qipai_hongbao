package com.anbang.qipai.hongbao.cqrs.q.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.cqrs.q.dao.HongbaodianProductDao;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianProduct;
import com.highto.framework.web.page.ListPage;

@Service
public class HongbaodianProductService {

	@Autowired
	private HongbaodianProductDao hongbaodianProductDao;

	public void insertHongbaodianProduct(HongbaodianProduct product) {
		hongbaodianProductDao.insert(product);
	}

	public void updateHongbaodianProduct(HongbaodianProduct product) {
		hongbaodianProductDao.update(product);
	}

	public void removeHongbaodianProductById(String[] productIds) {
		hongbaodianProductDao.remove(productIds);
	}

	public ListPage showHongbaodianProducts(int page, int size) {
		long amount = hongbaodianProductDao.countAmount();
		List<HongbaodianProduct> productList = hongbaodianProductDao.findAllHongbaodianProducts(page, size);
		ListPage listPage = new ListPage(productList, page, size, (int) amount);
		return listPage;
	}

	public HongbaodianProduct findHongbaodianProductByProductId(String productId) {
		return hongbaodianProductDao.findById(productId);
	}
}
