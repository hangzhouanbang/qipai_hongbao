package com.anbang.qipai.hongbao.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.plan.bean.IPVerifyDbo;
import com.anbang.qipai.hongbao.plan.dao.IPVerifyDboDao;

@Component
public class MongodbIPVerifyDboDao implements IPVerifyDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(IPVerifyDbo dbo) {
		mongoTemplate.insert(dbo);
	}

	@Override
	public long countByTime(Long startTime, Long endTime, String memberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		if (startTime != null || endTime != null) {
			Criteria criteria = Criteria.where("createTime");
			if (startTime != null) {
				criteria = criteria.gt(startTime);
			}
			if (endTime != null) {
				criteria = criteria.lt(endTime);
			}
			query.addCriteria(criteria);
		}
		return mongoTemplate.count(query, IPVerifyDbo.class);
	}

}
