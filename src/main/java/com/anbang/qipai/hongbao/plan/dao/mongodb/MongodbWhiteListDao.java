package com.anbang.qipai.hongbao.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.plan.bean.WhiteList;
import com.anbang.qipai.hongbao.plan.dao.WhiteListDao;

@Component
public class MongodbWhiteListDao implements WhiteListDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(WhiteList wl) {
		mongoTemplate.insert(wl);
	}

	@Override
	public void update(WhiteList wl) {
		Query query = new Query(Criteria.where("id").is(wl.getId()));
		Update update = new Update();
		update.set("playerId", wl.getPlayerId());
		update.set("addTime", wl.getAddTime());
		update.set("remark", wl.getRemark());
		update.set("operator", wl.getOperator());
		mongoTemplate.updateFirst(query, update, WhiteList.class);
	}

	@Override
	public void remove(String[] ids) {
		Object[] tempIds = ids;
		Query query = new Query(Criteria.where("id").in(tempIds));
		mongoTemplate.remove(query, WhiteList.class);
	}

	@Override
	public WhiteList findByPlayerId(String playerId) {
		Query query = new Query(Criteria.where("playerId").is(playerId));
		return mongoTemplate.findOne(query, WhiteList.class);
	}

}
