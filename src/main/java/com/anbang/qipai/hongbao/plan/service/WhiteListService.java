package com.anbang.qipai.hongbao.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.plan.bean.BlackList;
import com.anbang.qipai.hongbao.plan.bean.WhiteList;
import com.anbang.qipai.hongbao.plan.dao.BlackListDao;
import com.anbang.qipai.hongbao.plan.dao.WhiteListDao;

@Service
public class WhiteListService {

	@Autowired
	private WhiteListDao whiteListDao;

	@Autowired
	private BlackListDao blackListDao;

	public void insert(WhiteList wl) {
		whiteListDao.insert(wl);
	}

	public void update(WhiteList wl) {
		whiteListDao.update(wl);
	}

	public void remove(String[] ids) {
		whiteListDao.remove(ids);
	}

	public WhiteList findByPlayerId(String playerId) {
		return whiteListDao.findByPlayerId(playerId);
	}

	public BlackList findBlackListByPlayerId(String playerId) {
		return blackListDao.findByPlayerId(playerId);
	}

	public BlackList findBlackListById(String id) {
		return blackListDao.findById(id);
	}

	public void saveBlackList(BlackList blackList) {
		blackListDao.save(blackList);
	}

	public void removeBlackListByIds(String[] ids) {
		blackListDao.removeByIds(ids);
	}

}
