package org.magneton.test;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.magneton.core.base.Strings;

/**
 * .
 *
 * @author zhangmsh 2021/8/24
 * @since 2.0.0
 */
public class HibernateValid {

	private HibernateValid() {
	}

	public static boolean valid(Object obj) {
		try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
			Set<ConstraintViolation<Object>> validate = validatorFactory.getValidator().validate(obj);
			if (!validate.isEmpty()) {
				for (ConstraintViolation<Object> error : validate) {
					String errorMsg = error.getMessage();
					Class<Object> clazz = error.getRootBeanClass();
					Object rootBean = error.getRootBean();
					Path propertyPath = error.getPropertyPath();
					Object invalidValue = error.getInvalidValue();
					String msg = Strings.lenientFormat("类%s#%s值为%s，但是预期：%s", clazz.getName(), propertyPath,
							invalidValue, errorMsg);
					System.out.println(msg);
				}
			}
			return validate.isEmpty();
		}
	}

}
