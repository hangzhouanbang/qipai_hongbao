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
	private RewardType rewardType;// 奖励类型
	private double rewardNum;// 奖励数量
	private String reqIP;// 下订单IP
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

	public String getReqIP() {
		return reqIP;
	}

	public void setReqIP(String reqIP) {
		this.reqIP = reqIP;
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
