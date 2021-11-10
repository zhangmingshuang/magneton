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
