package com.anbang.qipai.hongbao.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.plan.bean.WhiteList;
import com.anbang.qipai.hongbao.plan.dao.WhiteListDao;

@Service
public class WhiteListService {

	@Autowired
	private WhiteListDao whiteListDao;

	public void insert(WhiteList wl) {
		whiteListDao.insert(wl);
	}

	public void update(WhiteList wl) {
		whiteListDao.update(wl);
	}

	public void remove(String[] ids) {
		whiteListDao.remove(ids);
	}

	public WhiteList findByPlayerIdAndLoginIP(String playerId, String loginIP) {
		return whiteListDao.findByPlayerIdAndLoginIP(playerId, loginIP);
	}
}
