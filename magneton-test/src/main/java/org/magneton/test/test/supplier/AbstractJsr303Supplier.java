package org.magneton.test.test.supplier;

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
public abstract class AbstractJsr303Supplier<T extends AbstractJsr303Supplier> extends AbstractBooleanSupplier<T> {

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
