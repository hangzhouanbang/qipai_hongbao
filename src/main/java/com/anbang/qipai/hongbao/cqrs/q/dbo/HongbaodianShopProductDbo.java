package com.anbang.qipai.hongbao.cqrs.q.dbo;

/**
 * 红包点商城商品
 * 
 * @author lsc
 *
 */
public class HongbaodianShopProductDbo {

	private String id;
	private String desc;// 奖品备注
	private RewardType rewardType;// 奖品类型
	private String type;// 奖品类别
	private int rewardNum;// 奖品数量
	private int remain;// 库存数量
	private String iconurl;// ICON图
	private int price;// 红包点兑换数量

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public RewardType getRewardType() {
		return rewardType;
	}

	public void setRewardType(RewardType rewardType) {
		this.rewardType = rewardType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRewardNum() {
		return rewardNum;
	}

	public void setRewardNum(int rewardNum) {
		this.rewardNum = rewardNum;
	}

	public int getRemain() {
		return remain;
	}

	public void setRemain(int remain) {
		this.remain = remain;
	}

	public String getIconurl() {
		return iconurl;
	}

	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
