package com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianproduct;

import java.util.HashMap;
import java.util.Map;

/**
 * 礼券商品管理
 * 
 * @author lsc
 *
 */
public class HongbaodianProductManager {

	private Map<String, HongbaodianProduct> idProductMap = new HashMap<>();

	/**
	 * 购买
	 */
	public int buyProduct(String id, int amount) throws ProductNotFoundException, ProductNotEnoughException {
		if (!idProductMap.containsKey(id)) {
			throw new ProductNotFoundException();
		}
		HongbaodianProduct product = idProductMap.get(id);
		product.buy(amount);
		return product.getRemain();
	}

	/**
	 * 添加
	 */
	public int addProduct(String id, int amount) throws ProductNotFoundException {
		if (!idProductMap.containsKey(id)) {
			throw new ProductNotFoundException();
		}
		HongbaodianProduct product = idProductMap.get(id);
		product.add(amount);
		return product.getRemain();
	}

	/**
	 * 清空
	 */
	public void clear() {
		idProductMap.clear();
	}

	/**
	 * 填充
	 */
	public void fill(String id, int remain) {
		HongbaodianProduct product = new HongbaodianProduct();
		product.setId(id);
		product.setRemain(remain);
		idProductMap.put(product.getId(), product);
	}

	public Map<String, HongbaodianProduct> getIdProductMap() {
		return idProductMap;
	}

	public void setIdProductMap(Map<String, HongbaodianProduct> idProductMap) {
		this.idProductMap = idProductMap;
	}
}
