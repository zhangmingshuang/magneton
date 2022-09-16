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

package org.magneton.test.util;

import com.google.common.collect.Sets;
import java.lang.reflect.Field;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2021/8/6
 * @since 2.0.0
 */
public class FieldUtil {

	private FieldUtil() {
	}

	public static Set<Field> getFields(@Nullable Class clazz) {
		Set<Field> fields = Sets.newHashSet();
		getFields(fields, clazz);
		return fields;
	}

	@SuppressWarnings("OverlyComplexBooleanExpression")
	private static void getFields(Set<Field> fields, Class clazz) {
		if (clazz == null || clazz == Object.class || clazz.getName().startsWith("java")
				|| clazz.getName().startsWith("javax")) {
			return;
		}
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field declaredField : declaredFields) {
			if (fields.contains(declaredField)) {
				continue;
			}
			fields.add(declaredField);
		}
		getFields(fields, clazz.getSuperclass());
	}

}
