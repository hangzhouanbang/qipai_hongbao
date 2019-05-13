package com.anbang.qipai.hongbao.plan.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.plan.bean.ProductType;
import com.anbang.qipai.hongbao.plan.dao.ProductTypeDao;

@Component
public class MongodbProductTypeDao implements ProductTypeDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(ProductType type) {
		mongoTemplate.insert(type);
	}

	@Override
	public void removeByIds(String[] ids) {
		Object[] temIds = ids;
		Query query = new Query(Criteria.where("id").in(temIds));
		mongoTemplate.remove(query, ProductType.class);
	}

	@Override
	public void updateDescById(String id, String desc) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("desc", desc);
		mongoTemplate.updateFirst(query, update, ProductType.class);
	}

	@Override
	public List<ProductType> findAll() {
		return mongoTemplate.findAll(ProductType.class);
	}

	@Override
	public ProductType findById(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, ProductType.class);
	}

}
