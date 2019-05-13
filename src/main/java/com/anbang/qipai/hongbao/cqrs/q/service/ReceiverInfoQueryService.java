package com.anbang.qipai.hongbao.cqrs.q.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.cqrs.q.dao.ReceiverInfoDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dbo.ReceiverInfoDbo;

/**
 * @Author: 吴硕涵
 * @Date: 2018/12/25 2:11 PM
 * @Version 1.0
 */
@Service
public class ReceiverInfoQueryService {

	@Autowired
	private ReceiverInfoDboDao receiverInfoDboDao;

	public ReceiverInfoDbo findReceiverByMemberId(String memberId) {
		return receiverInfoDboDao.findByMemberId(memberId);
	}

	public void addReceiverInfo(ReceiverInfoDbo dbo) {
		receiverInfoDboDao.add(dbo);
	}

	public void save(ReceiverInfoDbo dbo) {
		receiverInfoDboDao.save(dbo);
	}
}
