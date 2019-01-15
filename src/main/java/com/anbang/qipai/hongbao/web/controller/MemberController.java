package com.anbang.qipai.hongbao.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anbang.qipai.hongbao.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.hongbao.msg.service.MemberInvitationRecordMsgService;
import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;
import com.anbang.qipai.hongbao.plan.service.MemberInvitationRecordService;
import com.anbang.qipai.hongbao.remote.service.QipaiMembersRemoteService;
import com.anbang.qipai.hongbao.remote.vo.CommonRemoteVO;
import com.anbang.qipai.hongbao.web.vo.CommonVO;
import com.google.gson.Gson;
import com.highto.framework.web.page.ListPage;

@Controller
@RequestMapping("/member")
public class MemberController {

	private static String APPID = "wxb841e562b0100c95";

	private static String APPSECRET = "c411423c15fdd51bde4ec432732d26df";

	private Gson gson = new Gson();

	@Autowired
	protected HttpClient sslHttpClient;

	@Autowired
	private MemberAuthService memberAuthService;

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private MemberInvitationRecordService memberInvitationRecordService;

	@Autowired
	private MemberInvitationRecordMsgService memberInvitationRecordMsgService;

	@Autowired
	private QipaiMembersRemoteService qipaiMembersRemoteService;

	/**
	 * 邀请新玩家
	 */
	@RequestMapping(value = "/memberlogin")
	public String member_login(String code, String state) {
		Map map = null;
		try {
			map = takeOauth2AccessToken(code);
		} catch (Exception e) {
			return "redirect:http://scs.3cscy.com/majiang/u3D/html/xiazai.html";
		}
		Object errObj = map.get("errcode");
		if (errObj != null) {
			return "redirect:http://scs.3cscy.com/majiang/u3D/html/xiazai.html";
		}
		String accessToken = (String) map.get("access_token");
		String openid = (String) map.get("openid");
		Map infomap = null;
		try {
			infomap = takeUserInfo(accessToken, openid);
		} catch (Exception e) {
			return "redirect:http://scs.3cscy.com/majiang/u3D/html/xiazai.html";
		}
		String nickname = (String) infomap.get("nickname");
		String headimgurl = (String) infomap.get("headimgurl");
		String unionid = (String) infomap.get("unionid");
		int sex = Double.valueOf((double) infomap.get("sex")).intValue();
		// 玩家是否已经注册
		if (memberAuthQueryService.findThirdAuthorizationDbo("union.weixin", unionid) != null) {
			return "redirect:http://scs.3cscy.com/majiang/u3D/html/xiazai.html";
		}
		// 用户注册
		CommonRemoteVO rvo = qipaiMembersRemoteService.thirdauth_wechatidlogin(unionid, openid, nickname, headimgurl,
				sex);
		// 注册成功
		if (rvo.isSuccess()) {
			String json = gson.toJson(rvo.getData());
			Map data = gson.fromJson(json, Map.class);
			String memberId = memberAuthService.getMemberIdBySessionId((String) data.get("token"));
			// 是否受老玩家邀请且未受过其他人邀请
			if (!StringUtil.isBlank(memberId) && !StringUtil.isBlank(state)
					&& memberAuthQueryService.findByMemberId(state) != null
					&& memberInvitationRecordService.findMemberInvitationRecordByInvitationMemberId(memberId) == null) {
				// 邀请记录
				MemberInvitationRecord record = new MemberInvitationRecord();
				record.setMemberId(state);
				record.setInvitationMemberId(memberId);
				record.setCreateTime(System.currentTimeMillis());
				memberInvitationRecordService.insertMemberInvitationRecord(record);
				memberInvitationRecordMsgService.newRecord(record);
			}
		}
		return "redirect:http://scs.3cscy.com/majiang/u3D/html/xiazai.html";
	}

	@RequestMapping("/queryinvitation")
	@ResponseBody
	public CommonVO queryInvitationRecord(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ListPage listPage = memberInvitationRecordService.findMemberInvitationRecordByMemberId(page, size, memberId);
		Map data = new HashMap<>();
		data.put("listPage", listPage);
		vo.setData(data);
		return vo;
	}

	private Map takeOauth2AccessToken(String code) throws InterruptedException, ExecutionException, TimeoutException {
		Map data = new HashMap();
		try {
			// 拼接请求地址
			String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";// ?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
			Request request = sslHttpClient.POST(requestUrl).timeout(2, TimeUnit.SECONDS);
			request.param("appid", APPID);
			request.param("secret", APPSECRET);
			request.param("code", code);
			request.param("grant_type", "authorization_code");
			ContentResponse response = request.send();
			String content = response.getContentAsString();
			return gson.fromJson(content, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

	private Map takeUserInfo(String accessToken, String openid)
			throws InterruptedException, ExecutionException, TimeoutException {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid
				+ "&lang=zh_CN";
		String content = sslHttpClient.POST(url).timeout(2, TimeUnit.SECONDS).send().getContentAsString();
		return gson.fromJson(content, Map.class);
	}
}
