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

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 标准的{@code jsr303}标准注解进行校验.
 *
 * @author zhangmsh 2021/8/4
 * @since 2.0.0
 */
@Slf4j
public class Jsr303BooleanSupplier extends AbstractJsr303Supplier<Jsr303BooleanSupplier> {

	public Jsr303BooleanSupplier(Object obj) {
		super(obj);
	}

	@SneakyThrows
	@Override
	protected boolean doBooleanJudge() {
		if (this.getObj() == null) {
			return true;
		}
		// 实例化一个 validator工厂
		try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
			// 获取validator实例
			Validator validator = validatorFactory.getValidator();
			// 调用调用，得到校验结果信息 Set
			Set<ConstraintViolation<Object>> constraintViolations = validator.validate(super.getObj());
			super.addError(constraintViolations);
			return constraintViolations.isEmpty();
		}
	}

}
