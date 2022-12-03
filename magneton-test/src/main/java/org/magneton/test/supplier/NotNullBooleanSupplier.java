package org.magneton.test.supplier;

import java.lang.reflect.Field;
import lombok.SneakyThrows;

/**
 * 判断字段的所有数据都不能为 {@code null}
 *
 * <p>
 * 如果任意一个字段为 {@code null} 则返回 {@code false}
 *
 * @author zhangmsh 2021/8/5
 * @since 2.0.0
 */
public class NotNullBooleanSupplier extends AbstractBooleanSupplier<NotNullBooleanSupplier> {

	public NotNullBooleanSupplier(Object obj) {
		super(obj);
	}

	@SneakyThrows
	@Override
	public boolean doBooleanJudge() {
		if (this.getObj() == null) {
			super.addError(ValidateError.of(null, null, null, "对象不能是null"));
			return false;
		}
		boolean response = true;
		Object obj = this.getObj();
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Object o = field.get(obj);
			if (o == null) {
				super.addError(ValidateError.of(obj, field, null, "不能为null"));
			}
			response &= (o != null);
		}
		return response;
	}

}
