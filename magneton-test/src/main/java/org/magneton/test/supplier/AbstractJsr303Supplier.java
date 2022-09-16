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
import java.util.Set;
import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author zhangmsh 2021/8/4
 * @since 2.0.0
 */
@Slf4j
public abstract class AbstractJsr303Supplier<T extends AbstractJsr303Supplier>
		extends AbstractBooleanSupplier<T> {

	private Set<ConstraintViolation<Object>> constraintViolations;

	protected AbstractJsr303Supplier(Object obj) {
		super(obj);
	}

	@Override
	protected boolean isPrintable() {
		return this.constraintViolations != null && !this.constraintViolations.isEmpty();
	}

	protected void addError(@Nullable Set<ConstraintViolation<Object>> constraintViolations) {
		this.constraintViolations = constraintViolations;
	}

	@Override
	protected void printErrors() {
		for (ConstraintViolation<Object> error : this.constraintViolations) {
			String errorMsg = error.getMessage();
			Class<Object> clazz = error.getRootBeanClass();
			Object rootBean = error.getRootBean();
			Path propertyPath = error.getPropertyPath();
			Object invalidValue = error.getInvalidValue();
			String msg = Strings.lenientFormat("类%s#%s值为%s，但是预期：%s", clazz.getName(), propertyPath, invalidValue,
					errorMsg);
			this.doPrint(msg);
		}
	}

}
