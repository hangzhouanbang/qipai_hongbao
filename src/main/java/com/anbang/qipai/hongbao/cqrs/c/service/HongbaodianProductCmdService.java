package com.anbang.qipai.hongbao.cqrs.c.service;

public interface HongbaodianProductCmdService {

	Integer buyProduct(String id, Integer amount) throws Exception;

	Integer addProduct(String id, Integer amount) throws Exception;

	void clear() throws Exception;

	void fill(String id, Integer remain) throws Exception;
}
