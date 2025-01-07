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

package org.magneton.monitor;

import cn.nascent.tech.gaia.annotation.SpringFactories;
import cn.nascent.tech.gaia.foundation.RuntimeArgs;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.annotation.Nullable;

/**
 * SpringBoot适配器
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
@SpringFactories
public class SpringBootAdapter implements SpringApplicationRunListener {

	private SpringApplication application;

	private static String springProfilesActive;

	public SpringBootAdapter(SpringApplication application, String[] args) {
		this.application = application;
	}

	/**
	 * 环境准备就绪，初始化应用名称
	 * @param environment the environment
	 */
	@Override
	public void environmentPrepared(ConfigurableEnvironment environment) {
		String applicationName = environment.getProperty("spring.application.name");
		if (Strings.isNullOrEmpty(applicationName)) {
			applicationName = this.application.getMainApplicationClass().getSimpleName();
		}
		if (Strings.isNullOrEmpty(RuntimeArgs.property("spring.application.name").get())) {
			System.setProperty("spring.application.name", applicationName);
		}
		springProfilesActive = environment.getProperty("spring.profiles.active");
	}

	/**
	 * 获取当前profile
	 * @return 当前profile
	 */
	@Nullable
	public static String getProfile() {
		String profile = RuntimeArgs.sys("PROFILE").get();
		if (Strings.isNullOrEmpty(profile)) {
			// 兼容旧的项目PROFILE配置
			profile = RuntimeArgs.sys("SPRING_PROFILES_ACTIVE").get();
		}
		if (Strings.isNullOrEmpty(profile)) {
			profile = springProfilesActive;
		}
		return profile;

	}

}