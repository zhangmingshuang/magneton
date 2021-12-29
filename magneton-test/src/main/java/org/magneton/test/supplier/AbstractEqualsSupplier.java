package org.magneton.test.supplier;

import com.google.common.base.Strings;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;
import lombok.SneakyThrows;
import org.magneton.test.util.FieldUtil;

/**
 * .
 *
 * @author zhangmsh 2021/8/6
 * @since 2.0.0
 */
public abstract class AbstractEqualsSupplier<T> extends AbstractBooleanSupplier<T> {

	private final Object element1;

	private final Object element2;

	protected <E> AbstractEqualsSupplier(E element1, E element2) {
		super(null);
		this.element1 = element1;
		this.element2 = element2;
	}

	@SneakyThrows
	@Override
	protected boolean doBooleanJudge() {
		if (Objects.equals(this.element1, this.element2)) {
			return true;
		}
		Class<?> clazz1 = this.element1.getClass();
		Class<?> clazz2 = this.element2.getClass();
		if (clazz1 != clazz2) {
			String error = Strings.lenientFormat("%s与%s不是相同的类", clazz1.getName(), clazz2.getName());
			this.addError(ValidateError.of(null, null, null, error));
			return false;
		}
		StringBuilder builder = new StringBuilder(128);
		boolean reponse = true;
		Set<Field> fields = FieldUtil.getFields(clazz1);
		for (Field field : fields) {
			field.setAccessible(true);
			Object value1 = field.get(this.element1);
			Object value2 = field.get(this.element2);
			if (!Objects.equals(value1, value2)) {
				builder.append(clazz1.getSimpleName()).append("#").append(field.getName()).append("值[").append(value1)
						.append("]与[").append(value2).append("]不相等;");
				reponse = this.doFeieldValueNotEqualsProcess(field.getType(), value1, value2);
			}
		}
		if (!reponse) {
			this.addError(ValidateError.of(null, null, null, builder.toString()));
		}
		builder.setLength(0);
		return reponse;
	}

	protected boolean doFeieldValueNotEqualsProcess(Class<?> fieldClass, Object fieldValue1, Object fieldValue2) {
		return false;
	}

}
