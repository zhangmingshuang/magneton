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

package org.magneton.test.injector;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import org.magneton.test.annotation.TestAutowired;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.core.AfterAutowrited;
import org.magneton.test.core.ChaosContext;
import org.magneton.test.core.InjectType;
import org.magneton.test.core.TraceChain;
import org.magneton.test.parser.Definition;
import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

/**
 * 注入工厂.
 *
 * @author zhangmsh 2021/8/17
 * @since 2.0.0
 */
@Slf4j
@TestComponent
public class InjectorFactory implements AfterAutowrited {

	private final Map<Class, Injector> injectorTypeRef = Maps.newHashMap();

	@TestAutowired
	private List<Injector> injectors;

	public static InjectorFactory getInstance() {
		InjectorFactory injectorFactory = ChaosContext.getComponent(InjectorFactory.class);
		Verify.verifyNotNull(injectorFactory, "not injector factory found");
		return injectorFactory;
	}

	@Nullable
	public <T> T inject(Definition definition, Config config, InjectType injectType) {
		Injector injector = Preconditions.checkNotNull(this.getInjector(definition.getClazz()));
		TraceChain.current().inject(definition.getClazz(), injector);

		Object object = injector.inject(definition, config, injectType);
		this.injectDataToObject(object, definition.getChildDefinitions(), config, injectType);
		return (T) object;
	}

	private void injectDataToObject(Object object, List<Definition> childDefinitions, Config config,
			InjectType injectType) {
		if (childDefinitions == null || childDefinitions.isEmpty()) {
			return;
		}
		for (Definition childDefinition : childDefinitions) {
			Class childClazz = childDefinition.getClazz();
			Injector injector = Preconditions.checkNotNull(this.getInjector(childClazz), "%s not injector founded",
					childClazz);
			TraceChain.current().injectTo(childClazz,
					childDefinition.getField() != null ? childDefinition.getField().getName() : injector,
					object.getClass());
			Object childObject = injector.inject(childDefinition, config, injectType);
			if (childObject == null) {
				continue;
			}
			Field field = childDefinition.getField();
			if (field == null || Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
				continue;
			}
			field.setAccessible(true);
			try {
				field.set(object, childObject);
				this.injectDataToObject(childObject, childDefinition.getChildDefinitions(), config, injectType);
			}
			catch (IllegalAccessException e) {
				log.error("set value:{} to {}#{} error", childObject, childClazz, field);
				log.error("set field value error", e);
			}
		}
	}

	protected Injector getInjector(Class clazz) {
		Preconditions.checkNotNull(clazz, "clazz must not null");
		Injector injector = this.injectorTypeRef.get(clazz);
		if (injector != null) {
			return injector;
		}
		Set<Entry<Class, Injector>> entries = this.injectorTypeRef.entrySet();
		for (Entry<Class, Injector> entry : entries) {
			if (!entry.getKey().isAssignableFrom(clazz)) {
				continue;
			}
			injector = entry.getValue();
			if (injector == null || injector.getClass() == ObjectInjector.class) {
				continue;
			}
			if (injector.afterTypes().length < 1) {
				this.injectorTypeRef.put(clazz, injector);
				return injector;
			}
			for (Class afterClass : injector.afterTypes()) {
				if (afterClass.isAssignableFrom(clazz)) {
					injector = this.injectorTypeRef.get(afterClass);
					if (injector != null) {
						return injector;
					}
				}
			}
			return injector;
		}
		return this.injectorTypeRef.get(Object.class);
	}

	@Override
	public void afterAutowrited() {
		Verify.verifyNotNull(this.injectors, "没有找到对应的注射器");
		for (Injector injector : this.injectors) {
			Class[] types = injector.getTypes();
			if (types == null || types.length < 1) {
				continue;
			}
			for (Class type : types) {
				this.injectorTypeRef.put(type, injector);
			}
		}
	}

}
