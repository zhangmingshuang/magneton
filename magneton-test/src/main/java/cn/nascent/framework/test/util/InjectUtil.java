package cn.nascent.framework.test.util;

import cn.nascent.framework.test.injector.Inject;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
public class InjectUtil {

	private InjectUtil() {
	}

	@Nullable
	public static Inject get(Inject inject, int index) {
		Inject genericClass = GenericUtil.getClass(inject.getInectType(), index);
		if (genericClass != null) {
			return genericClass;
		}
		if (inject.getObject() != null) {
			return GenericUtil.getClass(inject.getObject(), index);
		}
		return null;
	}

	public static Inject getOrElse(Inject inject, int index, Class defaultClass) {
		Inject newInject = get(inject, index);
		return Optional.ofNullable(newInject).orElse(Inject.of(defaultClass, inject.getObject()));
	}

}
