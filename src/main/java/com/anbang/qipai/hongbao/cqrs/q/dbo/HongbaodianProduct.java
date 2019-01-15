package com.anbang.qipai.hongbao.cqrs.q.dbo;

/**
 * 红包点商品
 * 
 * @author lsc
 *
 */
public class HongbaodianProduct {
	private String id;// 商品id
	private String name;// 商品名称
	private int price;// 商品价格（红包点）
	private RewardType rewardType;// 奖励类型
	private double rewardNum;// 奖励数量

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public RewardType getRewardType() {
		return rewardType;
	}

	public void setRewardType(RewardType rewardType) {
		this.rewardType = rewardType;
	}

	public double getRewardNum() {
		return rewardNum;
	}

	public void setRewardNum(double rewardNum) {
		this.rewardNum = rewardNum;
	}

}
