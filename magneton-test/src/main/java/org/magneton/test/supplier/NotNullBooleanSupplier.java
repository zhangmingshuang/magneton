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

package org.magneton.test.supplier;

import java.lang.reflect.Field;
import lombok.SneakyThrows;

/**
 * 判断字段的所有数据都不能为 {@code null}
 *
 * <p>
 * 如果任意一个字段为 {@code null} 则返回 {@code false}
 *
 * @author zhangmsh 2021/8/5
 * @since 2.0.0
 */
public class NotNullBooleanSupplier
		extends AbstractBooleanSupplier<NotNullBooleanSupplier> {

	public NotNullBooleanSupplier(Object obj) {
		super(obj);
	}

	@SneakyThrows
	@Override
	public boolean doBooleanJudge() {
		if (this.getObj() == null) {
			super.addError(ValidateError.of(null, null, null, "对象不能是null"));
			return false;
		}
		boolean response = true;
		Object obj = this.getObj();
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Object o = field.get(obj);
			if (o == null) {
				super.addError(ValidateError.of(obj, field, null, "不能为null"));
			}
			response &= (o != null);
		}
		return response;
	}

}
