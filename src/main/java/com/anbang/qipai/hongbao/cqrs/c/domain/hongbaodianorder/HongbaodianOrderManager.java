package com.anbang.qipai.hongbao.cqrs.c.domain.hongbaodianorder;

import java.util.HashMap;
import java.util.Map;

public class HongbaodianOrderManager {
	private static int size = 1000;
	private Map<String, Integer> heighOrderIdMap = new HashMap<>();
	private Map<String, Integer> lowOrderIdMap = new HashMap<>();

	/**
	 * 创建订单
	 */
	public String createOrder(String orderId) throws OrderHasAlreadyExistenceException {
		if (lowOrderIdMap.containsKey(orderId) || heighOrderIdMap.containsKey(orderId)) {
			throw new OrderHasAlreadyExistenceException();
		}
		// 将订单号保存在两个map缓存
		if (size < 500) {
			lowOrderIdMap.put(orderId, size);
		} else if (size < 1000) {
			heighOrderIdMap.clear();
			heighOrderIdMap.put(orderId, size);
		} else {
			size = 0;
			lowOrderIdMap.clear();
			lowOrderIdMap.put(orderId, size);
		}
		size++;
		return orderId;
	}

	/**
	 * 完成订单
	 */
	public String finishOrder(String orderId) throws OrderNotFoundException {
		if (!lowOrderIdMap.containsKey(orderId) && !heighOrderIdMap.containsKey(orderId)) {
			throw new OrderNotFoundException();
		}
		lowOrderIdMap.remove(orderId);
		heighOrderIdMap.remove(orderId);
		return orderId;
	}

	public static int getSize() {
		return size;
	}

	public static void setSize(int size) {
		HongbaodianOrderManager.size = size;
	}

	public Map<String, Integer> getHeighOrderIdMap() {
		return heighOrderIdMap;
	}

	public void setHeighOrderIdMap(Map<String, Integer> heighOrderIdMap) {
		this.heighOrderIdMap = heighOrderIdMap;
	}

	public Map<String, Integer> getLowOrderIdMap() {
		return lowOrderIdMap;
	}

	public void setLowOrderIdMap(Map<String, Integer> lowOrderIdMap) {
		this.lowOrderIdMap = lowOrderIdMap;
	}

}
