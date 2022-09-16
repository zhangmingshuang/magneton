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

package org.magneton.test.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 2021/8/17
 * @since 2.0.0
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Definition {

	private Class<?> clazz;

	@Nullable
	private Field field;

	@Nullable
	private List<Class<?>> generics;

	@Nullable
	private Map<Class<?>, Annotation> annotations;

	@Nullable
	private List<Definition> childDefinitions;

	public Definition resetClazz(Class<?> componentType) {
		this.clazz = componentType;
		return this;
	}

}
