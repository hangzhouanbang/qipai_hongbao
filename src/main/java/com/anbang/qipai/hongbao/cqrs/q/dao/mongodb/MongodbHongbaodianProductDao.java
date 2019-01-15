package com.anbang.qipai.hongbao.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.q.dao.HongbaodianProductDao;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianProduct;

@Component
public class MongodbHongbaodianProductDao implements HongbaodianProductDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(HongbaodianProduct product) {
		mongoTemplate.insert(product);
	}

	@Override
	public void update(HongbaodianProduct product) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(product.getId()));
		Update update = new Update();
		update.set("name", product.getName());
		update.set("price", product.getPrice());
		update.set("rewardRMB", product.getRewardRMB());
		mongoTemplate.updateFirst(query, update, HongbaodianProduct.class);
	}

	@Override
	public void remove(String[] productIds) {
		Object[] ids = productIds;
		Query query = new Query();
		query.addCriteria(Criteria.where("id").in(ids));
		mongoTemplate.remove(query, HongbaodianProduct.class);
	}

	@Override
	public long countAmount() {
		Query query = new Query();
		return mongoTemplate.count(query, HongbaodianProduct.class);
	}

	@Override
	public List<HongbaodianProduct> findAllHongbaodianProducts(int page, int size) {
		Query query = new Query();
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, HongbaodianProduct.class);
	}

	@Override
	public HongbaodianProduct findById(String productId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(productId));
		return mongoTemplate.findOne(query, HongbaodianProduct.class);
	}

}
