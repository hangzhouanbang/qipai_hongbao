package com.anbang.qipai.hongbao.plan.dao;

import java.util.List;

import com.anbang.qipai.hongbao.plan.bean.HongbaodianProductRecord;

public interface HongbaodianProductRecordDao {

	void save(HongbaodianProductRecord record);

	HongbaodianProductRecord findById(String id);

	void updateStatusById(String id, String status);

	void updateDeliverTimeById(String id, long deliverTime);

	long countByMemberIdAndStatus(String memberId, String status);

	List<HongbaodianProductRecord> findByMemberIdAndStatus(int page, int size, String memberId, String status);
}
