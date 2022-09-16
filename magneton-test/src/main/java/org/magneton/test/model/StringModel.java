/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号401
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.test.model;

import javax.annotation.Nullable;

import org.magneton.test.model.generate.EmailAddressGeneratorAbstract;
import org.magneton.test.model.generate.base.AbstractGenericGenerator;

public enum StringModel {

	/**
	 * 正常模式
	 */
	NORMAL(null),
	/**
	 * 邮箱模式
	 */
	EMAIL(EmailAddressGeneratorAbstract.getInstance());

	private final AbstractGenericGenerator abstractGenericGenerator;

	StringModel(AbstractGenericGenerator abstractGenericGenerator) {
		this.abstractGenericGenerator = abstractGenericGenerator;
	}

	@Nullable
	public AbstractGenericGenerator getGenericGenerator() {
		return this.abstractGenericGenerator;
	}

}
