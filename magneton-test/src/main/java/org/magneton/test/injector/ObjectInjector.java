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

import org.magneton.test.annotation.TestAutowired;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.magneton.test.parser.Definition;
import org.magneton.test.util.PrimitiveUtil;
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
