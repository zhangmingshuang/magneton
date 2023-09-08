package org.magneton.spring.starter.validate;

import com.google.common.base.Preconditions;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Jsr303Validator.
 *
 * @author zhangmsh 2022/4/23
 * @since 1.0.0
 */
public class Jsr303Validator {

	private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

	private Jsr303Validator() {
	}

	public static void validate(Object object) {
		Preconditions.checkNotNull(object, "object must not be null");
		Validator validator = VALIDATOR_FACTORY.getValidator();
		// 调用调用，得到校验结果信息 Set
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
		if (constraintViolations.isEmpty()) {
			return;
		}
		StringBuilder builder = new StringBuilder(256);
		for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
			builder.append(constraintViolation.getPropertyPath()).append(" : ").append(constraintViolation.getMessage())
					.append("(").append(constraintViolation.getInvalidValue()).append(")").append("\n");
		}
		throw new IllegalArgumentException(builder.toString());
	}

}
