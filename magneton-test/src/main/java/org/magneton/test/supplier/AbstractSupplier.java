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

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.reflect.Field;
import java.util.List;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author zhangmsh 2021/8/5
 * @since 2.0.0
 */
@Slf4j
public class AbstractSupplier<T> {

	private boolean print;

	private final Object obj;

	private List<ValidateError> errors;

	public AbstractSupplier(@Nullable Object obj) {
		this.obj = obj;
	}

	@Nullable
	protected Object getObj() {
		return this.obj;
	}

	protected void addError(ValidateError error) {
		if (this.errors == null) {
			this.errors = Lists.newArrayList();
		}
		this.errors.add(error);
	}

	protected boolean isPrintable() {
		return this.print;
	}

	@CanIgnoreReturnValue
	protected boolean takeErrors() {
		if (this.errors == null || this.errors.isEmpty()) {
			return false;
		}
		if (this.isPrintable()) {
			this.printErrors();
		}
		return true;
	}

	protected void printErrors() {
		StringBuilder builder = new StringBuilder(128);
		for (ValidateError error : this.errors) {
			String errorMessage = error.getMessage();
			Object obj = error.getObj();
			String className = "";
			String propertyPath = "";
			Object invalidValue = error.getInvalidValue();
			if (obj != null) {
				Class clazz = obj.getClass();
				className = clazz.getName();
				builder.append("类").append(className);
				propertyPath = error.getField() == null ? "" : error.getField().getName();
				if (!Strings.isNullOrEmpty(propertyPath)) {
					builder.append("#").append(propertyPath);
				}
			}
			if (invalidValue != null) {
				builder.append("值为：").append(invalidValue).append("，但是预期：");
			}
			builder.append(errorMessage);
			this.doPrint(builder.toString());
		}
	}

	@SuppressWarnings("UseOfSystemOutOrSystemErr")
	protected void doPrint(String msg) {
		if (Strings.isNullOrEmpty(msg)) {
			return;
		}
		System.out.println(msg);
		System.out.println("----");
	}

	public void setPrint(boolean print) {
		this.print = print;
	}

	@Setter
	@Getter
	@ToString
	@AllArgsConstructor
	public static class ValidateError {

		private Object obj;

		private Field field;

		private Object invalidValue;

		private String message;

		public static ValidateError of(Object obj, Field field, Object invalidValue, String message) {
			return new ValidateError(obj, field, invalidValue, message);
		}

	}

}
