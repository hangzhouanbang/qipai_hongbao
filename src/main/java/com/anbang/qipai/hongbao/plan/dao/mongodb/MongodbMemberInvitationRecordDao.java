package com.anbang.qipai.hongbao.plan.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;
import com.anbang.qipai.hongbao.plan.dao.MemberInvitationRecordDao;

@Component
public class MongodbMemberInvitationRecordDao implements MemberInvitationRecordDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(MemberInvitationRecord record) {
		mongoTemplate.insert(record);
	}

	@Override
	public MemberInvitationRecord findByMemberIdAndInvitationMemberId(String memberId, String invitationMemberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.addCriteria(Criteria.where("invitationMemberId").is(invitationMemberId));
		return mongoTemplate.findOne(query, MemberInvitationRecord.class);
	}

	@Override
	public MemberInvitationRecord findByInvitationMemberId(String invitationMemberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("invitationMemberId").is(invitationMemberId));
		return mongoTemplate.findOne(query, MemberInvitationRecord.class);
	}

	@Override
	public List<MemberInvitationRecord> findByMemberId(int page, int size, String memberId, String state) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.addCriteria(Criteria.where("state").is(state));
		query.skip((page - 1) * size);
		query.limit(size);
		query.with(new Sort(Direction.DESC, "createTime"));
		return mongoTemplate.find(query, MemberInvitationRecord.class);
	}

	@Override
	public long countByMemberId(String memberId, String state) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.addCriteria(Criteria.where("state").is(state));
		return mongoTemplate.count(query, MemberInvitationRecord.class);
	}

	@Override
	public void updateState(String id, String state) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("state", state);
		mongoTemplate.updateFirst(query, update, MemberInvitationRecord.class);
	}

	@Override
	public MemberInvitationRecord findById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, MemberInvitationRecord.class);
	}

}
