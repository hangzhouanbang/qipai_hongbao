package com.anbang.qipai.hongbao.cqrs.q.dao;

import com.anbang.qipai.hongbao.cqrs.q.dbo.AuthorizationDbo;

public interface AuthorizationDboDao {

	AuthorizationDbo find(boolean thirdAuth, String publisher, String uuid);

	void save(AuthorizationDbo authDbo);

	AuthorizationDbo findAuthorizationDboByMemberIdAndPublisher(boolean thirdAuth, String memberId, String publisher);

}
