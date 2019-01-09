package com.anbang.qipai.hongbao.cqrs.q.dbo;

/**
 * 红包点订单
 * 
 * @author lsc
 *
 */
public class HongbaodianOrder {
	private String id;// 订单号
	private String status;// 订单状态
	private String desc;// 订单备注
	private String payerId;// 支付人
	private String payerName;// 支付人昵称
	private String payerOpenId;// 支付人openid
	private String receiverId;// 收货人
	private String receiverName;// 收货人昵称
	private String receiverOpenId;// 收货人openid
	private String productId;// 商品id
	private String produceName;// 商品名称
	private int productPrice;// 商品价格（红包点）
	private int rewardYushi;// 奖励玉石
	private int rewardLiquan;// 奖励礼券
	private long rewardVipTime;// 奖励会员时间
	private double rewardRMB;// 奖励现金
	private String reqIP;// 终端IP
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

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getPayerOpenId() {
		return payerOpenId;
	}

	public void setPayerOpenId(String payerOpenId) {
		this.payerOpenId = payerOpenId;
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProduceName() {
		return produceName;
	}

	public void setProduceName(String produceName) {
		this.produceName = produceName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
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

	public String getReqIP() {
		return reqIP;
	}

	public void setReqIP(String reqIP) {
		this.reqIP = reqIP;
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
