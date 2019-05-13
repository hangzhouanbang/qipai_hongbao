package com.anbang.qipai.hongbao.plan.dao.mongodb;

import java.util.List;

import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.plan.bean.HongbaodianProductRecord;
import com.anbang.qipai.hongbao.plan.dao.HongbaodianProductRecordDao;

@Component
public class MongodbHongbaodianProductRecordDao implements HongbaodianProductRecordDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(HongbaodianProductRecord record) {
		mongoTemplate.insert(record);
	}

	@Override
	public HongbaodianProductRecord findById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, HongbaodianProductRecord.class);
	}

	@Override
	public void updateStatusById(String id, String status) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("status", status);
		mongoTemplate.updateFirst(query, update, HongbaodianProductRecord.class);
	}

	@Override
	public long countByMemberIdAndStatus(String memberId, String status) {
		Query query = new Query();
		if (StringUtil.isNotBlank(memberId)) {
			query.addCriteria(Criteria.where("memberId").is(memberId));
		}
		if (StringUtil.isNotBlank(status)) {
			query.addCriteria(Criteria.where("status").is(status));
		}
		return mongoTemplate.count(query, HongbaodianProductRecord.class);
	}

	@Override
	public List<HongbaodianProductRecord> findByMemberIdAndStatus(int page, int size, String memberId, String status) {
		Query query = new Query();
		if (StringUtil.isNotBlank(memberId)) {
			query.addCriteria(Criteria.where("memberId").is(memberId));
		}
		if (StringUtil.isNotBlank(status)) {
			query.addCriteria(Criteria.where("status").is(status));
		}
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, HongbaodianProductRecord.class);
	}

	@Override
	public void updateDeliverTimeById(String id, long deliverTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("deliverTime", deliverTime);
		mongoTemplate.updateFirst(query, update, HongbaodianProductRecordDao.class);
	}

}
