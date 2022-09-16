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

package org.magneton.test;

import org.magneton.test.annotation.TestComponentScan;
import org.magneton.test.config.Config;
import org.magneton.test.core.ChaosApplication;
import org.magneton.test.core.ChaosContext;
import org.magneton.test.core.InjectType;
import org.magneton.test.core.TraceChain;
import org.magneton.test.injector.InjectorFactory;
import org.magneton.test.model.generate.ValueGenerator;
import org.magneton.test.parser.Definition;
import org.magneton.test.parser.ParserFactory;
import org.magneton.test.supplier.TestBooleanSupplier;
import com.google.common.base.Preconditions;
import java.util.Optional;

/**
 * .
 *
 * @author zhangmsh 2021/8/17
 * @since 2.0.0
 */
@TestComponentScan
public class ChaosTest implements ChaosApplication {

	private static final Config DEFAULT_CONFIG = new Config();

	public static <T> T createExcepted(Class<T> clazz) {
		return create(clazz, null, InjectType.EXPECTED);
	}

	public static <T> T createExcepted(Class<T> clazz, Config config) {
		return create(clazz, config, InjectType.EXPECTED);
	}

	public static <T> T createAntiExcepted(Class<T> clazz) {
		return create(clazz, null, InjectType.ANTI_EXPECTED);
	}

	public static <T> T createAntiExcepted(Class<T> clazz, Config config) {
		return create(clazz, config, InjectType.ANTI_EXPECTED);
	}

	public static <T> T createDefaultValue(Class<T> clazz) {
		return create(clazz, null, InjectType.DEFAULT_VALUE);
	}

	public static <T> T create(Class<T> clazz, Config config, InjectType injectType) {
		Preconditions.checkNotNull(clazz, "clazz must not be null");
		Preconditions.checkNotNull(injectType, "injectType must not be null");
		TraceChain.current().start(clazz);
		try {
			Definition definition = ParserFactory.getInstance().parse(clazz);
			return InjectorFactory.getInstance().inject(definition, Optional.ofNullable(config).orElse(DEFAULT_CONFIG),
					injectType);
		}
		finally {
			TraceChain.current().end();
		}
	}

	public static TestBooleanSupplier booleanSupplier() {
		return TestBooleanSupplier.getInstance();
	}

	public static ValueGenerator valueGenerator() {
		return ValueGenerator.getInstance();
	}

	static {
		ChaosContext.init(ChaosTest.class);
	}

}
