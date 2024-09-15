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

package org.magneton.foundiation;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

/**
 * profile工具类.
 *
 * @author zhangmsh.
 * @since 1.0.2
 */
@Slf4j
public class RuntimeProfile {

	/**
	 * 配置的PROFILE环境变量
	 */
	private static final String PROFILE_KEY = "PROFILE";

	/**
	 * 适配单体的PROFILE环境变量
	 */
	private static final String SPRING_PROFILES_ACTIVE_KEY = "SPRING_PROFILES_ACTIVE";

	/**
	 * 获取当前有效的profile
	 * @apiNote 读取环境变量的值，如果没有则使用-D配置的值。优先级：PROFILE > SPRING_PROFILES_ACTIVE，如果都没有则使用local。
	 * @return 当前有效的profile
	 */
	public static String getEffectiveProfile() {
		String profile = RuntimeArgs.sys(PROFILE_KEY).get();
		log.info("get profile from env PROFILE = {}", profile);
		if (Strings.isNullOrEmpty(profile)) {
			// fix: 兼容旧的项目PROFILE配置
			log.warn("get PROFILE from env but not found, try get SPRING_PROFILES_ACTIVE from env...");
			profile = RuntimeArgs.sys(SPRING_PROFILES_ACTIVE_KEY).get();
			if (!Strings.isNullOrEmpty(profile)) {
				log.info("using profile from SPRING_PROFILES_ACTIVE = {}", profile);
			}
		}
		if (Strings.isNullOrEmpty(profile)) {
			// 兜底
			profile = "local";
			log.warn("get profile from env but all not found. use default profile : {}", profile);
		}
		return profile;
	}

	/**
	 * 获取当前有效的profile
	 * @param keys
	 * 环境变量key，优先使用key尝试读取系统属性变量及-D配置的变量，如果都没有则使用{@link #getEffectiveProfile()}
	 * @return 当前有效的profile，如果都没有，则使用local
	 */
	public static String getEffectiveProfile(String... keys) {
		for (String key : keys) {
			String profile = RuntimeArgs.sys(key).get();
			log.info("get profile from env {} = {}", key, profile);
			if (!Strings.isNullOrEmpty(profile)) {
				return profile;
			}
		}
		return getEffectiveProfile();
	}

}