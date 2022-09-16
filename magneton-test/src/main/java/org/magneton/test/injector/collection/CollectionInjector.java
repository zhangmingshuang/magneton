/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号401
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.test.injector.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
