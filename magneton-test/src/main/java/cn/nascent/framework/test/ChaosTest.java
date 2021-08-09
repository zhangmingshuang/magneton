package cn.nascent.framework.test;

import cn.nascent.framework.test.annotation.TestComponentScan;
import cn.nascent.framework.test.core.ChaosApplication;
import cn.nascent.framework.test.core.ChaosContext;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.generate.TestValueGenerator;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.injector.InjectorFactory;
import cn.nascent.framework.test.supplier.TestBooleanSupplier;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author zhangmsh 2021/7/30
 * @since 2.0.0
 */
@Slf4j
@TestComponentScan("cn.nascent.framework.test")
public final class ChaosTest implements ChaosApplication {

	private static final Config DEFAULT_CONFIG = new Config();

	static {
		ChaosContext.init(ChaosTest.class);
	}

	private ChaosTest() {
	}

	public static <T> T createByAngel(Class<T> clazz) {
		return createByAngel(clazz, DEFAULT_CONFIG);
	}

	public static <T> T createByAngel(Class<T> clazz, Config config) {
		return create(clazz, config, InjectType.ANGEL);
	}

	/**
	 * 恶魔模式
	 *
	 * <p>
	 * 该模式创建的类对象存在非常多的不确定性，通常用来进行混乱测试时使用。
	 *
	 * <p>
	 * 该模式可能会返回 {@code null}， 该概率由 {@link Config#getDemonNullProbability()} 控制。
	 *
	 * <p>
	 * 如果是一个对象，并且没有返回{@code null}，则对象的内部字段数据也有可能为 {@code null}，概率同上。
	 *
	 * <p>
	 * 如果对象的字段注解了标准的 JSR303注解，则会根据该注释的意义进行反向注入。比如 {@code @NotEmpty}则一定会返回{@code null}
	 *
	 * <p>
	 * 比如{@code @Min(1)}，则一定会返回小于1的数。
	 * @param clazz 要创建的类
	 * @param <T> 泛型
	 * @return 创建的类
	 */
	@Nullable
	public static <T> T createByDemon(Class<T> clazz) {
		return createByDemon(clazz, DEFAULT_CONFIG);
	}

	/**
	 * 恶魔模式
	 *
	 * <p>
	 * 该模式创建的类对象存在非常多的不确定性，通常用来进行混乱测试时使用。
	 *
	 * <p>
	 * 该模式可能会返回 {@code null}， 该概率由 {@link Config#getDemonNullProbability()} 控制。
	 *
	 * <p>
	 * 如果是一个对象，并且没有返回{@code null}，则对象的内部字段数据也有可能为 {@code null}，概率同上。
	 *
	 * <p>
	 * 如果对象的字段注解了标准的 JSR303注解，则会根据该注释的意义进行反向注入。比如 {@code @NotEmpty}则一定会返回{@code null}
	 *
	 * <p>
	 * 比如{@code @Min(1)}，则一定会返回小于1的数。
	 * @param clazz 要创建的类
	 * @param <T> 泛型
	 * @return 创建的类
	 */
	@Nullable
	public static <T> T createByDemon(Class<T> clazz, Config config) {
		return create(clazz, config, InjectType.DEMON);
	}

	public static <T> T create(Class<T> clazz, Config config, InjectType injectType) {
		Inject.setRoot(clazz);
		return Objects.requireNonNull(ChaosContext.get(InjectorFactory.class))
				.inject(Optional.ofNullable(config).orElse(DEFAULT_CONFIG), Inject.of(clazz), injectType);
	}

	public static TestBooleanSupplier booleanSupplier() {
		return TestBooleanSupplier.getInstance();
	}

	public static TestValueGenerator valueGenerator() {
		return TestValueGenerator.getInstance();
	}

}
