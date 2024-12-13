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

import com.google.common.base.MoreObjects;
import lombok.extern.slf4j.Slf4j;
import org.magneton.foundiation.RuntimeArgs;
import org.magneton.foundiation.RuntimeProfile;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.SpringFactories;

/**
 * sls监控自动配置
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
@Configuration
@ComponentScan
@SpringFactories
public class SlsMonitorAutoConfiguration {

	/**
	 * 监控环境变量
	 */
	private static final String MONITOR_ENV_KEY = "MONITOR_ENV";

	/**
	 * 监控开关环境变量
	 */
	private static final String MONITOR_ENABLE_KEY = "MONITOR_ENABLE";

	/**
	 * 监控开关
	 */
	private static final boolean ENABLE = Boolean
		.parseBoolean(MoreObjects.firstNonNull(RuntimeArgs.sys(MONITOR_ENABLE_KEY).get(), "true"));

	/**
	 * 当前有效的profile
	 */
	private static final String PROFILE = RuntimeProfile.getEffectiveProfile(MONITOR_ENV_KEY);

	/**
	 * 获取当前系统的profile
	 * @return 当前生效的profile
	 */
	public static String getProfile() {
		return PROFILE;
	}

	/**
	 * 判断当前的监控是否开启
	 * @return true:开启 false:关闭
	 */
	public static boolean isEnable() {
		return ENABLE;
	}

}