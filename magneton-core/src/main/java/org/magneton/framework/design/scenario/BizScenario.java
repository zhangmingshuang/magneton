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

package org.magneton.framework.design.scenario;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;

/**
 * BizScenario（业务场景）= bizId + useCase + scenario, which can uniquely identify a user
 * scenario.
 *
 * @author zhangmsh
 * @since 1.0.0
 **/
@Getter
@ToString
public class BizScenario {

	/**
	 * 默认的业务ID
	 */
	public static final String DEFAULT_BIZ_ID = "#defaultBizId#";

	/**
	 * 默认的业务模块
	 */
	public static final String DEFAULT_USE_CASE = "#defaultUseCase#";

	/**
	 * 默认的业务场景
	 */
	public static final String DEFAULT_SCENARIO = "#defaultScenario#";

	/**
	 * 业务ID
	 */
	private final String bizId;

	/**
	 * 业务用例
	 */
	private final String useCase;

	/**
	 * 业务场景
	 */
	private final String scenario;

	public BizScenario(@Nullable String bizId, @Nullable String useCase, @Nullable String scenario) {
		this.bizId = Strings.isNullOrEmpty(bizId) ? DEFAULT_BIZ_ID : bizId;
		this.useCase = Strings.isNullOrEmpty(useCase) ? DEFAULT_USE_CASE : useCase;
		this.scenario = Strings.isNullOrEmpty(scenario) ? DEFAULT_SCENARIO : scenario;
	}

	public static BizScenario valueOf(@Nullable String bizId, @Nullable String useCase, @Nullable String scenario) {
		return new BizScenario(bizId, useCase, scenario);
	}

	/**
	 * 构造默认的场景扩展点
	 * @param bizId 业务系统
	 * @param useCase 用例
	 * @return 扩展点
	 */
	public static BizScenario valueOf(@Nullable String bizId, @Nullable String useCase) {
		return BizScenario.valueOf(bizId, useCase, DEFAULT_SCENARIO);
	}

	/**
	 * 构造默认的用例扩展点
	 * @param bizId 业务系统
	 * @return 扩展点
	 */
	public static BizScenario valueOf(@Nullable String bizId) {
		return BizScenario.valueOf(bizId, DEFAULT_USE_CASE, DEFAULT_SCENARIO);
	}

	/**
	 * 构造默认的协议扩展点
	 * @return 扩展点
	 */
	public static BizScenario newDefault() {
		return BizScenario.valueOf(DEFAULT_BIZ_ID, DEFAULT_USE_CASE, DEFAULT_SCENARIO);
	}

}