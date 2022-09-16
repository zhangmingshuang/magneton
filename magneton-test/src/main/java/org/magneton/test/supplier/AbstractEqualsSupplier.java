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
import java.util.Objects;
import java.util.Set;

import org.magneton.test.util.FieldUtil;
import com.google.common.base.Strings;
import lombok.SneakyThrows;

/**
 * .
 *
 * @author zhangmsh 2021/8/6
 * @since 2.0.0
 */
public abstract class AbstractEqualsSupplier<T> extends AbstractBooleanSupplier<T> {

	private final Object element1;

	private final Object element2;

	protected <E> AbstractEqualsSupplier(E element1, E element2) {
		super(null);
		this.element1 = element1;
		this.element2 = element2;
	}

	@SneakyThrows
	@Override
	@SuppressWarnings("java:S3011")
	protected boolean doBooleanJudge() {
		if (Objects.equals(this.element1, this.element2)) {
			return true;
		}
		Class<?> clazz1 = this.element1.getClass();
		Class<?> clazz2 = this.element2.getClass();
		if (clazz1 != clazz2) {
			String error = Strings.lenientFormat("%s与%s不是相同的类", clazz1.getName(), clazz2.getName());
			this.addError(ValidateError.of(null, null, null, error));
			return false;
		}
		StringBuilder builder = new StringBuilder(128);
		boolean reponse = true;
		Set<Field> fields = FieldUtil.getFields(clazz1);
		for (Field field : fields) {
			field.setAccessible(true);
			Object value1 = field.get(this.element1);
			Object value2 = field.get(this.element2);
			if (!Objects.equals(value1, value2)) {
				builder.append(clazz1.getSimpleName()).append("#").append(field.getName()).append("值[").append(value1)
						.append("]与[").append(value2).append("]不相等;");
				reponse = this.doFeieldValueNotEqualsProcess(field.getType(), value1, value2);
			}
		}
		if (!reponse) {
			this.addError(ValidateError.of(null, null, null, builder.toString()));
		}
		builder.setLength(0);
		return reponse;
	}

	protected boolean doFeieldValueNotEqualsProcess(Class<?> fieldClass, Object fieldValue1, Object fieldValue2) {
		return false;
	}

}
