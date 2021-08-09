package cn.nascent.framework.test.injector;

import cn.nascent.framework.test.util.FieldUtil;
import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@Setter
@Getter
@ToString
public class Inject<T> {

	private static final ThreadLocal<Class> ROOT = new ThreadLocal<>();

	public static final void setRoot(Class root) {
		ROOT.set(root);
	}

	public static final Class getRoot() {
		return ROOT.get();
	}

	private Class<T> inectType;

	@Nullable
	private Object object;

	public static <T> Inject<T> of(Class<T> type) {
		return new Inject(type, null);
	}

	public static <T> Inject<T> of(Class<T> inectType, Object object) {
		return new Inject(Preconditions.checkNotNull(inectType), object);
	}

	private Inject(Class inectType, Object object) {
		this.inectType = inectType;
		this.object = object;
	}

	public String getName() {
		return this.inectType.getName();
	}

	public boolean isArray() {
		return this.inectType.isArray();
	}

	public boolean isPrimitive() {
		return this.inectType.isPrimitive();
	}

	public Set<Field> getFields() {
		return FieldUtil.getFields(this.inectType);
	}

	public boolean isInterface() {
		return this.inectType.isInterface();
	}

}
