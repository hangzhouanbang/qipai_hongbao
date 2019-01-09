package com.anbang.qipai.hongbao.remote.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anbang.qipai.hongbao.remote.vo.CommonRemoteVO;

/**
 * 会员中心远程服务
 * 
 * @author lsc
 *
 */
@FeignClient("qipai-members")
public interface QipaiMembersRemoteService {

	@RequestMapping(value = "/auth/trytoken")
	public CommonRemoteVO auth_trytoken(@RequestParam(value = "token") String token);

}
