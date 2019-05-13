package com.anbang.qipai.hongbao.cqrs.q.dao;

import com.anbang.qipai.hongbao.cqrs.q.dbo.ReceiverInfoDbo;

/**
 * @Author: 吴硕涵
 * @Date: 2018/12/25 2:04 PM
 * @Version 1.0
 */
public interface ReceiverInfoDboDao {
	void save(ReceiverInfoDbo dbo);

	void add(ReceiverInfoDbo dbo);

	ReceiverInfoDbo findByMemberId(String memebrId);
}
