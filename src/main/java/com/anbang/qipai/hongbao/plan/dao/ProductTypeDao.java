package com.anbang.qipai.hongbao.plan.dao;

import java.util.List;

import com.anbang.qipai.hongbao.plan.bean.ProductType;

public interface ProductTypeDao {

	void save(ProductType type);

	void removeByIds(String[] ids);

	void updateDescById(String id, String desc);

	List<ProductType> findAll();

	ProductType findById(String id);
}
