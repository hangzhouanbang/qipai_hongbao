package com.anbang.qipai.hongbao.cqrs.q.dao.mongodb;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.q.dao.PayInfoDao;
import com.anbang.qipai.hongbao.cqrs.q.dbo.PayInfo;

@Component
public class MongodbPayInfoDao implements PayInfoDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(PayInfo info) {
		mongoTemplate.insert(info);
	}

	@Override
	public PayInfo findByOrderId(String orderId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("orderId").is(orderId));
		return mongoTemplate.findOne(query, PayInfo.class);
	}

	@Override
	public void updateReturnParamsByOrderId(String orderId, Map returnParams) {
		Query query = new Query();
		query.addCriteria(Criteria.where("orderId").is(orderId));
		Update update = new Update();
		update.set("returnParams", returnParams);
		mongoTemplate.updateFirst(query, update, PayInfo.class);
	}

	@Override
	public void updateFinishTime(String orderId, long finishTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("orderId").is(orderId));
		Update update = new Update();
		update.set("finishTime", finishTime);
		mongoTemplate.updateFirst(query, update, PayInfo.class);
	}

	@Override
	public void updateQueryParamsByOrderId(String orderId, Map queryParams) {
		Query query = new Query();
		query.addCriteria(Criteria.where("orderId").is(orderId));
		Update update = new Update();
		update.set("queryParams", queryParams);
		mongoTemplate.updateFirst(query, update, PayInfo.class);
	}

}
