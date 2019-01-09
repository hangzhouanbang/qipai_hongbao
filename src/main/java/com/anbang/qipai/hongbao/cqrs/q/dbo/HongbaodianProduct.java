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
	private int rewardYushi;// 奖励玉石
	private int rewardLiquan;// 奖励礼券
	private long rewardVipTime;// 奖励会员时间
	private double rewardRMB;// 奖励现金

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

	public int getRewardYushi() {
		return rewardYushi;
	}

	public void setRewardYushi(int rewardYushi) {
		this.rewardYushi = rewardYushi;
	}

	public int getRewardLiquan() {
		return rewardLiquan;
	}

	public void setRewardLiquan(int rewardLiquan) {
		this.rewardLiquan = rewardLiquan;
	}

	public long getRewardVipTime() {
		return rewardVipTime;
	}

	public void setRewardVipTime(long rewardVipTime) {
		this.rewardVipTime = rewardVipTime;
	}

	public double getRewardRMB() {
		return rewardRMB;
	}

	public void setRewardRMB(double rewardRMB) {
		this.rewardRMB = rewardRMB;
	}

}
