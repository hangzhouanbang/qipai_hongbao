package com.anbang.qipai.hongbao.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.cqrs.q.dao.MemberHongbaodianRecordDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianRecordDbo;

@Component
public class MongodbMemberHongbaodianRecordDboDao implements MemberHongbaodianRecordDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(MemberHongbaodianRecordDbo record) {
		mongoTemplate.insert(record);
	}

	@Override
	public void remove(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoTemplate.remove(query, MemberHongbaodianRecordDbo.class);
	}

	@Override
	public long countAmountByMemberId(String memberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		return mongoTemplate.count(query, MemberHongbaodianRecordDbo.class);
	}

	@Override
	public List<MemberHongbaodianRecordDbo> findByMemberId(int page, int size, String memberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.skip((page - 1) * size);
		query.limit(size);
		query.with(new Sort(Direction.DESC, "accountingTime"));
		return mongoTemplate.find(query, MemberHongbaodianRecordDbo.class);
	}

	@Override
	public List<MemberHongbaodianRecordDbo> findByMemberId(String memberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.with(new Sort(Direction.DESC, "accountingTime"));
		return mongoTemplate.find(query, MemberHongbaodianRecordDbo.class);
	}

	@Override
	public List<MemberHongbaodianRecordDbo> findByMemberIdAndSummary(String memberId, String summary) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.addCriteria(Criteria.where("summaty.text").regex(summary));
		query.with(new Sort(Direction.DESC, "accountingTime"));
		return mongoTemplate.find(query, MemberHongbaodianRecordDbo.class);
	}

}
