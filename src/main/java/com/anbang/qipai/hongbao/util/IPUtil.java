package com.anbang.qipai.hongbao.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {

	/**
	 * 获取真实ip
	 */
	public static String getRealIp(HttpServletRequest request) {
		String ip;
		ip = request.getHeader("X-Real-IP");
		if (ip == null) {
			String xip = request.getHeader("x-forwarded-for");
			if (xip != null) {
				String[] ips = xip.split(",");
				ip = ips[0];
			} else {
				ip = request.getRemoteAddr();
			}
		}
		return ip;
	}

	/**
	 * 验证请求是否经过代理
	 */
	public static boolean verifyIp(HttpServletRequest request) {
		String ip;
		ip = request.getHeader("X-Real-IP");
		if (ip == null) {
			String xip = request.getHeader("X-Forwarded-For");
			if (xip != null) {
				String[] ips = xip.split(",");
				if (ips.length > 2) {
					return false;
				}
			} else {
				return true;
			}
		}
		return true;
	}

	/**
	 * 返回本机外网ip地址，如果没有外网ip就返回内网ip
	 */
	public static String getLocalHostRelIP() throws UnknownHostException, SocketException {
		String INTRANET_IP = InetAddress.getLocalHost().getHostAddress(); // 内网IP

		Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		Enumeration<InetAddress> addrs;
		while (networks.hasMoreElements()) {
			addrs = networks.nextElement().getInetAddresses();
			while (addrs.hasMoreElements()) {
				ip = addrs.nextElement();
				if (ip != null && ip instanceof Inet4Address && ip.isSiteLocalAddress()
						&& !ip.getHostAddress().equals(INTRANET_IP)) {
					return ip.getHostAddress();
				}
			}
		}

		// 如果没有外网IP，就返回内网IP
		return INTRANET_IP;
	}
}
