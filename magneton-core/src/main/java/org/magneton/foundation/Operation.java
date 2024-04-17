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

package org.magneton.foundation;

import javax.annotation.Nullable;

/**
 * 操作
 *
 * @author zhangmsh 2022/11/17
 * @since 1.0.0
 */
public interface Operation {

	/**
	 * 获取当前值
	 * @return 当前值
	 */
	@Nullable
	String get();

	default String getOrDefault(String defaultValue) {
		String value = this.get();
		return value == null || value.isEmpty() ? defaultValue : value;
	}

	/**
	 * 或者
	 * @param arg 参数
	 * @return 当前值为 {@code null} 时，获取该参数对应的值
	 */
	@Nullable
	String orElse(String arg);

	/**
	 * 或者
	 * @param arg 参数
	 * @param defaultValue 默认值
	 * @return 当前值为 {@code null} 时，获取该参数对应的值，如果该参数对应的值也为 {@code null}，则返回默认值
	 */
	String orElse(String arg, String defaultValue);

	/**
	 * 或者
	 * @param arg 参数
	 * @return 当前值为 {@code null}时，获取该参数对应的值操作
	 */
	Operation or(String arg);

}