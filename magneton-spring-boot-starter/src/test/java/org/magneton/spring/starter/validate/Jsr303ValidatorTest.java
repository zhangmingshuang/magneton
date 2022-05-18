package org.magneton.spring.starter.validate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author zhangmsh 2022/4/23
 * @since 1.0.0
 */
class Jsr303ValidatorTest {

	public static class A {

		@Min(11)
		private int id;

		@NotBlank
		private String str;

	}

	@Test
	void test() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> Jsr303Validator.validate(new A()));
	}

}