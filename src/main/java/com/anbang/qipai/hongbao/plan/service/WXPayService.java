package com.anbang.qipai.hongbao.plan.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.conf.WXPayConfig;
import com.anbang.qipai.hongbao.cqrs.q.dbo.HongbaodianOrder;
import com.anbang.qipai.hongbao.cqrs.q.dbo.RewardOrderDbo;
import com.anbang.qipai.hongbao.util.MD5Util;
import com.anbang.qipai.hongbao.util.XMLObjectConvertUtil;

@Service
public class WXPayService {
	private static int socketTimeout = 10000;// 连接超时时间，默认10秒
	private static int connectTimeout = 30000;// 传输超时时间，默认30秒
	private RequestConfig requestConfig;// 请求器的配置
	private CloseableHttpClient httpClient;// HTTP请求器

	public Map<String, String> rewardAgent(HongbaodianOrder order) throws Exception {
		String orderInfo = createRewardInfo(order);
		SortedMap<String, String> responseMap = reward(orderInfo);
		String queryInfo = createQueryRewardInfo(order);
		responseMap = queryReward(queryInfo);
		return responseMap;
	}

	public Map<String, String> rewardAgent(RewardOrderDbo order) throws Exception {
		String orderInfo = createRewardInfo(order);
		SortedMap<String, String> responseMap = reward(orderInfo);
		String queryInfo = createQueryRewardInfo(order);
		responseMap = queryReward(queryInfo);
		return responseMap;
	}

	private SortedMap<String, String> reward(String orderInfo) throws Exception {
		String result = null;
		// 加载证书
		try {
			initCert();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers");
		// 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
		StringEntity postEntity = new StringEntity(orderInfo, "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setEntity(postEntity);
		// 根据默认超时限制初始化requestConfig
		requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout)
				.build();
		// 设置请求器的配置
		httpPost.setConfig(requestConfig);
		try {
			HttpResponse response = null;
			try {
				response = httpClient.execute(httpPost);
			} catch (IOException e) {
				e.printStackTrace();
			}
			HttpEntity entity = response.getEntity();
			try {
				result = EntityUtils.toString(entity, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			httpPost.abort();
		}
		// 接受数据
		SortedMap<String, String> responseMap = XMLObjectConvertUtil.praseXMLToMap(result);
		return responseMap;
	}

	private String createRewardInfo(HongbaodianOrder order) {
		// 创建可排序的Map集合
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		// 应用id
		parameters.put("mch_appid", WXPayConfig.APPID);
		// 商户号
		parameters.put("mchid", WXPayConfig.MCH_ID);
		// 随机字符串
		parameters.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
		// 商户订单号
		parameters.put("partner_trade_no", order.getId());
		// 用户openid
		parameters.put("openid", order.getReceiverOpenId());
		// 校验用户姓名选项
		parameters.put("check_name", "NO_CHECK");
		// 收款用户姓名
		// parameters.put("re_user_name", order.getRe_user_name());
		// 金额
		parameters.put("amount", Integer.toString((int) (order.getRewardNum() * 100)));
		// 企业付款备注
		parameters.put("desc", order.getDesc());
		// 服务端实际ip
		parameters.put("spbill_create_ip", order.getSpbill_create_ip());
		parameters.put("sign", createSign(parameters));
		String xml = XMLObjectConvertUtil.praseMapToXML(parameters);
		return xml;
	}

	private String createRewardInfo(RewardOrderDbo order) {
		// 创建可排序的Map集合
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		// 应用id
		parameters.put("mch_appid", WXPayConfig.APPID);
		// 商户号
		parameters.put("mchid", WXPayConfig.MCH_ID);
		// 随机字符串
		parameters.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
		// 商户订单号
		parameters.put("partner_trade_no", order.getId());
		// 用户openid
		parameters.put("openid", order.getReceiverOpenId());
		// 校验用户姓名选项
		parameters.put("check_name", "NO_CHECK");
		// 收款用户姓名
		// parameters.put("re_user_name", order.getRe_user_name());
		// 金额
		parameters.put("amount", Integer.toString((int) (order.getRewardRMB() * 100)));
		// 企业付款备注
		parameters.put("desc", order.getDesc());
		// 服务端实际ip
		parameters.put("spbill_create_ip", order.getSpbill_create_ip());
		parameters.put("sign", createSign(parameters));
		String xml = XMLObjectConvertUtil.praseMapToXML(parameters);
		return xml;
	}

	private SortedMap<String, String> queryReward(String queryInfo) throws MalformedURLException, IOException {
		String result = null;
		// 加载证书
		try {
			initCert();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo");
		// 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
		StringEntity postEntity = new StringEntity(queryInfo, "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setEntity(postEntity);
		// 根据默认超时限制初始化requestConfig
		requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout)
				.build();
		// 设置请求器的配置
		httpPost.setConfig(requestConfig);
		try {
			HttpResponse response = null;
			try {
				response = httpClient.execute(httpPost);
			} catch (IOException e) {
				e.printStackTrace();
			}
			HttpEntity entity = response.getEntity();
			try {
				result = EntityUtils.toString(entity, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			httpPost.abort();
		}
		// 接受数据
		SortedMap<String, String> responseMap = XMLObjectConvertUtil.praseXMLToMap(result);
		return responseMap;
	}

	private String createQueryRewardInfo(HongbaodianOrder order) {
		// 创建可排序的Map集合
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		// 应用id
		parameters.put("appid", WXPayConfig.APPID);
		// 商户号
		parameters.put("mch_id", WXPayConfig.MCH_ID);
		// 随机字符串
		parameters.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
		// 商户订单号
		parameters.put("partner_trade_no", order.getId());
		parameters.put("sign", createSign(parameters));
		String xml = XMLObjectConvertUtil.praseMapToXML(parameters);
		return xml;
	}

	private String createQueryRewardInfo(RewardOrderDbo order) {
		// 创建可排序的Map集合
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		// 应用id
		parameters.put("appid", WXPayConfig.APPID);
		// 商户号
		parameters.put("mch_id", WXPayConfig.MCH_ID);
		// 随机字符串
		parameters.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
		// 商户订单号
		parameters.put("partner_trade_no", order.getId());
		parameters.put("sign", createSign(parameters));
		String xml = XMLObjectConvertUtil.praseMapToXML(parameters);
		return xml;
	}

	/**
	 * 生成签名
	 * 
	 * @param parameters
	 * @return
	 */
	private String createSign(SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		/* 拼接 key,设置路径:微信商户平台(pay.weixin.com)->账户设置->API安全-->秘钥设置 */
		sb.append("key=" + WXPayConfig.KEY);
		String sign = MD5Util.getMD5(sb.toString(), WXPayConfig.CHARSET).toUpperCase();
		return sign;
	}

	private void initCert() throws Exception {
		// 证书密码，默认为商户ID
		String key = WXPayConfig.MCH_ID;
		// 证书的路径
		String path = "/data/app/qipai_hongbao/scscyhongbao.p12";
		// 指定读取证书格式为PKCS12
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		// 读取本机存放的PKCS12证书文件
		FileInputStream instream = new FileInputStream(new File(path));
		try {
			// 指定PKCS12的密码(商户ID)
			keyStore.load(instream, key.toCharArray());
		} finally {
			instream.close();
		}
		SSLContext sslContext = new SSLContextBuilder().loadKeyMaterial(keyStore, WXPayConfig.MCH_ID.toCharArray())
				.build();
		HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null,
				hostnameVerifier);
		httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}
}
