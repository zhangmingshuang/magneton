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

package org.magneton.test.core;

import org.magneton.test.config.Config;

/**
 * 注入类型.
 *
 * @author zhangmsh 2021/8/2
 * @since 1.0.0
 */
public enum InjectType {

	/** 预期，根据JSR303注入标准数据或者根据字段类型对应的配置{@link Config}注入预期的数据 */
	EXPECTED,
	/** 反预期，根据JSR303注入反预期的JSR303数据或者{@link Config}配置的范围反向生成 */
	ANTI_EXPECTED,
	/**
	 * 如果是原子类型及其封装类型，采设置对应的默认值
	 *
	 * <p>
	 * 如果不是，则会一直解析类型存在的字段属性，直接找到原子类型及期封装类型
	 *
	 * <p>
	 * 或者直到是 {@code Object.class}或者是以{@code java/javax}开头的包名对象，然后返回 {@code null}
	 */
	DEFAULT_VALUE;

}
