package cn.nascent.framework.test.supplier;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/6
 * @since 2.0.0
 * @see ValueEqualsBooleanSupplier
 */
class ValueEqualsBooleanSupplierTest {

	@Test
	void test() {

		Assertions.assertTrue(new ValueEqualsBooleanSupplier(null, null));

		ValueEqualsBooleanSupplier valueEqualsBooleanSupplier = new ValueEqualsBooleanSupplier(new A(), new A());
		Assertions.assertTrue(valueEqualsBooleanSupplier.getAsBoolean());

		valueEqualsBooleanSupplier = new ValueEqualsBooleanSupplier(new A(), new A1()).print();
		Assertions.assertFalse(valueEqualsBooleanSupplier.getAsBoolean());

		valueEqualsBooleanSupplier = new ValueEqualsBooleanSupplier(new A(), new A2()).print();
		Assertions.assertFalse(valueEqualsBooleanSupplier.getAsBoolean());
	}

	@Setter
	@Getter
	@ToString
	public static class A {

		private int i = 1;

		private String s = "123";

		private Date date = new Date();

		double d = 0.2;

	}

	@Setter
	@Getter
	@ToString
	public static class A1 {

		private int i = 2;

		private String s = "123";

	}

	@Setter
	@Getter
	@ToString
	public static class A2 extends A {

		private long l = 123;

	}

}