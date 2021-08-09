package cn.nascent.framework.test.injector.object;

import cn.nascent.framework.test.annotation.TestAutowired;
import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.annotation.TestSort;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.AbstractInjector;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.injector.InjectorFactory;
import cn.nascent.framework.test.util.PrimitiveUtil;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@TestSort
@TestComponent
@Slf4j
public class ObjectInjector extends AbstractInjector {

	@TestAutowired
	private InjectorFactory injectorFactory;

	@SuppressWarnings({ "ForLoopReplaceableByForEach", "OverlyBroadCatchBlock" })
	@Override
	protected Object createValue(Config config, InjectType injectType, Inject inject) {
		if (inject.getInectType() == Object.class) {
			return null;
		}
		Set<Field> fields = inject.getFields();
		Field field = null;
		Object value = null;
		try {
			boolean hadNotFields = fields.size() < 1;
			if (hadNotFields && inject.isPrimitive()) {
				return this.injectorFactory.inject(config, Inject.of(PrimitiveUtil.random()), injectType);
			}
			Object obj = inject.getInectType().getConstructor().newInstance();
			if (hadNotFields) {
				return obj;
			}
			for (Field f : fields) {
				field = f;
				if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
					continue;
				}
				Class<?> fieldType = field.getType();
				value = this.injectorFactory.inject(config, Inject.of(fieldType, field), injectType);
				field.setAccessible(true);
				field.set(obj, value);
			}
			return obj;
		}
		catch (Throwable e) {
			log.error("{}#{}设置值{}错误", inject.getInectType(), field, value);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Object createArray(Config config, InjectType injectType, Inject inject, Integer length) {
		Class componentType = inject.getInectType().getComponentType();
		return Array.newInstance(componentType, length);
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { Object.class };
	}

}
