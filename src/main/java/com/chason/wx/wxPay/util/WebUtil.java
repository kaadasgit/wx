package com.chason.wx.wxPay.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * web相关操作工具类
 * 
 * @author Alex
 * 
 */
public class WebUtil {

	private static Log log = LogFactory.getLog(WebUtils.class);
	

	/**
	 * 获取请求客户端的IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = getWholeIpAddr(request);
		if (StringUtils.isEmpty(ip))
			return null;

		if (ip.indexOf(",") > 0) {
			String[] ips = ip.split(",");
			for (String i : ips) {
				if (StringUtils.isEmpty(i))
					continue;
				i = i.trim();
				if (isVaildIp(i))
					return i;
			}
			ip = ips[0].trim();
		}
		return ip.trim();
	}
	
	/**
	 * 获取请求客户端的IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getWholeIpAddr(HttpServletRequest request) {
		if (null == request)
			return null;
		String ip = request.getHeader("X-Real-IP");
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * 是否是有效的IP地址
	 * @param ip
	 * @return
	 */
	public static boolean isVaildIp(String ip){
		/*
		 * 	IPv4专用地址如下：
			IP等级 IP位置
			Class A 10.0.0.0-10.255.255.255
			默认子网掩码:255.0.0.0
			Class B 172.16.0.0-172.31.255.255
			默认子网掩码:255.255.0.0
			Class C 192.168.0.0-192.168.255.255
			默认子网掩码:255.255.255.0
		 */
		if(StringUtils.isEmpty(ip))
			return false;
		return !(ip.startsWith("10.") || ip.startsWith("172.") || ip.startsWith("192."));
	}
	
	public static void main(String[] args) {
		/*System.out.println(isVaildIp("10.0.0.0"));
		System.out.println(isVaildIp("172.16.0.0"));
		System.out.println(isVaildIp("192.168.0.0"));
		
		System.out.println(isVaildIp("110.0.0.0"));*/
		System.out.println(getIpAddr(null));
	}

	public static void logRequestInformation(HttpServletRequest request) {
		log.info("scheme = " + request.getScheme() + "");
		log.info("serverName = " + request.getServerName() + "");
		log.info("serverPort = " + request.getServerPort() + "");
		log.info("contextPath = " + request.getContextPath() + "");
		log.info("servletPath = " + request.getServletPath() + "");
		log.info("queryString = " + request.getQueryString() + "");
		log.info("requestURI = " + request.getRequestURI() + "");
		log.info("info = " + request.getPathInfo() + "");
		log.info("request= " + request.toString() + "");
	}
}
