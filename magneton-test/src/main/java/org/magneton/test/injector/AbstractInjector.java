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

import java.lang.reflect.Array;

import javax.annotation.Nullable;

import org.magneton.test.config.Config;
import org.magneton.test.config.ConfigProcessor;
import org.magneton.test.config.ConfigProcessorFactory;
import org.magneton.test.core.InjectType;
import org.magneton.test.parser.Definition;
import org.magneton.test.util.PrimitiveUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@Slf4j
public abstract class AbstractInjector implements Injector {

	@Override
	public <T> T inject(Definition definition, Config config, InjectType injectType) {
		Class clazz = definition.getClazz();
		ConfigProcessor configProcessor = ConfigProcessorFactory.of(injectType);
		if (configProcessor.nullable(config, definition)) {
			return (T) PrimitiveUtil.defaultValue(clazz);
		}
		if (clazz.isArray()) {
			Class<?> componentType = clazz.getComponentType();
			int length = configProcessor.nextSize(config, definition);
			if (length < 1) {
				return null;
			}
			Object array = Array.newInstance(componentType, length);
			for (int i = 0; i < length; i++) {
				Object value = this.injectValue(definition.resetClazz(componentType), config, injectType);
				Array.set(array, i, value);
			}
			return (T) array;
		}
		return (T) this.injectValue(definition, config, injectType);
	}

	@Nullable
	protected Object injectValue(Definition definition, Config config, InjectType injectType) {
		Class clazz = definition.getClazz();
		ConfigProcessor configProcessor = ConfigProcessorFactory.of(injectType);
		if (configProcessor.nullable(config, definition)) {
			return PrimitiveUtil.defaultValue(clazz);
		}
		return this.createValue(definition, config, injectType);
	}

	@Nullable
	protected abstract Object createValue(Definition definition, Config config, InjectType injectType);

}
