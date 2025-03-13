/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号501
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.framework.core.utils;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;

import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * IP工具类
 *
 * @author zhangmsh
 * @since 2.0.0
 */
@Slf4j
public class IpUtils {

	/**
	 * unknown
	 */
	private static final String UNKNOWN = "unknown";

	/**
	 * 本地IP
	 */
	private static final String LOCALHOST = "";

	/**
	 * 逗号
	 */
	private static final String SEPARATOR = ",";

	/**
	 * IP请求头
	 */
	private static final String[] HEADERS = { "x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP" };

	private IpUtils() {
	}

	@Nullable
	public static String getRealIpAddress(ServerHttpRequest request) {
		try {
			HttpHeaders headers = request.getHeaders();
			String ipAddress = null;
			for (int i = 0; i < HEADERS.length; i++) {
				String header = HEADERS[i];
				ipAddress = headers.getFirst(header);
				if (!Strings.isNullOrEmpty(ipAddress) && !UNKNOWN.equalsIgnoreCase(ipAddress)) {
					break;
				}
			}
			if (Strings.isNullOrEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
				InetSocketAddress remoteAddress = request.getRemoteAddress();
				InetAddress address = remoteAddress.getAddress();
				if (address != null) {
					ipAddress = address.getHostName();
					if (LOCALHOST.equalsIgnoreCase(ipAddress)) {
						ipAddress = InetAddress.getLocalHost().getHostAddress();
					}
				}
			}

			if (ipAddress != null) {
				ipAddress = ipAddress.split(SEPARATOR)[0].trim();
			}
			return ipAddress;
		}
		catch (Throwable e) {
			log.error("获取请求" + request.getURI() + "的IP地址异常", e);
		}
		return null;
	}

}
