package org.magneton.test.injector.collection;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import org.magneton.test.annotation.TestAutowired;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.config.ConfigProcessorFactory;
import org.magneton.test.core.InjectType;
import org.magneton.test.exception.UnsupportedTypeCreateException;
import org.magneton.test.injector.AbstractInjector;
import org.magneton.test.injector.InjectorFactory;
import org.magneton.test.parser.Definition;
import org.magneton.test.parser.ParserFactory;

/**
 * .
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
@TestComponent
public class MapInjector extends AbstractInjector {

	@TestAutowired
	private InjectorFactory injectorFactory;

	@Nullable
	@Override
	protected Object createValue(Definition definition, Config config, InjectType injectType) {
		Class clazz = definition.getClazz();
		Map map;
		if (clazz.isInterface()) {
			if (SortedMap.class.isAssignableFrom(clazz)) {
				map = Maps.newTreeMap();
			}
			else if (Map.class.isAssignableFrom(clazz)) {
				map = Maps.newHashMap();
			}
			else {
				throw new UnsupportedTypeCreateException("不支持的Map类型%s", clazz);
			}
		}
		else {
			try {
				map = (Map) clazz.getConstructor().newInstance();
			}
			catch (Throwable e) {
				throw new UnsupportedTypeCreateException(e);
			}
		}
		int size = ConfigProcessorFactory.of(injectType).nextSize(config, definition);
		if (size < 0) {
			return null;
		}
		else if (size == 0) {
			return Collections.emptyMap();
		}
		List<Class<?>> generics = definition.getGenerics();
		Class keyGeneric;
		Class valueGeneric;
		if (generics == null || generics.isEmpty()) {
			keyGeneric = String.class;
			valueGeneric = String.class;
		}
		else {
			keyGeneric = generics.get(0);
			valueGeneric = generics.get(1);
		}
		Definition keyGenericDefinition = ParserFactory.getInstance().parse(keyGeneric);
		Definition valueGenericDefinition = ParserFactory.getInstance().parse(valueGeneric);
		for (int i = 0; i < size; i++) {
			Object keyValue = this.injectorFactory.inject(keyGenericDefinition, config, injectType);
			Object valueValue = this.injectorFactory.inject(valueGenericDefinition, config, injectType);
			if (keyValue != null) {
				map.put(keyValue, valueValue);
			}
		}

		return map;
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { Map.class, Map[].class };
	}

}
