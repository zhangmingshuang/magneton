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

package org.magneton.monitor.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

/**
 * 业务对象
 * <p>
 * 该对象用来处理复杂的业务场景，包括：业务ID、使用用例、场景
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Setter
@Getter
public class Biz {

	/**
	 * 业务ID
	 */
	private String id;

	/**
	 * 使用用例
	 */
	@Nullable
	private String useCase;

	/**
	 * 场景
	 */
	@Nullable
	private String scenario;

	/**
	 * to biz id
	 * @return biz id
	 */
	public String toBizId() {
		return Biz.valueOfBizId(this.id, this.useCase, this.scenario);
	}

	/**
	 * 创建业务对象
	 * @param id 业务ID
	 * @return 业务对象
	 */
	public static Biz of(String id) {
		return of(id, null, null);
	}

	/**
	 * 创建业务对象
	 * @param id 业务ID
	 * @param useCase 使用用例
	 * @return 业务对象
	 */
	public static Biz of(String id, @Nullable String useCase) {
		return of(id, useCase, null);
	}

	/**
	 * 创建业务对象
	 * @param id 业务ID
	 * @param useCase 使用用例
	 * @param scenario 场景
	 * @return 业务对象
	 */
	public static Biz of(String id, @Nullable String useCase, @Nullable String scenario) {
		Preconditions.checkNotNull(id, "id must not be null");
		Biz biz = new Biz();
		biz.setId(id);
		biz.setUseCase(useCase);
		biz.setScenario(scenario);
		return biz;
	}

	/**
	 * 组装ID
	 * @param id 业务ID
	 * @param useCase 用例
	 * @param scenario 场景
	 * @return ID
	 */
	public static String valueOfBizId(String id, @Nullable String useCase, @Nullable String scenario) {
		Preconditions.checkNotNull(id, "id is null");

		StringBuilder builder = new StringBuilder(64);
		builder.append(id);
		if (!Strings.isNullOrEmpty(useCase)) {
			builder.append("-").append(useCase);
		}
		if (!Strings.isNullOrEmpty(scenario)) {
			builder.append("-").append(scenario);
		}
		return builder.toString();
	}

	@Override
	public String toString() {
		return this.toBizId();
	}

}
