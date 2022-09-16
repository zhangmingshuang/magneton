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

package org.magneton.test.validate;

import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;

import java.lang.annotation.Annotation;
import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;

/**
 * {@code @NotEmpty}验证注解的元素值不为 null 且不为空（字符串长度不为 0、集合大小不为 0）
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 */
@TestComponent
public class NotEmptyConfigPostProcessor extends AbstractConfigPostProcessor {

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		int minSize = config.getMinSize();
		if (minSize < 1) {
			config.setMinSize(1);
		}
		int maxSize = config.getMaxSize();
		if (maxSize < 1) {
			config.setMaxSize(1);
		}
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { NotEmpty.class, org.hibernate.validator.constraints.NotEmpty.class };
	}

}
