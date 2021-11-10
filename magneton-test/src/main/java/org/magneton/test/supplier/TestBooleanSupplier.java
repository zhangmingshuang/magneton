package org.magneton.test.supplier;

import java.util.function.BooleanSupplier;

/**
 * .
 *
 * @author zhangmsh 2021/8/5
 * @since 2.0.0
 */
public class TestBooleanSupplier {

	private static final TestBooleanSupplier BOOLEAN_TEST_SUPPLIER = new TestBooleanSupplier();

	private TestBooleanSupplier() {
	}

	public static TestBooleanSupplier getInstance() {
		return BOOLEAN_TEST_SUPPLIER;
	}

	/**
	 * 判断一个对象或对象的所有字段数据不能为 {@code null}
	 * @param obj 要判断的对象
	 * @return Boolean Supplier.
	 */
	public BooleanSupplier notNull(Object obj) {
		return new NotNullBooleanSupplier(obj).sout();
	}

	/**
	 * 判断一个对象的的所有字段是否符合标准 {@code jsr303} 标准的校验
	 *
	 * <p>
	 * 注意：如果要判断的对象 {@code obj} 为 {@code null}， 则判断一定为 {@code true}
	 * @param obj 要判断的对象
	 * @return Boolean Supplier.
	 */
	public BooleanSupplier jsr303(Object obj) {
		return new Jsr303BooleanSupplier(obj).sout();
	}

	/**
	 * 判断两个对象的引用或字段数据是否相同
	 *
	 * <p>
	 * 注意：时间类型{@link java.util.Date} 或 {@link java.sql.Date}默认允许存在一定的时间差异，
	 * 祥见{@link ValueEqualsBooleanSupplier#doFeieldValueNotEqualsProcess(Class, Object, Object)}
	 * @param element1 对象1
	 * @param element2 对象2
	 * @param <E> 相同对象
	 * @return Boolean Supplier.
	 */
	public <E> BooleanSupplier valueEquals(E element1, E element2) {
		return new ValueEqualsBooleanSupplier(element1, element2).sout();
	}

}
