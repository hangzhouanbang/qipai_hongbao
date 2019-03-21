package com.anbang.qipai.hongbao.cqrs.q.dbo;

import java.util.Map;

/**
 * 微信付款详情
 * 
 * @author lsc
 *
 */
public class PayInfo {
	private String id;
	private String orderId;// 订单id
	private double amount;// 金额
	private String desc;// 备注
	private String spbill_create_ip;// 终端IP
	private long createTime;// 发起时间
	private Map returnParams;// 微信返回参数
	private Map queryParams;// 微信查询返回参数
	private long finishTime;// 结束时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public Map getReturnParams() {
		return returnParams;
	}

	public void setReturnParams(Map returnParams) {
		this.returnParams = returnParams;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public Map getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map queryParams) {
		this.queryParams = queryParams;
	}

}
