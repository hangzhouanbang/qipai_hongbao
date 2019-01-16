package com.anbang.qipai.hongbao.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.q.dao.HongbaodianOrderDao;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianOrder;
import com.mongodb.BasicDBObject;

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

	@Override
	public double countTotalRewardNumByReceiverId(String receiverId) {
		Aggregation aggregation = Aggregation.newAggregation(HongbaodianOrder.class,
				Aggregation.match(Criteria.where("receiverId").is(receiverId).and("status").is("SUCCESS")),
				Aggregation.group().sum("rewardNum").as("rewardNum"));
		AggregationResults<BasicDBObject> result = mongoTemplate.aggregate(aggregation, HongbaodianOrder.class,
				BasicDBObject.class);
		List<BasicDBObject> list = result.getMappedResults();
		if (list.isEmpty()) {
			return 0;
		}
		BasicDBObject basicObj = list.get(0);
		return basicObj.getDouble("rewardNum") / 100;
	}

}
