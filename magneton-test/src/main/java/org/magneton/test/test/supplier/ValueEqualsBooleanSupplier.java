package org.magneton.test.test.supplier;

import java.util.Date;

/**
 * 判断两个相同类型的对象的数据是否全部相等.
 *
 * <p>
 * 如果两个对象的任意一个数据不相等，则返回 {@code false}
 *
 * @author zhangmsh 2021/8/6
 * @since 2.0.0
 */
public class ValueEqualsBooleanSupplier
		extends AbstractEqualsSupplier<ValueEqualsBooleanSupplier> {

	/** Date类型的时间比较时允许的时间差异，单位毫秒 */
	private int dateTimeDiffTolerate = 999;

	public <E> ValueEqualsBooleanSupplier(E element1, E element2) {
		super(element1, element2);
	}

	@Override
	protected boolean doFeieldValueNotEqualsProcess(Class<?> fieldClass, Object fieldValue1, Object fieldValue2) {
		if (Date.class.isAssignableFrom(fieldClass) || java.sql.Date.class.isAssignableFrom(fieldClass)) {
			long time1 = ((Date) fieldValue1).getTime();
			long time2 = ((Date) fieldValue2).getTime();
			return Math.abs(time1 - time2) < this.dateTimeDiffTolerate;
		}
		return false;
	}

	public void setDateTimeDiffTolerate(int dateTimeDiffTolerate) {
		this.dateTimeDiffTolerate = dateTimeDiffTolerate;
	}

	public int getDateTimeDiffTolerate() {
		return this.dateTimeDiffTolerate;
	}

}
