package com.anbang.qipai.hongbao.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.plan.bean.BlackList;
import com.anbang.qipai.hongbao.plan.dao.BlackListDao;

@Component
public class MongodbBlackListDao implements BlackListDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(BlackList blackList) {
		mongoTemplate.insert(blackList);
	}

	@Override
	public BlackList findByPlayerId(String playerId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(playerId));
		return mongoTemplate.findOne(query, BlackList.class);
	}

	@Override
	public void removeByIds(String[] ids) {
		Object[] blackListIds = ids;
		Query query = new Query();
		query.addCriteria(Criteria.where("id").in(blackListIds));
		mongoTemplate.remove(query, BlackList.class);
	}

	@Override
	public BlackList findById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, BlackList.class);
	}

}
