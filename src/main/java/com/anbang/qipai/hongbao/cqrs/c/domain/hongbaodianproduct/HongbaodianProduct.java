package com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianproduct;

/**
 * 礼券商品
 * 
 * @author lsc
 *
 */
public class HongbaodianProduct {
	private String id;
	private int remain;// 库存

	public void buy(int amount) throws ProductNotEnoughException {
		if (remain < amount) {
			throw new ProductNotEnoughException();
		}
		remain -= amount;
	}

	public void add(int amount) {
		remain += amount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRemain() {
		return remain;
	}

	public void setRemain(int remain) {
		this.remain = remain;
	}
}
