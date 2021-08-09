package cn.nascent.framework.test.supplier;

import javax.validation.constraints.Min;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/6
 * @since
 */
class Jsr303BooleanSupplierTest {

	@Test
	void getAsBoolean() {
		Assertions.assertTrue(new Jsr303BooleanSupplier(null));

		Jsr303BooleanSupplier jsr303BooleanSupplier = new Jsr303BooleanSupplier(new StandardDto()).print();
		Assertions.assertTrue(jsr303BooleanSupplier.getAsBoolean());

		jsr303BooleanSupplier = new Jsr303BooleanSupplier(new ErrorDto()).print();
		Assertions.assertFalse(jsr303BooleanSupplier.getAsBoolean());
	}

	public static class StandardDto {

		@Min(0)
		private int i = 1;

	}

	public static class ErrorDto {

		@Min(11)
		private int i = 1;

	}

}