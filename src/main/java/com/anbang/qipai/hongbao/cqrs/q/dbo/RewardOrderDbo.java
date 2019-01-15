package com.anbang.qipai.hongbao.cqrs.q.dbo;

/**
 * 返现订单
 * 
 * @author lsc
 *
 */
public class RewardOrderDbo {
	private String id;// 订单号
	private String status;// 订单状态
	private String desc;// 订单备注
	private String receiverId;// 收货人
	private String receiverName;// 收货人昵称
	private String receiverOpenId;// 收货人openid
	private double rewardRMB;// 奖励现金
	private String spbill_create_ip;// 终端IP
	private long createTime;// 创建时间
	private long finishTime;// 完成时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverOpenId() {
		return receiverOpenId;
	}

	public void setReceiverOpenId(String receiverOpenId) {
		this.receiverOpenId = receiverOpenId;
	}

	public double getRewardRMB() {
		return rewardRMB;
	}

	public void setRewardRMB(double rewardRMB) {
		this.rewardRMB = rewardRMB;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

}
