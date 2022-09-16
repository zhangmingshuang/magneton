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

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.annotation.Nullable;

import org.magneton.test.annotation.TestAutowired;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;
import org.magneton.test.util.AnnotationUtil;
import org.hibernate.validator.constraints.Range;

/**
 * {@link org.hibernate.validator.constraints.Range}
 *
 * <p>
 * {@link javax.validation.constraints.Min}与{@link javax.validation.constraints.Max}的组合
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 */
@TestComponent
public class RangeConfigPostProcessor extends AbstractConfigPostProcessor {

	@TestAutowired
	private MaxConfigPostProcessor maxConfigPostProcessor;

	@TestAutowired
	private MinConfigPostProcessor minConfigPostProcessor;

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		Map<String, Object> metadata = AnnotationUtil.getMetadata(annotation);
		long min = (long) metadata.get("min");
		long max = (long) metadata.get("max");

		this.minConfigPostProcessor.setByte(config, min);
		this.minConfigPostProcessor.setShort(config, min);
		this.minConfigPostProcessor.setInt(config, min);
		this.minConfigPostProcessor.setLong(config, min);
		this.minConfigPostProcessor.setBigDecimal(config, min);
		this.minConfigPostProcessor.setBigInteger(config, min);

		this.maxConfigPostProcessor.setByte(config, max);
		this.maxConfigPostProcessor.setShort(config, max);
		this.maxConfigPostProcessor.setInt(config, max);
		this.maxConfigPostProcessor.setLong(config, max);
		this.maxConfigPostProcessor.setBigDecimal(config, max);
		this.maxConfigPostProcessor.setBigInteger(config, max);
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { Range.class };
	}

}
