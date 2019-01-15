package com.anbang.qipai.hongbao.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.q.dao.MemberHongbaodianAccountDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianAccountDbo;

@Component
public class MongodbMemberHongbaodianAccountDboDao implements MemberHongbaodianAccountDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(MemberHongbaodianAccountDbo dbo) {
		mongoTemplate.insert(dbo);
	}

	@Override
	public void updateBalance(String accountId, int balance) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(accountId));
		Update update = new Update();
		update.set("balance", balance);
		mongoTemplate.updateFirst(query, update, MemberHongbaodianAccountDbo.class);
	}

	@Override
	public void remove(String accountId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(accountId));
		mongoTemplate.remove(query, MemberHongbaodianAccountDbo.class);
	}

	@Override
	public MemberHongbaodianAccountDbo findByMemberId(String memberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		return mongoTemplate.findOne(query, MemberHongbaodianAccountDbo.class);
	}

}
