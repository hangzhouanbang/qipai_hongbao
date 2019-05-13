package com.anbang.qipai.hongbao.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.q.dao.HongbaodianShopProductDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dao.mongodb.repository.HongbaodianShopProductDboRepository;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianShopProductDbo;

@Component
public class MongodbScoreShopProductDboDao implements HongbaodianShopProductDboDao {

	@Autowired
	private HongbaodianShopProductDboRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(HongbaodianShopProductDbo dbo) {
		repository.save(dbo);
	}

	@Override
	public void update(HongbaodianShopProductDbo dbo) {
		repository.save(dbo);

	}

	@Override
	public void removeByIds(String[] ids) {
		Object[] temIds = ids;
		Query query = new Query();
		query.addCriteria(Criteria.where("id").in(temIds));
		mongoTemplate.remove(query, HongbaodianShopProductDbo.class);
	}

	@Override
	public HongbaodianShopProductDbo findById(String id) {
		return repository.findOne(id);
	}

	@Override
	public List<HongbaodianShopProductDbo> findByType(int page, int size, String type) {
		Query query = new Query();
		query.addCriteria(Criteria.where("type").is(type));
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, HongbaodianShopProductDbo.class);
	}

	@Override
	public void removeAll() {
		repository.deleteAll();
	}

	@Override
	public void saveAll(List<HongbaodianShopProductDbo> products) {
		mongoTemplate.insert(products, HongbaodianShopProductDbo.class);
	}

	@Override
	public void incRemainById(String id, int amount) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.inc("remain", amount);
		mongoTemplate.updateFirst(query, update, HongbaodianShopProductDbo.class);
	}

	@Override
	public long countByType(String type) {
		Query query = new Query();
		query.addCriteria(Criteria.where("type").is(type));
		return mongoTemplate.count(query, HongbaodianShopProductDbo.class);
	}

	@Override
	public void updateRemainById(String id, int remain) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("remain", remain);
		mongoTemplate.updateFirst(query, update, HongbaodianShopProductDbo.class);
	}

}
