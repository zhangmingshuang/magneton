package org.magneton.test.test;

import org.magneton.test.test.annotation.TestComponentScan;
import org.magneton.test.test.config.Config;
import org.magneton.test.test.core.ChaosApplication;
import org.magneton.test.test.core.ChaosContext;
import org.magneton.test.test.core.InjectType;
import org.magneton.test.test.core.TraceChain;
import org.magneton.test.test.injector.InjectorFactory;
import org.magneton.test.test.model.generate.ValueGenerator;
import org.magneton.test.test.parser.Definition;
import org.magneton.test.test.parser.ParserFactory;
import org.magneton.test.test.supplier.TestBooleanSupplier;
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

	static {
		ChaosContext.init(ChaosTest.class);
	}

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
		Preconditions.checkNotNull(clazz, "clazz must be not null");
		Preconditions.checkNotNull(injectType, "injectType must be not null");
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

}
