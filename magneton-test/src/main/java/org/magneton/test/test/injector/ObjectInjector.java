package org.magneton.test.test.injector;

import org.magneton.test.test.annotation.TestAutowired;
import org.magneton.test.test.annotation.TestComponent;
import org.magneton.test.test.config.Config;
import org.magneton.test.test.core.InjectType;
import org.magneton.test.test.parser.Definition;
import org.magneton.test.test.util.PrimitiveUtil;
import lombok.SneakyThrows;

/**
 * .
 *
 * @author zhangmsh 2021/8/18
 * @since 2.0.0
 */
@TestComponent
public class ObjectInjector extends AbstractInjector {

	@TestAutowired
	private InjectorFactory injectorFactory;

	@SneakyThrows
	@Override
	protected Object createValue(Definition definition, Config config, InjectType injectType) {
		Class clazz = definition.getClazz();
		if (PrimitiveUtil.isPrimitive(clazz)) {
			return this.injectorFactory.getInjector(clazz).inject(definition, config, injectType);
		}
		return clazz.getConstructor().newInstance();
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { Object.class };
	}

}