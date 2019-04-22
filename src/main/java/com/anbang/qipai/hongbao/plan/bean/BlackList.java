package com.anbang.qipai.hongbao.plan.bean;

public class BlackList {
	private String id;
	private String playerId;// 玩家id
	private long addTime;// 添加时间
	private String remark;// 备注
	private String reqIP;// 注册ip

	public String getReqIP() {
		return reqIP;
	}

	public void setReqIP(String reqIP) {
		this.reqIP = reqIP;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
