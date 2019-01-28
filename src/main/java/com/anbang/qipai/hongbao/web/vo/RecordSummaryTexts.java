package com.anbang.qipai.hongbao.web.vo;

public enum RecordSummaryTexts {
	新玩家注册, 购买会员卡赠送, 管理员赠送金币, 管理员赠送积分, 邮件奖励, 任务奖励, 创建房间, 加入房间, 离开房间, 绑定邀请码, 发送俏皮话;

	public static String getSummaryText(String text) {
		if (text == null) {
			return null;
		}
		if (text.contains("buy")) {
			text = text.replace("buy", "兑换");
		}
		return text;
	}
}
