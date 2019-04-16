package com.anbang.qipai.hongbao.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anbang.qipai.hongbao.conf.GongZhongHaoConfig;
import com.anbang.qipai.hongbao.conf.MemberInvitationRecordState;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.hongbao.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.hongbao.msg.service.MemberInvitationRecordMsgService;
import com.anbang.qipai.hongbao.plan.bean.MemberInvitationRecord;
import com.anbang.qipai.hongbao.plan.service.MemberInvitationRecordService;
import com.anbang.qipai.hongbao.remote.service.QipaiMembersRemoteService;
import com.anbang.qipai.hongbao.remote.vo.CommonRemoteVO;
import com.anbang.qipai.hongbao.util.IPUtil;
import com.anbang.qipai.hongbao.web.vo.CommonVO;
import com.google.gson.Gson;
import com.highto.framework.web.page.ListPage;

@Controller
@RequestMapping("/member")
public class MemberController {

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
	 * 微信引导
	 */
	@RequestMapping(value = "/share")
	@ResponseBody
	public CommonVO share(String token) {
		CommonVO vo = new CommonVO();
		if (token == null) {
			token = "";
		}
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		String URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
		URL += "?appid=" + GongZhongHaoConfig.APPID;
		String REDIRECT_URI = null;
		try {
			REDIRECT_URI = URLEncoder.encode("http://3cs.3cscy.com/hongbao/member/memberlogin", "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		URL += "&redirect_uri=" + REDIRECT_URI;
		URL += "&response_type=code";
		URL += "&scope=snsapi_userinfo";
		URL += "&state=" + memberId;
		URL += "#wechat_redirect";
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("URL", URL);
		return vo;
	}

	/**
	 * 邀请新玩家
	 */
	@RequestMapping(value = "/memberlogin")
	public String member_login(HttpServletRequest request, String code, String state) {
		if (StringUtil.isBlank(state)) {
			return "redirect:http://3cs.3cscy.com/majiang/u3D/html/red_packet.html";
		}
		Map map = null;
		try {
			map = takeOauth2AccessToken(code);
		} catch (Exception e) {
			return "redirect:http://3cs.3cscy.com/majiang/u3D/html/red_packet.html";
		}
		Object errObj = map.get("errcode");
		if (errObj != null) {
			return "redirect:http://3cs.3cscy.com/majiang/u3D/html/red_packet.html";
		}
		String accessToken = (String) map.get("access_token");
		String openid = (String) map.get("openid");
		Map infomap = null;
		try {
			infomap = takeUserInfo(accessToken, openid);
		} catch (Exception e) {
			return "redirect:http://3cs.3cscy.com/majiang/u3D/html/red_packet.html";
		}
		String nickname = (String) infomap.get("nickname");
		String headimgurl = (String) infomap.get("headimgurl");
		String unionid = (String) infomap.get("unionid");
		String repIP = IPUtil.getRealIp(request);
		int sex = Double.valueOf((double) infomap.get("sex")).intValue();
		// 玩家是否已经注册
		if (memberAuthQueryService.findThirdAuthorizationDbo("union.weixin", unionid) != null) {
			return "redirect:http://3cs.3cscy.com/majiang/u3D/html/red_packet.html";
		}
		if (memberAuthQueryService.findThirdAuthorizationDbo("open.weixin.app.qipai", openid) != null) {
			return "redirect:http://3cs.3cscy.com/majiang/u3D/html/red_packet.html";
		}
		if (memberAuthQueryService.findThirdAuthorizationDbo("open.weixin.gongzhonghao.qipai", openid) != null) {
			return "redirect:http://3cs.3cscy.com/majiang/u3D/html/red_packet.html";
		}
		// 用户注册
		CommonRemoteVO rvo = qipaiMembersRemoteService.thirdauth_wechatidlogin_gongzhonghao(unionid, openid, nickname,
				headimgurl, sex, repIP);
		// 注册成功
		if (rvo.isSuccess()) {
			String json = gson.toJson(rvo.getData());
			Map data = gson.fromJson(json, Map.class);
			String memberId = memberAuthService.getMemberIdBySessionId((String) data.get("token"));
			// 是否受老玩家邀请且未受过其他人邀请
			if (!StringUtil.isBlank(memberId) && !StringUtil.isBlank(state)
					&& memberInvitationRecordService.findMemberInvitationRecordByInvitationMemberId(memberId) == null) {
				MemberDbo member = memberAuthQueryService.findByMemberId(state);
				if (member == null) {
					return "redirect:http://3cs.3cscy.com/majiang/u3D/html/red_packet.html";
				}
				MemberDbo invitateMember = memberAuthQueryService.findByMemberId(memberId);
				// 邀请记录
				MemberInvitationRecord record = new MemberInvitationRecord();
				record.setMemberId(state);
				record.setNickname(member.getNickname());
				record.setInvitationMemberId(memberId);
				record.setState(MemberInvitationRecordState.PROCESSING);
				record.setCause("未登陆");
				if (invitateMember != null) {
					// 如果未收到新玩家消息则不填昵称
					record.setInvitationMemberNickname(invitateMember.getNickname());
				}
				record.setCreateTime(System.currentTimeMillis());
				memberInvitationRecordService.insertMemberInvitationRecord(record);
				memberInvitationRecordMsgService.newRecord(record);
			}
			return "redirect:http://3cs.3cscy.com/majiang/u3D/html/red_packet.html";
		}
		return "redirect:http://3cs.3cscy.com/majiang/u3D/html/red_packet.html";
	}

	/**
	 * 测试邀请
	 */
	@RequestMapping(value = "/memberbind")
	public String member_bind(String memberId, String inviteMemberId) {
		// 是否受老玩家邀请且未受过其他人邀请
		if (!StringUtil.isBlank(inviteMemberId) && !StringUtil.isBlank(memberId) && memberInvitationRecordService
				.findMemberInvitationRecordByInvitationMemberId(inviteMemberId) == null) {
			MemberDbo member = memberAuthQueryService.findByMemberId(memberId);
			if (member == null) {
				return "redirect:http://3cs.3cscy.com/majiang/u3D/html/red_packet.html";
			}
			MemberDbo invitateMember = memberAuthQueryService.findByMemberId(inviteMemberId);
			// 邀请记录
			MemberInvitationRecord record = new MemberInvitationRecord();
			record.setMemberId(memberId);
			record.setNickname(member.getNickname());
			record.setInvitationMemberId(inviteMemberId);
			if (invitateMember != null) {
				// 如果未收到新玩家消息则不填昵称
				record.setInvitationMemberNickname(invitateMember.getNickname());
			}
			record.setState(MemberInvitationRecordState.PROCESSING);
			record.setCreateTime(System.currentTimeMillis());
			memberInvitationRecordService.insertMemberInvitationRecord(record);
			memberInvitationRecordMsgService.newRecord(record);
			return "redirect:http://3cs.3cscy.com/majiang/u3D/html/red_packet.html";
		}
		return "redirect:http://3cs.3cscy.com/majiang/u3D/html/red_packet.html";
	}

	/**
	 * 查询邀请记录
	 */
	@RequestMapping("/queryinvitation")
	@ResponseBody
	public CommonVO queryInvitationRecord(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "300") int size, String token) {
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
			request.param("appid", GongZhongHaoConfig.APPID);
			request.param("secret", GongZhongHaoConfig.APPSECRET);
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
