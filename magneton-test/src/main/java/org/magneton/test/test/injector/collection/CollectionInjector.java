package org.magneton.test.test.injector.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.magneton.test.test.annotation.TestAutowired;
import org.magneton.test.test.annotation.TestComponent;
import org.magneton.test.test.config.Config;
import org.magneton.test.test.config.ConfigProcessorFactory;
import org.magneton.test.test.core.InjectType;
import org.magneton.test.test.exception.UnsupportedTypeCreateException;
import org.magneton.test.test.injector.AbstractInjector;
import org.magneton.test.test.injector.InjectorFactory;
import org.magneton.test.test.parser.Definition;
import org.magneton.test.test.parser.ParserFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * .
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
@TestComponent
public class CollectionInjector extends AbstractInjector {

	@TestAutowired
	private InjectorFactory injectorFactory;

	@Override
	protected Object createValue(Definition definition, Config config, InjectType injectType) {
		Class clazz = definition.getClazz();
		Collection collection;
		if (clazz.isInterface()) {
			if (Set.class.isAssignableFrom(clazz)) {
				collection = Sets.newHashSet();
			}
			else if (Collection.class.isAssignableFrom(clazz) || List.class.isAssignableFrom(clazz)) {
				collection = Lists.newArrayList();
			}
			else {
				throw new UnsupportedTypeCreateException("集合类型%s暂不支持注入", clazz);
			}
		}
		else {
			try {
				collection = (Collection) clazz.getConstructor().newInstance();
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
			return Collections.emptyList();
		}
		List<Class<?>> generics = definition.getGenerics();
		Class genericClass;
		if (generics == null || generics.isEmpty()) {
			// 没有泛型注明，则是一个Object。 使用Object则统一采用字段串代替
			genericClass = String.class;
		}
		else {
			genericClass = generics.get(0);
		}

		Definition genericDefinition = ParserFactory.getInstance().parse(genericClass);
		for (int i = 0; i < size; i++) {
			Object value = this.injectorFactory.inject(genericDefinition, config, injectType);
			if (value == null) {
				continue;
			}
			collection.add(value);
		}
		return collection;
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { Collection.class, Collection[].class };
	}

}
