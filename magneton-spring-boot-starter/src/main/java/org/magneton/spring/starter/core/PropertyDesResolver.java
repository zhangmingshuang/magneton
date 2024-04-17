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

package org.magneton.spring.starter.core;

import org.apache.commons.lang3.StringUtils;
import org.magneton.foundation.DesUtil;
import org.magneton.foundation.RuntimeArgs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 配置文件自动解码
 *
 * <pre>
 *  yml文件中加上NASCENT-DES(XXX)，就可以进行对括号内XXX自动解密，省去了手动在代码中进行解密的操作。
 *  解码器：
 *  {@link DesUtil#decrypt(String)}
 *  eg:
 *  {@code
 *    spring:
 *      redis:
 *		  redisson:
 *          config: |
 *            singleServerConfig:
 *              password: NASCENT-DES(null)
 *  }
 * </pre>
 *
 * @author zhangmsh
 * @since 2.0.0
 */
public class PropertyDesResolver {

	private static final String REGEX = "NASCENT-DES\\((.*?)\\)";

	private final Pattern pattern = Pattern.compile(REGEX);

	private static final String DES_KEY = RuntimeArgs.env("MAGNETON_DES_KEY").getOrDefault("magneton");

	public static String encrypt(String data) {
		return DesUtil.encrypt(data, DES_KEY);
	}

	public PropertyDesResolver() {
	}

	public String resolvePropertyValue(String value) {
		if (StringUtils.isBlank(value)) {
			return value;
		}
		return this.resolveDESValue(value);
	}

	private String resolveDESValue(String value) {
		if (value == null) {
			return value;
		}
		final String trimmedValue = value.trim();

		Matcher matcher = this.pattern.matcher(trimmedValue);

		StringBuffer sb = new StringBuffer();

		// 根据配置格式为：NASCENT-DES(XXX)内的信息进行解码
		while (matcher.find()) {
			String matchStr = matcher.group(1);
			// 空字符串不解码
			if (StringUtils.isBlank(matchStr) || ("null").equalsIgnoreCase(matchStr)) {
				matcher.appendReplacement(sb, matchStr);
			}
			else {
				String decrypt = DesUtil.decrypt(matchStr, DES_KEY);

				// 当解密明文中存在特殊字符 $ 与 \时，appendReplacement会做特殊处理($被作为反向引用组)，需要进行转义处理
				if (StringUtils.isNotBlank(decrypt)) {
					decrypt = decrypt.replaceAll("\\$", "\\\\\\$");
				}

				matcher.appendReplacement(sb, decrypt);
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

}