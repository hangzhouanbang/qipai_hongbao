package com.anbang.qipai.hongbao.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.q.dao.HongbaodianOrderDao;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianOrder;

@Component
public class MongodbHongbaodianOrderDao implements HongbaodianOrderDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(HongbaodianOrder order) {
		mongoTemplate.insert(order);
	}

	@Override
	public HongbaodianOrder findById(String orderId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(orderId));
		return mongoTemplate.findOne(query, HongbaodianOrder.class);
	}

	@Override
	public void updateStatus(String orderId, String status) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(orderId));
		Update update = new Update();
		update.set("status", status);
		mongoTemplate.updateFirst(query, update, HongbaodianOrder.class);
	}

	@Override
	public void updateFinishTime(String orderId, long finishTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(orderId));
		Update update = new Update();
		update.set("finishTime", finishTime);
		mongoTemplate.updateFirst(query, update, HongbaodianOrder.class);
	}

}
