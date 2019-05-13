package com.anbang.qipai.hongbao.msg.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.hongbao.cqrs.q.service.HongbaodianShopProductDboService;
import com.anbang.qipai.hongbao.msg.channel.sink.ProductTypeSink;
import com.anbang.qipai.hongbao.msg.msjobs.CommonMO;
import com.anbang.qipai.hongbao.plan.bean.ProductType;
import com.google.gson.Gson;

@EnableBinding(ProductTypeSink.class)
public class ProductTypeReceiver {

	@Autowired
	private HongbaodianShopProductDboService hongbaodianShopProductDboService;

	private Gson gson = new Gson();

	@StreamListener(ProductTypeSink.PRODUCTTYPE)
	public void recordMembers(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("add type".equals(msg)) {
			ProductType type = gson.fromJson(json, ProductType.class);
			hongbaodianShopProductDboService.saveProductType(type);
		}
		if ("remove type".equals(msg)) {
			String[] ids = gson.fromJson(json, String[].class);
			hongbaodianShopProductDboService.removeProductTypeByIds(ids);
		}
		if ("update type".equals(msg)) {
			ProductType type = gson.fromJson(json, ProductType.class);
			hongbaodianShopProductDboService.updateDescProductTypeById(type.getId(), type.getDesc());
		}
	}
}
