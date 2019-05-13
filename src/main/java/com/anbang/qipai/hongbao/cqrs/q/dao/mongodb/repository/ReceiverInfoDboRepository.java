package com.anbang.qipai.hongbao.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.hongbao.cqrs.q.dbo.ReceiverInfoDbo;

public interface ReceiverInfoDboRepository extends MongoRepository<ReceiverInfoDbo, String> {

}
